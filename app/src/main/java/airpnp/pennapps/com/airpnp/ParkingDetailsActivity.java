package airpnp.pennapps.com.airpnp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ParkingDetailsActivity extends AppCompatActivity {

    private String email;
    private JSONObject tempJSONObject;
    private JSONArray tempJSONArray;

    Button arrivalStartDateBtn;
    Button arrivalStartTimeBtn;
    Button arrivalEndDateBtn;
    Button arrivalEndTimeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);
        email=getIntent().getStringExtra("owner_email");
        getParkingDetails();

        SimpleDateFormat sdfNOW = new SimpleDateFormat("MMM dd, yyyy");
        arrivalStartDateBtn = (Button) findViewById(R.id.btn_start_date);
        arrivalStartDateBtn.setText(sdfNOW.format(new Date()));
        arrivalStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(true);
            }
        });
        arrivalStartTimeBtn = (Button) findViewById(R.id.btn_start_time);
        arrivalStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(true);
            }
        });
        arrivalEndDateBtn = (Button) findViewById(R.id.btn_end_date);
        arrivalEndDateBtn.setText(sdfNOW.format(new Date()));
        arrivalEndDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(false);
            }
        });
        arrivalEndTimeBtn = (Button) findViewById(R.id.btn_end_time);
        arrivalEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(false);
            }
        });

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
                    //Toast.makeText(ParkingDetailsActivity.this, "" + tempJSONArray.length(), Toast.LENGTH_LONG).show();
                    tempJSONObject=tempJSONArray.getJSONObject(0);
                    String ownerFirstName=tempJSONObject.getString("firstname");
                    String ownerLastName=tempJSONObject.getString("lastname");
                    String hourlyRate=tempJSONObject.getString("rate");
                    String phone=tempJSONObject.getString("phone");
                    TextView textView1=(TextView)findViewById(R.id.tv_owner_name);
                    TextView textView2=(TextView)findViewById(R.id.tv_phone);
                    TextView textView3=(TextView)findViewById(R.id.tv_rate);
                    TextView textView4=(TextView)findViewById(R.id.tv_rules);
                    textView1.setText(ownerFirstName + " " + ownerLastName);
                    textView2.setText(phone);
                    textView3.setText("$" + hourlyRate + "/hr");
                    textView4.setText("No Minivans please");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date=new Date();
                    //textView3.setText(simpleDateFormat.format(date));
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

    public void setDate(final boolean isArrival) {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                        Calendar calendar = new GregorianCalendar(year,monthOfYear, dayOfMonth);
                        if (isArrival) {
                            arrivalStartDateBtn.setText(sdf.format(calendar.getTime()));
                        } else {
                            arrivalEndDateBtn.setText(sdf.format(calendar.getTime()));
                        }

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void setTime(boolean isArrival) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void bookParking(View view) {
    }
}