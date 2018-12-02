package es.uniovi.uo257977.clock;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddAlarmActivity extends AppCompatActivity {

    private final int TONE_PICKER = 001;

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        timePicker = findViewById(R.id.timePicker_addAlarm);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            //Arregla un bug de algunas versiones de Android que no ponen bien la hora en formato 24H
            timePicker.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

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
