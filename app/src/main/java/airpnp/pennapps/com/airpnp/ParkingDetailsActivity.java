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

    public Calendar startDate;
    public Calendar endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);
        email=getIntent().getStringExtra("owner_email");
        getParkingDetails();

        // Setting initial calendar values
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        SimpleDateFormat sdfDateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat sdfTimeFormatter = new SimpleDateFormat("h:mm a");

        arrivalStartDateBtn = (Button) findViewById(R.id.btn_start_date);
        arrivalStartDateBtn.setText(sdfDateFormatter.format(startDate.getTime()));
        arrivalStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(true);
            }
        });
        arrivalStartTimeBtn = (Button) findViewById(R.id.btn_start_time);
        arrivalStartTimeBtn.setText(sdfTimeFormatter.format(startDate.getTime()));
        arrivalStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(true);
            }
        });
        arrivalEndDateBtn = (Button) findViewById(R.id.btn_end_date);
        arrivalEndDateBtn.setText(sdfDateFormatter.format(endDate.getTime()));
        arrivalEndDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(false);
            }
        });
        arrivalEndTimeBtn = (Button) findViewById(R.id.btn_end_time);
        arrivalEndTimeBtn.setText(sdfTimeFormatter.format(endDate.getTime()));
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
        Calendar currCalendar;
        if (isArrival) {
            currCalendar = startDate;
        } else {
            currCalendar = endDate;
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                        // Set the new calendar dates
                        if (isArrival) {
                            startDate.set(Calendar.YEAR, year);
                            startDate.set(Calendar.MONTH, monthOfYear);
                            startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            arrivalStartDateBtn.setText(sdf.format(startDate.getTime()));
                        } else {
                            endDate.set(Calendar.YEAR, year);
                            endDate.set(Calendar.MONTH, monthOfYear);
                            endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            arrivalEndDateBtn.setText(sdf.format(endDate.getTime()));
                        }

                    }
                },
                currCalendar.get(Calendar.YEAR),
                currCalendar.get(Calendar.MONTH),
                currCalendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void setTime(final boolean isArrival) {
        Calendar currCalendar;
        if (isArrival) {
            currCalendar = startDate;
        } else {
            currCalendar = endDate;
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                        // Set the new calendar times
                        if (isArrival) {
                            startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            startDate.set(Calendar.MINUTE, minute);
                            startDate.set(Calendar.SECOND, second);

                            arrivalStartTimeBtn.setText(sdf.format(startDate.getTime()));
                        } else {
                            endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            endDate.set(Calendar.MINUTE, minute);
                            endDate.set(Calendar.SECOND, second);

                            arrivalEndTimeBtn.setText(sdf.format(endDate.getTime()));
                        }
                    }
                },
                currCalendar.get(Calendar.HOUR_OF_DAY),
                currCalendar.get(Calendar.MINUTE),
                false
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void bookParking(View view) {
    }
}