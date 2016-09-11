package airpnp.pennapps.com.airpnp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private EditText editText9;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;

    private String responseServer;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ffs.ttf");
        final TextView tv = (TextView) findViewById(R.id.register_text);
        tv.setTypeface(tf);
    }

    public void button1_onClick(View v)
    {
        editText1=(EditText)findViewById(R.id.editText1);
        editText2=(EditText)findViewById(R.id.editText2);
        editText3=(EditText)findViewById(R.id.editText3);
        editText4=(EditText)findViewById(R.id.editText4);
        editText5=(EditText)findViewById(R.id.editText5);
        editText6=(EditText)findViewById(R.id.editText7);
        editText7=(EditText)findViewById(R.id.editText8);
        editText8=(EditText)findViewById(R.id.editText9);
        editText9=(EditText)findViewById(R.id.editText10);

        firstName=editText1.getText().toString();
        lastName=editText2.getText().toString();
        email=editText3.getText().toString();
        phone=editText4.getText().toString();
        password=editText5.getText().toString();
        streetAddress=editText6.getText().toString();
        city=editText7.getText().toString();
        state=editText8.getText().toString();
        zip=editText9.getText().toString();

        registerUser(firstName, lastName, email, phone, password, streetAddress, city, state, zip);
    }

    public void registerUser(String firstName, String lastName, String email, String phone, String password, String streetAddress, String city, String state, String zip)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url1 = "http://li367-204.members.linode.com/register?firstname=" + firstName + "&lastname=" + lastName + "&email=" + email + "&phone=" + phone + "&password=" + password + "&street=" + streetAddress + "&city=" + city + "&state=" + state + "&zip=" + zip;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url1, (String)null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    String status = response.getString("status");
                    if(status.equals("1"))
                    {
                        Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                        AsyncT1 asyncT = new AsyncT1();
                        asyncT.execute();
                        startActivity(intent);
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest1);
    }

    class AsyncT1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.reimaginebanking.com/customers?key=1f925e3612560ecb9d6fca3348f05ae8");

            try {

                JSONObject jsonobj = new JSONObject();

                jsonobj.put("first_name", firstName);
                jsonobj.put("last_name",  lastName);

                JSONObject address=new JSONObject();
                address.put("street_number", streetAddress.split(" ")[0]);
                address.put("street_name", streetAddress.replace("^\\s*[0-9]+\\s+",""));
                address.put("city", city);
                address.put("state", state);
                address.put("zip", zip);

                jsonobj.put("address", address);

                httppost.setHeader("Content-Type", "application/json");

                StringEntity se = new StringEntity(jsonobj.toString());
                Log.i("TAG", jsonobj.toString());

                //se.setContentEncoding("UTF-8");
                se.setContentType("application/json");

                httppost.setEntity(se);

                // Execute HTTP Post Request
                org.apache.http.HttpResponse response=httpclient.execute(httppost);
                InputStream inputStream = response.getEntity().getContent();
                InputStreamToStringExample str = new InputStreamToStringExample();
                String responseServer = str.getStringFromInputStream(inputStream);
                Log.d("response", "response -----" + responseServer);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonObject = new JSONObject(responseServer);
                JSONObject jsonObject1=jsonObject.getJSONObject("objectCreated");
                customerId=jsonObject1.getString("_id");

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static class InputStreamToStringExample {

        public static void main(String[] args) throws IOException {

            // intilize an InputStream
            InputStream is =
                    new ByteArrayInputStream("file content..blah blah".getBytes());

            String result = getStringFromInputStream(is);

            System.out.println(result);
            System.out.println("Done");

        }

        // convert InputStream to String
        private static String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }

    }
}
