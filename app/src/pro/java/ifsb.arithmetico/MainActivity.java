package ifsb.arithmetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import ifsb.arithmetico.BugReporter;
import ifsb.arithmetico.R;


public class MainActivity extends ActionBarActivity {
    private ImageView btn_add;
    private ImageView btn_subtract;
    private ImageView btn_combo;
    private Button btn_multiplication;
    private Button btn_division;
    private ImageView iv_front;
    private MenuItem item_music, item_autonext, item_mute, item_bugreport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UserSettings.init(this);
        btn_add = (ImageView) findViewById(R.id.btn_addition);
        btn_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_add.setImageDrawable(getResources().getDrawable(R.drawable.addbtndown));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_add.setImageDrawable(getResources().getDrawable(R.drawable.addbtnup));
                        SoundPlayer.play(MainActivity.this, R.raw.buttonpress);
                        SoundPlayer.vibrate();
                        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                        intent.putExtra("numquestions", UserSettings.getNumQuestions());
                        intent.putExtra("generator", "ifsb.arithmetico.core.AdditionQuestionGenerator");
                        intent.putExtra("fragment", "ifsb.arithmetico.AdditionPanelFragment");
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        btn_subtract = (ImageView) findViewById(R.id.btn_subtraction);
        btn_subtract.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_subtract.setImageDrawable(getResources().getDrawable(R.drawable.subtbtndown));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_subtract.setImageDrawable(getResources().getDrawable(R.drawable.subtbtnup));
                        SoundPlayer.play(MainActivity.this, R.raw.buttonpress);
                        SoundPlayer.vibrate();
                        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                        intent.putExtra("numquestions", UserSettings.getNumQuestions());
                        intent.putExtra("generator", "ifsb.arithmetico.core.SubtractionQuestionGenerator");
                        intent.putExtra("fragment", "ifsb.arithmetico.SubtractionPanelFragment");
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        btn_combo = (ImageView) findViewById(R.id.btn_combo);
        btn_combo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_combo.setImageDrawable(getResources().getDrawable(R.drawable.combobtndown));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_combo.setImageDrawable(getResources().getDrawable(R.drawable.combobtnup));
                        SoundPlayer.play(MainActivity.this, R.raw.buttonpress);
                        SoundPlayer.vibrate();
                        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                        intent.putExtra("numquestions", UserSettings.getNumQuestions());
                        intent.putExtra("generator", "ifsb.arithmetico.core.ComboQuestionGenerator");
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        QuickSetting.init();
        Hints.init(getApplicationContext());
        SoundPlayer.init(this);
        if (UserSettings.bgMusicEnabled())
            BackgroundMusicPlayer.createPlayer(this, UserSettings.getBgMusic());
    }

    @Override
    public void onResume() {
        SharedPreferences preferences = getSharedPreferences("ifsb.arithmetico", MODE_PRIVATE);
        if (preferences.getBoolean("firstrun", true)) {
            PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
            preferences.edit().putBoolean("firstrun", false).commit();
            SoundPlayer.play(this, R.raw.right);
            AlertDialogCreator.show(MainActivity.this, "Welcome to\nArithmetic-O-Mania!", "Select a quick setting before proceeding...",
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            startActivity(new Intent(MainActivity.this, QuickSettingActivity.class));
                        }
                    }, null, null);
        }
        refreshToolbar();
        super.onResume();
     }

    @Override
    public void onBackPressed() {
        BackgroundMusicPlayer.stopPlayer();
        BackgroundMusicPlayer.releasePlayer();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item_music = menu.findItem(R.id.action_music);
        item_autonext = menu.findItem(R.id.action_autonext);
        item_mute = menu.findItem(R.id.action_mute);
        item_bugreport = menu.findItem(R.id.action_bugreport);
        refreshToolbar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quicksetting) {
            startActivity(new Intent(this, QuickSettingActivity.class));
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, UserSettingsActivity.class));
            return true;
        }
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        if (id == R.id.action_music) {
            if (!UserSettings.bgMusicEnabled() && UserSettings.getBgMusic() == null) {
                AlertDialogCreator.show(MainActivity.this, "BG Audio Disabled", "Please enable BG audio in Settings",
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                startActivity(new Intent(MainActivity.this, UserSettingsActivity.class));
                                dialog.cancel();
                            }
                        }, null, null);
            }
            else {
                UserSettings.enableBgMusic(!UserSettings.bgMusicEnabled());
                BackgroundMusicPlayer.update(getApplicationContext());
                refreshToolbar();
            }
        }
        if (id == R.id.action_mute) {
            UserSettings.setSoundEffects(!UserSettings.soundEffectsEnabled());
            refreshToolbar();
        }
        if (id == R.id.action_autonext) {
            UserSettings.enableAutonext(!UserSettings.autoNextEnabled());
            refreshToolbar();
        }
        if (id == R.id.action_bugreport)
            BugReporter.generate(this);

        return super.onOptionsItemSelected(item);
    }

    private void refreshToolbar() {
        if (item_music != null) {
            item_music.setIcon(getResources().getDrawable((UserSettings.bgMusicEnabled()) ? R.drawable.music_on : R.drawable.music_off));
            item_music.setTitle((UserSettings.bgMusicEnabled()) ? "Disable BG Audio" : "Enable BG Audio");
        }
        if (item_autonext != null) {
            item_autonext.setIcon(getResources().getDrawable((UserSettings.autoNextEnabled()) ? R.drawable.autonext_on : R.drawable.autonext_off));
            item_autonext.setTitle((UserSettings.autoNextEnabled()) ? "Disable Autonext" : "Enable Autonext");
        }
        if (item_mute != null) {
            item_mute.setIcon(getResources().getDrawable((UserSettings.soundEffectsEnabled()) ? R.drawable.ic_muted_off : R.drawable.ic_muted_on));
            item_mute.setTitle((UserSettings.soundEffectsEnabled()) ? "Disable Sound Effects" : "Enable Sound Effects");
        }
    }
}
