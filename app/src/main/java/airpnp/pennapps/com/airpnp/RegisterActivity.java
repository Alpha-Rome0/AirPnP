package airpnp.pennapps.com.airpnp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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
import org.apache.http.config.Registry;
import org.json.JSONArray;
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
    private EditText editText10;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private String customerkey;

    private String ownerRemarks;
    private Switch ownerRegister;
    private boolean isDoingOwnerRegistration;
    private TextInputLayout textWrapperComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ffs.ttf");
        final TextView tv = (TextView) findViewById(R.id.register_text);
        tv.setTypeface(tf);

        textWrapperComments = (TextInputLayout) findViewById(R.id.text_wrapper_comments);

        ownerRegister = (Switch) findViewById(R.id.admin_lock_switch);
        ownerRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textWrapperComments.setVisibility(View.VISIBLE);
                } else {
                    textWrapperComments.setVisibility(View.GONE);
                }
            }
        });
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
        editText10=(EditText)findViewById(R.id.owner_remarks);

        firstName=editText1.getText().toString().replace(" ", "%20");
        lastName=editText2.getText().toString().replace(" ", "%20");
        email=editText3.getText().toString().replace(" ", "%20");
        phone=editText4.getText().toString().replace(" ", "%20");
        password=editText5.getText().toString().replace(" ", "%20");
        streetAddress=editText6.getText().toString().replace(" ", "%20");
        city=editText7.getText().toString().replace(" ", "%20");
        state=editText8.getText().toString().replace(" ", "%20");
        zip=editText9.getText().toString().replace(" ", "%20");

        ownerRemarks = editText10.getText().toString();
        isDoingOwnerRegistration = ownerRegister.isChecked();

        registerUser(firstName, lastName, email, phone, password, streetAddress, city, state, zip);
    }

    public void registerUser(String firstName, String lastName, String email, String phone, String password, String streetAddress, String city, String state, String zip)
    {
        registerCapitalOne();
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
                        try {
                            customerkey = response.getJSONObject("objectCreated").getString("_id");
                            Log.d("CAP", response.toString());
                            Toast.makeText(RegisterActivity.this, customerkey, Toast.LENGTH_SHORT).show();

                            RequestQueue queue1 = Volley.newRequestQueue(RegisterActivity.this);
                            String url1 = "http://li367-204.members.linode.com/register?firstname=" + firstName + "&lastname=" + lastName + "&email=" + email + "&phone=" + phone + "&password=" + password + "&street=" + streetAddress + "&city=" + city + "&state=" + state + "&zip=" + zip + "&customerkey=" + customerkey;
                            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url1, (String)null, new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    try
                                    {
                                        JSONArray jsonArray = response.getJSONArray("result");
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String status = jsonObject.getString("status");
                                        if(status.equals("1"))
                                        {
                                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                    catch(JSONException e)
                                    {
                                        e.printStackTrace();
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
                            queue1.add(jsonObjectRequest1);

                        }
                        catch(Exception e)
                        {
                            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
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
