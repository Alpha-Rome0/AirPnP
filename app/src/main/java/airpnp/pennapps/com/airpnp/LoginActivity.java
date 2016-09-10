package airpnp.pennapps.com.airpnp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView textView;

    //    Facebook Login
    private LoginButton btnFBLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Facebook

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        textView = (AutoCompleteTextView) findViewById(R.id.email);
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        btnFBLogin = (LoginButton) findViewById(R.id.fb_login_button);
        btnFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("SUCCESS", "YEP SUCCESS");
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile;
                ProfileTracker mProfileTracker;
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProf, Profile newProf) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", newProf.getName());
                            Profile.setCurrentProfile(newProf);
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getName());
                }
                // TODO GET FB DATA FOR NEXT ACTIVITY HERE!!
//                System.out.println("FB: "+ " --- "+loginResult.getAccessToken().getUserId() + " Token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                System.out.println("WTF: Canceled");
            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("WTF: Error");
            }
        });

        // Init components

        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ffs.ttf");
        final TextView tv = (TextView) findViewById(R.id.welcome_text);
        tv.setTypeface(tf);

        final int color1 = 160;
        final int color2 = 189;
        final int color3 = 255;
        final int color4 = 224;
        final TextView linkSignup = (TextView) findViewById(R.id.signup_link);
        //Change the text color when the user touches it
        linkSignup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        linkSignup.setTextColor(Color.argb(color1, color2, color2, color2));
                        break;
                    case MotionEvent.ACTION_UP:
                        linkSignup.setTextColor(Color.argb(color3, color4, color4, color4));
                        break;
                }
                return false;
            }
        });
        linkSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }

        });

    }

    public void loginAction(View view) {
        Intent intent;
        if (textView.getText().toString().equals("owner")) {
            intent = new Intent(getApplicationContext(), OwnerActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MapsActivity.class);
        }
        startActivity(intent);
    }
}
