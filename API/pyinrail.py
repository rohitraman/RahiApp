import time
from io import BytesIO
import requests
import pytesseract
import os
import json
from fuzzywuzzy import process
from PIL import Image


class RailwayEnquiry:
    """
    The railway enquiry class which has methods to fetch various enquiry details
    """
    def __init__(self, src=None, dest=None, date=None):
        """
        default values of src, dest, date will be used in queries (if they are not passed explicitly)
        """
        self.session = {}
        self.create_session()
           
    def create_session(self):
        """
        create a session by solving captcha challenge
        """
        self.session['timestamp'] = int(time.time() * 1000)
        url = "http://www.indianrail.gov.in/enquiry/captchaDraw.png?{}".format(self.session['timestamp'])
        r = requests.get(url)
        self.session['cookies'] = r.cookies
        try:
            f = BytesIO(r.content)
        except OSError:
            return None
        im = Image.open(f)
        text = pytesseract.image_to_string(im, lang = 'eng')
        try:
            self.session['captcha'] = eval(text.split("=")[0])
        except:
            self.create_session()


    def get_pnr_status(self, pnr_no):
        """
        get pnr status
        """
        params = {
            "inputCaptcha": self.session['captcha'],
            "inputPnrNo": pnr_no,
            "language": "en",
            "inputPage": "PNR",
            "_": self.session['timestamp']
        }
        r = requests.get("http://www.indianrail.gov.in/enquiry/CommonCaptcha", params=params, cookies=self.session['cookies'])
        try:
            return r.json()
        except:
            if r.json()['errorMessage'] == "Session out or Bot attack":
                self.create_session()
                return self.get_pnr_status(pnr_no)
            else:
                return r.json()['errorMessage']

