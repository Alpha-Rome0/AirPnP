package airpnp.pennapps.com.airpnp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OwnerActivity extends AppCompatActivity {

    TextView ownersText;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        listview=(ListView)findViewById(R.id.reviews);
        ownersText = (TextView) findViewById(R.id.owner_text);
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ffs.ttf");
        ownersText.setTypeface(tf);

        ArrayList<String> reviews=new ArrayList();
        reviews.add("User1 - Review: 5 Stars");
        reviews.add("User2 - Review: 4 Stars");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, reviews);



        // Bind to our new adapter.
        listview.setAdapter(adapter);
    }
}
