package airpnp.pennapps.com.airpnp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingDetailsActivity extends AppCompatActivity {

    private String email;
    private JSONObject tempJSONObject;
    private JSONArray tempJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);
        email=getIntent().getStringExtra("email");
        getParkingDetails();
    }

    public void getParkingDetails()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://li367-204.members.linode.com/getparkingdetails?email=" + email;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    tempJSONArray = response.getJSONArray("data");
                    for(int i=0;i<tempJSONArray.length();i++)
                    {
                        tempJSONObject=tempJSONArray.getJSONObject(i);
                        String ownerFirstName=tempJSONObject.getString("firstname");
                        String ownerLastName=tempJSONObject.getString("lastname");
                        String hourlyRate=tempJSONObject.getString("rate");
                        String phone=tempJSONObject.getString("phone");
                        TextView textView1=(TextView)findViewById(R.id.textView2);
                        TextView textView2=(TextView)findViewById(R.id.textView2);
                        TextView textView3=(TextView)findViewById(R.id.textView3);
                        textView1.setText(ownerFirstName + " " + ownerLastName);
                        textView2.setText(hourlyRate);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date date=new Date();
                        textView3.setText(simpleDateFormat.format(date));
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(ParkingDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest1);
    }

    public void button1_onClick(View v)
    {

    }
}
