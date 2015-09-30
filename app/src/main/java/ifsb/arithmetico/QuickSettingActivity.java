package ifsb.arithmetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import ifsb.arithmetico.R;

public class QuickSettingActivity extends AppCompatActivity {
    private MenuItem doneMenuItem;
    private ListView lv_quicksettings;
    private int selected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_quicksettings = (ListView) findViewById(R.id.lv_quicksettings);

        ArrayList<String> names = QuickSetting.getNames();
        final QuickSettingArrayAdapter quickSettingArrayAdapter;
        lv_quicksettings.setAdapter(quickSettingArrayAdapter = new QuickSettingArrayAdapter(this, R.layout.quicksetting_listview_item, names));
        lv_quicksettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                quickSettingArrayAdapter.setSelected(view, i);
            }
        });
    }

    public void enableDone() {
        doneMenuItem.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quick_setting, menu);
        (doneMenuItem = menu.findItem(R.id.action_done)).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            String selected = ((QuickSettingArrayAdapter) lv_quicksettings.getAdapter()).getSelected();
            if (selected != null) {
                QuickSetting.activate(selected);
                if (UserSettings.bgMusicEnabled() && UserSettings.getBgMusic() == null) {
                    AlertDialogCreator.show(QuickSettingActivity.this, "BG Audio Setup", "Please select an audio file",
                            "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    selectAudioFile();
                                    //finish();
                                }
                            }, null, null);
                    return true;
                }
            }
            finish();
            return true;
        }
        if (id == R.id.action_cancel) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectAudioFile() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK) {
                UserSettings.setBgMusic(data.getData());
                if (BackgroundMusicPlayer.getPlayer() != null)
                    BackgroundMusicPlayer.releasePlayer();
                BackgroundMusicPlayer.createPlayer(this, UserSettings.getBgMusic());
            }
            else if (resultCode == RESULT_CANCELED) {
                UserSettings.enableBgMusic(false);
                UserSettings.setBgMusic("");
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
