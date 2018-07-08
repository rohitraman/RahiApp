package com.example.roshan.rahiapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
//Format of response
//        {'coach_types': {'CC': '0', '2S': '0', 'GN': '1', 'SL': '1', '2A': '1', '3A': '1', '1A': '0'}, 'region': 'NE', 'train_type': 'Super Fast', 'train_base': {'source_stn_name': 'Thiruvananthapuram Central', 'train_id': '2134', 'source_depart': '06.15', 'from_stn_code': 'SRR', 'from_stn_name': 'Shoranur Jn', 'type': 'SUPERFAST', 'running_days': '0110001', 'to_time': '23.05', 'to_stn_name': 'Chennai Central', 'dstn_stn_code': 'GKP', 'travel_time': '09.55', 'dstn_stn_name': 'Gorakhpur', 'distance_from_to': '595', 'train_name': 'RAPTISAGAR EXP', 'train_no': '12512', 'source_stn_code': 'TVC', 'from_time': '13.10', 'average_speed': '60', 'to_stn_code': 'MAS', 'dstn_reach': '15.20'}, 'coach_arrangement': [{'1': {'coach_id': '', 'tag': '', 'type': 'En'}}, {'2': {'coach_id': '', 'tag': '', 'type': 'GS'}}, {'3': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'4': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'5': {'coach_id': 'S1', 'tag': 'S', 'type': 'SL'}}, {'6': {'coach_id': 'S2', 'tag': 'S', 'type': 'SL'}}, {'7': {'coach_id': 'S3', 'tag': 'S', 'type': 'SL'}}, {'8': {'coach_id': 'S4', 'tag': 'S', 'type': 'SL'}}, {'9': {'coach_id': 'S5', 'tag': 'S', 'type': 'SL'}}, {'10': {'coach_id': 'S6', 'tag': 'S', 'type': 'SL'}}, {'11': {'coach_id': 'S7', 'tag': 'S', 'type': 'SL'}}, {'12': {'coach_id': 'S8', 'tag': 'S', 'type': 'SL'}}, {'13': {'coach_id': 'S9', 'tag': 'S', 'type': 'SL'}}, {'14': {'coach_id': 'S10', 'tag': 'S', 'type': 'SL'}}, {'15': {'coach_id': 'S11', 'tag': 'S', 'type': 'SL'}}, {'16': {'coach_id': '', 'tag': '', 'type': 'PC'}}, {'17': {'coach_id': 'B1', 'tag': 'B', 'type': '3A'}}, {'18': {'coach_id': 'B2', 'tag': 'B', 'type': '3A'}}, {'19': {'coach_id': 'B3', 'tag': 'B', 'type': '3A'}}, {'20': {'coach_id': 'B4', 'tag': 'B', 'type': '3A'}}, {'21': {'coach_id': 'A1', 'tag': 'A', 'type': '2A'}}, {'22': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'23': {'coach_id': '', 'tag': '', 'type': 'UR'}}, {'24': {'coach_id': '', 'tag': '', 'type': 'GS'}}], 'rake_share': ['12511', '12512', '12589', '12590', '12591', '12592']}

public class GetTrainsOnActivity extends AppCompatActivity {
    EditText etFrom, etTo, etDate;
    Button button;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_trains_on);
        etFrom = (EditText) findViewById(R.id.et_from_2);
        etTo = (EditText) findViewById(R.id.et_to_2);
        etDate = (EditText) findViewById(R.id.et_date);

        button = (Button) findViewById(R.id.button2);
        etDate.setShowSoftInputOnFocus(false);
        Calendar calendar = Calendar.getInstance();
        final int dd = calendar.get(Calendar.DAY_OF_MONTH);
        final int mm = calendar.get(Calendar.MONTH);
        final int yy = calendar.get(Calendar.YEAR);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(GetTrainsOnActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date = String.valueOf(i2) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i);
                        etDate.setText(date);
                    }
                }, yy, mm, dd);
                dialog.show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] dates = date.split("-");
                String dd = dates[0];
                String mm = dates[1];
                String yy = dates[2];
                JSONObject object = new JSONObject();
                try {
                    object.put("from", etFrom.getText().toString().toUpperCase());
                    object.put("to", etTo.getText().toString().toUpperCase());
                    object.put("dd", Integer.parseInt(dd));
                    object.put("mm", Integer.parseInt(mm));
                    object.put("yyyy", Integer.parseInt(yy));
                    final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://railwayapi.herokuapp.com/getTrainsOn", object, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("Last one",response.toString());
                                JSONObject train_base = response.getJSONObject("trains");
                                String source_stn_name = train_base.getString("source_stn_name");
                                String source_depart = train_base.getString("source_depart");
                                String from_stn_name = train_base.getString("from_stn_name");
                                String dstn_stn_name = train_base.getString("dstn_stn_name");
                                String dstn_stn_code = train_base.getString("dstn_stn_code");
                                String train_name = train_base.getString("train_name");
                                String train_no = train_base.getString("train_no");
                                String from_stn_code = train_base.getString("from_stn_code");
                                String to_stn_code = train_base.getString("to_stn_code");
                                String to_stn_name = train_base.getString("to_stn_name");
                                String to_time = train_base.getString("to_time");
                                String from_time = train_base.getString("from_time");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Eror", error.getLocalizedMessage());
                            NetworkResponse response = error.networkResponse;
                            if (error instanceof ServerError && response != null) {
                                try {
                                    String string = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                    Log.i("Logging", string);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            ;
                        }
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue queue = Volley.newRequestQueue(GetTrainsOnActivity.this);
                    queue.add(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
