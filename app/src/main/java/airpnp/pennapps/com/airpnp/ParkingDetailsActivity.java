package airpnp.pennapps.com.airpnp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ParkingDetailsActivity extends AppCompatActivity {

    private JSONObject tempJSONObject;
    private JSONArray tempJSONArray;

    Button arrivalStartDateBtn;
    Button arrivalStartTimeBtn;
    Button arrivalEndDateBtn;
    Button arrivalEndTimeBtn;

    private String phone;
    private Button book;

    private double hourlyRate;

    public Calendar startDate;
    public Calendar endDate;

    private long hours;

    private String userCustomerId;
    private String ownerCustomerId;
    private String userAccountId;
    private String ownerAccountId;
    private String userEmail;
    private String ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);
        userEmail=getIntent().getStringExtra("user_email");
        ownerEmail=getIntent().getStringExtra("owner_email");
        getCustomerKey();
        getAccountId();
        getParkingDetails();

        // Setting initial calendar values
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        userEmail = getIntent().getStringExtra("user_email");

        SimpleDateFormat sdfDateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat sdfTimeFormatter = new SimpleDateFormat("h:mm a");

        book = (Button) findViewById(R.id.btn_book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookParking(v);
            }
        });
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

    public void getParkingDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://li367-204.members.linode.com/getparkingdetails?email=" + ownerEmail;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tempJSONArray = response.getJSONArray("data");
                    //Toast.makeText(ParkingDetailsActivity.this, "" + tempJSONArray.length(), Toast.LENGTH_LONG).show();
                    tempJSONObject = tempJSONArray.getJSONObject(0);
                    String ownerFirstName = tempJSONObject.getString("firstname");
                    String ownerLastName = tempJSONObject.getString("lastname");
                    String street = tempJSONObject.getString("street");
                    String city = tempJSONObject.getString("city");
                    String state = tempJSONObject.getString("state");
                    hourlyRate = Double.valueOf(tempJSONObject.getString("rate"));
                    phone = tempJSONObject.getString("phone");
                    TextView textView1 = (TextView) findViewById(R.id.tv_owner_name);
                    TextView textView2 = (TextView) findViewById(R.id.tv_phone);
                    TextView textView3 = (TextView) findViewById(R.id.tv_rate);
                    TextView textView4 = (TextView) findViewById(R.id.tv_rules);
                    TextView textView5 = (TextView) findViewById(R.id.tv_house_name);
                    TextView textView6 = (TextView) findViewById(R.id.tv_house_addr);
                    textView1.setText("Name: " + ownerFirstName + " " + ownerLastName);
                    textView2.setText("Contact: " + phone);
                    textView3.setText("Rate: $" + hourlyRate + " / hr");
                    textView4.setText("Remarks: No Minivans please");
                    textView5.setText(street);
                    textView6.setText(city);

                } catch (JSONException e) {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("rest.nexmo.com")
                .appendPath("sms")
                .appendPath("json")
                .appendQueryParameter("api_key", getString(R.string.nexmo_id))
                .appendQueryParameter("api_secret", getString(R.string.nexmo_secret))
                .appendQueryParameter("from", "12675097486")
                .appendQueryParameter("to", phone)
                .appendQueryParameter("text", "Hi! This is AirPnP notifying you that " + userEmail + " has booked your parking spot!");
        String url = builder.build().toString();
        Log.d("!!!", url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(postRequest);

        String url2 = "http://li367-204.members.linode.com/getlatlng?email=" + ownerEmail;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url2, (String)null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tempJSONArray = response.getJSONArray("result");
                    tempJSONObject=tempJSONArray.getJSONObject(0);
                    String lat=tempJSONObject.getString("lat");
                    String lng=tempJSONObject.getString("lng");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lng));
                    startActivity(intent);
                } catch (JSONException e) {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest1);


        String ownerEmail = getIntent().getStringExtra("owner_email");
        MyApplication.markerHashMap.get(ownerEmail).remove();
        MyApplication.markerHashMap.remove(ownerEmail);
        //String startDate=arrivalStartDateBtn.getText().toString();
        //String endDate=arrivalEndDateBtn.getText().toString();
        String startTime = arrivalStartTimeBtn.getText().toString();
        String endTime = arrivalEndTimeBtn.getText().toString();
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

        int startYear = startDate.get(Calendar.YEAR);
        int startMonth = startDate.get(Calendar.MONTH);
        int startDay = startDate.get(Calendar.DAY_OF_MONTH);

        int endYear = endDate.get(Calendar.YEAR);
        int endMonth = endDate.get(Calendar.MONTH);
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);

        try {
            Date date = parseFormat.parse(startTime);
            startTime = displayFormat.format(date);
            date = parseFormat.parse(endTime);
            endTime = displayFormat.format(date);
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            int startMinute = Integer.parseInt(startTime.split(":")[1]);
            int endHour = Integer.parseInt(endTime.split(":")[0]);
            int endMinute = Integer.parseInt(endTime.split(":")[1]);
            DateTime dateTime1 = new DateTime(startYear, startMonth, startDay, startHour, startMinute, 0);
            DateTime dateTime2 = new DateTime(endYear, endMonth, endDay, endHour, endMinute, 0);
            Interval interval = new Interval(dateTime1, dateTime2);
            Duration duration = interval.toDuration();
            hours=duration.getStandardHours();
            getCustomerKey();
            getAccountId();
            getAvailableBalance();
            long hours = duration.getStandardHours();
        } catch (Exception e) {
            Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void getCustomerKey() {
        RequestQueue queue1 = Volley.newRequestQueue(ParkingDetailsActivity.this);
        String url1 = "http://li367-204.members.linode.com/getcustomerkey?email=" + ownerEmail;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url1, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tempJSONArray = response.getJSONArray("result");
                    tempJSONObject = tempJSONArray.getJSONObject(0);
                    ownerCustomerId = tempJSONObject.getString("customerkey");
                } catch (JSONException e) {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue1.add(jsonObjectRequest1);

        RequestQueue queue2 = Volley.newRequestQueue(ParkingDetailsActivity.this);
        String url2 = "http://li367-204.members.linode.com/getcustomerkey?email=" + userEmail;
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url2, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tempJSONArray = response.getJSONArray("result");
                    tempJSONObject = tempJSONArray.getJSONObject(0);
                    userCustomerId = tempJSONObject.getString("customerkey");
                } catch (JSONException e) {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue2.add(jsonObjectRequest2);

        NessieClient nessieClient=NessieClient.getInstance("1f925e3612560ecb9d6fca3348f05ae8");

    }

    public void getAccountId()
    {
        RequestQueue queue3 = Volley.newRequestQueue(ParkingDetailsActivity.this);
        String url3 = "http://li367-204.members.linode.com/getaccountid?email=" + userEmail;
        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, url3, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tempJSONArray = response.getJSONArray("result");
                    tempJSONObject = tempJSONArray.getJSONObject(0);
                    userAccountId = tempJSONObject.getString("accountid");
                } catch (JSONException e) {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue3.add(jsonObjectRequest3);

        RequestQueue queue4 = Volley.newRequestQueue(ParkingDetailsActivity.this);
        String url4 = "http://li367-204.members.linode.com/getaccountid?email=" + ownerEmail;
        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(Request.Method.GET, url4, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tempJSONArray = response.getJSONArray("result");
                    tempJSONObject = tempJSONArray.getJSONObject(0);
                    ownerAccountId = tempJSONObject.getString("accountid");
                } catch (JSONException e) {
                    Toast.makeText(ParkingDetailsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ParkingDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue4.add(jsonObjectRequest4);
    }

    public void getAvailableBalance()
    {
        NessieClient nessieClient=NessieClient.getInstance("1f925e3612560ecb9d6fca3348f05ae8");
        nessieClient.ACCOUNT.getAccount(userAccountId, new NessieResultsListener() {
            @Override
            public void onSuccess(Object result) {
                Account account = (Account)result;
                String accountBalance="" + account.getBalance();
                int a=(int)hours;
                Double b=new Double(hourlyRate);
                int x=b.intValue();
                int cost=a*x;
                Intent intent=new Intent(ParkingDetailsActivity.this, PaymentActivity.class);
                intent.putExtra("cost", a);
                intent.putExtra("ownerEmail", ownerEmail);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("ownerAccountId", ownerAccountId);
                intent.putExtra("userAccountId", userAccountId);
                intent.putExtra("cost", cost);
                intent.putExtra("hours", (int)hours);
                startActivity(intent);
            }

            @Override
            public void onFailure(NessieError error) {
                Toast.makeText(ParkingDetailsActivity.this, "onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }
}