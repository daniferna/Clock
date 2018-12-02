package es.uniovi.uo257977.clock;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddAlarmActivity extends AppCompatActivity {

    private final int TONE_PICKER = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
    }

    public void seleccionarTonoAdd(View view) {
        Intent intentTono = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        Uri tonoPorDefecto = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccionar tono");
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, tonoPorDefecto);
        startActivityForResult(intentTono, TONE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO
    }
}
