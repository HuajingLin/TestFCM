package edu.temple.testfcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button button;
    RequestQueue queue; //volley

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        //FirebaseMessaging.getInstance().subscribeToTopic("test");
        final String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Test ====> ", "Token: " + token);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage_JSON_Volley("testing: message to app");
            }
        });
    }

    private void sendMessage_JSON_Volley(String msg){
        String topic = "/topics/test";
        JSONObject data = new JSONObject();
        JSONObject dataBody = new JSONObject();
        try {
            dataBody.put("title", "msg_title");
            dataBody.put("message", msg);
            dataBody.put("key1", "value1");
            dataBody.put("key2", "value2");
            data.put("to", topic);
            data.put("data", dataBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        Method Type: POST
        URL: https://fcm.googleapis.com/fcm/send
        Headers:
        Authorization: key="Firebase server key"
        Content-Type: application/json
        Body:
        {
            "to": "/topics/topic_name",
            "data": {
            "title": "Notification title",
                    "message": "Notification message",
                    "key1" : "value1",
                    "key2" : "value2" //additional data you want to pass
             }
        }*/

        String url = "https://fcm.googleapis.com/fcm/send";
        final String serverKey = "key=" + "AAAAiGhxOvs:APA91bH3mX_VrNOLbfNHPvqveIWe0PTQ8sdvtUOwvbWXszlIDOfub09VXIHHG-_toZCh8b7-Dn2RI52iCKnffHDEmdfejJYLmAbIXbyi0ULwW4hKWKo4TON2V9T7tnAdNXEuTSIm2s2m";
        final String contentType = "application/json";
        Log.d("key ===>", serverKey);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("FCM_Response ===>", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response ===>", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Alif");
                params.put("domain", "http://itsalif.info");
                return params;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", serverKey);
                headers.put("Content-Type", contentType);
                return headers;
            }
        };
        queue.add(jsonObjReq);

    }
}
