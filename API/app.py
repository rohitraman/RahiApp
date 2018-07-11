from flask import Flask, request, jsonify
import pyinrail
import sys
import RailIN
import json
import os

app = Flask(__name__)

@app.route('/getAvailability',methods = ['POST'])
def getAvailability():
    try:
        req_data = request.get_json()
        print(req_data,file = sys.stderr)
        fromData = req_data['from']
        toData = req_data['to']
        trainNo = req_data['train_no']
        clss = req_data['cls']
        quota = req_data['qt']
        day = req_data['dd']
        month = req_data['mm']
        year = req_data['yyyy']
        ri = RailIN.RailIN()
        data = ri.getAvailability(trainNo,fromData,toData,clss,quota,day,month,year)
        data = jsonify(json.loads(data))
        return data
    except Exception:
        return jsonify(status_code = 400, msg = 'Bad Request'),400

@app.route('/getPNR',methods = ['POST'])
def getPNR():
    try:
        req_data = request.get_json()
        pnr = req_data['pnr']
        enq = RailIN.RailIN()
        # print('hello',file = sys.stderr)
        data = enq.getPNR(pnr)
        # print('hello',file = sys.stderr)
        return jsonify(json.loads(data))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

@app.route('/getRoute',methods = ['POST'])
def getRoute():
    try:
        req_data = request.get_json()
        trainNo = req_data['train_no']
        ri = RailIN.RailIN()
        data = ri.getRoute(trainNo)
        # print(data,file = sys.stderr)
        return jsonify(route = json.loads(json.dumps(data)))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

@app.route('/getAllTrains',methods = ['POST'])
def getAllTrains():
    try:
        req_data = request.get_json()
        fromData = req_data['from']
        toData = req_data['to']
        ri = RailIN.RailIN()
        data = ri.getAllTrains(fromData,toData)
        print(data,file = sys.stderr)
        return jsonify(trains = json.loads(json.dumps(data)))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

@app.route('/getTrainsOn',methods = ['POST'])
def getTrainsOn():
    try:
        req_data = request.get_json()
        fromData = req_data['from']
        toData = req_data['to']
        day = req_data['dd']
        month = req_data['mm']
        year = req_data['yyyy']
        ri = RailIN.RailIN()
        data = ri.getTrainsOn(fromData,toData,day,month,year)
        # print(data,file = sys.stderr)
        return jsonify(trains = json.loads(json.dumps(data)))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

@app.route('/getTrain',methods = ['POST'])
def getTrain():
    try:
        req_data = request.get_json()
        trainNo = req_data['train_no']
        ri = RailIN.RailIN()
        data = ri.getTrain(trainNo)
        # print(data,file = sys.stderr)
        return jsonify(train = json.loads(json.dumps(data)))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

@app.route('/getFare',methods = ['POST'])
def getFare():
    try:
        req_data = request.get_json()
        fromData = req_data['from']
        toData = req_data['to']
        trainNo = req_data['train_no']
        ri = RailIN.RailIN()
        data = ri.getFare(trainNo,fromData,toData)
        return jsonify(json.loads(json.dumps(data)))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

@app.route('/getStatus',methods = ['POST'])
def getStatus():
    try:
        req_data = request.get_json()
        trainNo = req_data['train_no']
        station = req_data['station']
        ri = RailIN.RailIN()
        data = ri.getStatus(trainNo,station)
        return jsonify(status = json.loads(json.dumps(data)))
    except Exception as e:
        return jsonify(status_code = 400, msg = str(e)),400

if __name__ == '__main__':
    port = int(os.environ.get('PORT',5000))
    app.run(host= '0.0.0.0',port=port,debug=True)
