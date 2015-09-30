package ifsb.arithmetico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ifsb.arithmetico.R;

public class SessionReportActivity extends AppCompatActivity {
    private Button btn_dismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_report);
        Intent intent = getIntent();
        int numQuestions = intent.getIntExtra("numquestions", -1);
        int numAttempted = intent.getIntExtra("numattempted", -1);
        int numPerfect = intent.getIntExtra("numperfect", -1);
        int numSkipped = intent.getIntExtra("skipped", -1);
        float totalTime = intent.getFloatExtra("totaltime", -1f);
        ((TextView) findViewById(R.id.tv_numquestions)).setText(""+numQuestions);
        ((TextView) findViewById(R.id.tv_numattempted)).setText(""+numAttempted);
        ((TextView) findViewById(R.id.tv_numperfect)).setText(""+numPerfect);
        ((TextView) findViewById(R.id.tv_numskipped)).setText(""+numSkipped);
        ((TextView) findViewById(R.id.tv_totaltime)).setText(format(totalTime));
        ((TextView) findViewById(R.id.tv_avgtime)).setText((numAttempted == 0) ? "N/A" : format(totalTime/numAttempted));
        ((Button) findViewById(R.id.btn_dismiss)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String format(double number) {
        return String.format("%.2f", number);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
