package airpnp.pennapps.com.airpnp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OwnerActivity extends AppCompatActivity {

    TextView ownersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        ownersText = (TextView) findViewById(R.id.owner_text);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ffs.ttf");
        ownersText.setTypeface(tf);
    }
}
