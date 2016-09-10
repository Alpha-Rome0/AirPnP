package airpnp.pennapps.com.airpnp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;

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
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);
        editText4 = (EditText)findViewById(R.id.editText4);

        String firstName = editText1.getText().toString();
        String lastName = editText2.getText().toString();
        String email = editText3.getText().toString();
        String password = editText4.getText().toString();

        registerUser(firstName, lastName, email, password);
    }

    public void registerUser(String firstName, String lastName, String email, String password)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url1 = "http://li367-204.members.linode.com/register?firstname=" + firstName + "&lastname=" + lastName + "&email=" + email + "&password=" + password;
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
}
