package airpnp.pennapps.com.airpnp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
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
import org.json.JSONException;
import org.json.JSONObject;

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

    private Switch ownerRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ffs.ttf");
        final TextView tv = (TextView) findViewById(R.id.register_text);
        tv.setTypeface(tf);

        ownerRegister = (Switch)
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
                        registerCapitalOne();
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

    public void registerCapitalOne()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url="http://api.reimaginebanking.com/customers?key=1f925e3612560ecb9d6fca3348f05ae8";
        Map<String, Object> jsonParams1 = new HashMap<String, Object>();

        jsonParams1.put("first_name", firstName);
        jsonParams1.put("last_name", lastName);

        Map<String, String> jsonParams2 = new HashMap<String, String>();
        jsonParams2.put("street_number", streetAddress.split(" ")[0]);
        jsonParams2.put("street_name", streetAddress.replace("^\\s*[0-9]+\\s+",""));
        jsonParams2.put("city", city);
        jsonParams2.put("state", state);
        jsonParams2.put("zip", zip);

        jsonParams1.put("address", jsonParams2);

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,

                new JSONObject(jsonParams1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CAP", "onResponse");
                        Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Log.d("CAP", "ErrorResponse");
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(postRequest);
    }
}
