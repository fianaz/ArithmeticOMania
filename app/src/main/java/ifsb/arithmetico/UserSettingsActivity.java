package ifsb.arithmetico;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;

import ifsb.arithmetico.R;


public class UserSettingsActivity extends PreferenceActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);

        boolean enabled = ((CheckBoxPreference) findPreference("soundeffects")).isChecked();
        findPreference("numerickeysound").setEnabled(enabled);
        findPreference("buttonsound").setEnabled(enabled);
        findPreference("autonextsound").setEnabled(enabled);
        findPreference("endsession").setEnabled(enabled);
        ((CheckBoxPreference) findPreference("soundeffects")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean enabled = ((CheckBoxPreference) preference).isChecked();
                findPreference("numerickeysound").setEnabled(enabled);
                findPreference("buttonsound").setEnabled(enabled);
                findPreference("autonextsound").setEnabled(enabled);
                findPreference("endsession").setEnabled(enabled);
                return true;
            }
        });

        findPreference("bgaudiofile").setEnabled(((CheckBoxPreference) findPreference("bgaudio")).isChecked());

        ((CheckBoxPreference) findPreference("bgaudio")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean enabled = ((CheckBoxPreference) preference).isChecked();
                findPreference("bgaudiofile").setEnabled(enabled);
                if (enabled && UserSettings.getBgMusic() == null) {
                    AlertDialogCreator.show(UserSettingsActivity.this, "BG Audio Setup", "Please select an audio file",
                            "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            selectAudioFile();
                            dialog.cancel();
                        }
                    }, null, null);
                }
                return true;
            }
        });

        (findPreference("bgaudiofile")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                selectAudioFile();
                return true;
            }
        });

        findPreference("autonexttime").setEnabled(((CheckBoxPreference) findPreference("autonext")).isChecked());

        ((CheckBoxPreference) findPreference("autonext")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                findPreference("autonexttime").setEnabled(((CheckBoxPreference) preference).isChecked());
                UserSettings.enableAutonext(((CheckBoxPreference) preference).isChecked());
                return true;
            }
        });

        findPreference("countdowntime").setEnabled(((CheckBoxPreference) findPreference("countdown")).isChecked());

        ((CheckBoxPreference) findPreference("countdown")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                findPreference("countdowntime").setEnabled(((CheckBoxPreference) preference).isChecked());
                return true;
            }
        });

    }

    public void selectAudioFile() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 1);
    }

    @Override
    public void onBackPressed() {
        UserSettings.update();
        finish();
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
                ((CheckBoxPreference) findPreference("bgaudio")).setChecked(false);
                findPreference("bgaudiofile").setEnabled(false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
