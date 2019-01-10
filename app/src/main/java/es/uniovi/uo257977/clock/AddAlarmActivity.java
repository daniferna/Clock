package es.uniovi.uo257977.clock;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.logic.Alarm;
import es.uniovi.uo257977.clock.logic.ListAlarms;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;

public class AddAlarmActivity extends AppCompatActivity {

    private final int TONE_PICKER = 001;

    private TimePicker timePicker;
    FloatingActionButton myFab;

    private Alarm alarma = new Alarm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        timePicker = findViewById(R.id.timePicker_timer);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            //Arregla un bug de algunas versiones de Android que no ponen bien la hora en formato 24H
            timePicker.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        myFab = findViewById(R.id.fab_addAlarm);
        myFab.setOnClickListener(this::añadirAlarma);

        checkearPermisos();
    //    mostrarInformacionSpotify();

    }

    private void checkearPermisos() {
        MaterialButton buttonTone = findViewById(R.id.btn_seleccionarTonoAdd);
        ImageButton buttonSpotify = findViewById(R.id.imgButtonToneSpotify);
        TextView txtAlertaPermisos = findViewById(R.id.txtAlertaPermisosAdd);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (!Settings.System.canWrite(this)) {
                buttonTone.setEnabled(false);
                buttonSpotify.setEnabled(false);
                txtAlertaPermisos.setVisibility(View.VISIBLE);
            }
    }

    public void seleccionarTonoAdd(View view) {
        Intent intentTono = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccionar tono");
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        startActivityForResult(intentTono, TONE_PICKER);
    }
 /*   public void seleccionarTonoSpotifyAdd(View view) {
        Intent intentTono = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        Uri tonoPorDefecto = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccionar tono");
        intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, tonoPorDefecto);
        startActivityForResult(intentTono, TONE_PICKER);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TONE_PICKER && resultCode == RESULT_OK) {
            Uri tonoSeleccionado = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (tonoSeleccionado == null)
                return;
           alarma.setSonido(tonoSeleccionado.toString());
        }
    }

    public void añadirAlarma(View view) {
        TimePicker time = findViewById(R.id.timePicker_timer);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cal.set(Calendar.HOUR_OF_DAY, time.getHour());
            cal.set(Calendar.MINUTE, time.getMinute());
            cal.set(Calendar.SECOND,0);
        } else {
            cal.add(Calendar.HOUR_OF_DAY, time.getCurrentHour());
            cal.add(Calendar.MINUTE, time.getCurrentMinute());
            cal.set(Calendar.SECOND,0);
        }
        alarma.setFecha_alarma(cal.getTime());

        EditText textoAlarma = findViewById(R.id.nombreAlarmaTxt);
        alarma.setNombre(textoAlarma.getText().toString());

        CheckBox spotifyRbt = findViewById(R.id.spotifyRbt);
        if (spotifyRbt.isChecked()) {
            alarma.setSpotify(true);
        } else {
            alarma.setSpotify(false);
        }

        ChipGroup botonesDias = findViewById(R.id.chipGroup_addAlarm);
        checkBotones(botonesDias, alarma);


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(this, "AlarmAppPreferences", MODE_PRIVATE);
        ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
        if (alarmasList != null) {
            alarmasList.alarmas.add(alarma);
        } else {
            alarmasList = new ListAlarms();
            ArrayList<Alarm> temp = new ArrayList<>();
            temp.add(alarma);
            alarmasList.setAlarms(temp);
        }


        complexPreferences.putObject("Alarmas", alarmasList);
        complexPreferences.commit();

        getIntent().putParcelableArrayListExtra("Alarmas", alarmasList.alarmas);
        setResult(RESULT_OK, getIntent());


        finish();

    }

    private void checkBotones(ChipGroup botonesDias, Alarm alarma) {
        ArrayList<Alarm.DIAS_ALARMA> diasAlarma = new ArrayList<>();
        Chip boton;
        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    boton = findViewById(R.id.chip_lunesAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.LUNES);
                    break;
                case 1:
                    boton = findViewById(R.id.chip_martesAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.MARTES);
                    break;
                case 2:
                    boton = findViewById(R.id.chip_miercolesAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.MIERCOLES);
                    break;
                case 3:
                    boton = findViewById(R.id.chip_juevesAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.JUEVES);
                    break;
                case 4:
                    boton = findViewById(R.id.chip_viernesAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.VIERNES);
                    break;
                case 5:
                    boton = findViewById(R.id.chip_sabadoAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.SABADO);
                    break;
                case 6:
                    boton = findViewById(R.id.chip_domingoAdd);
                    if (boton.isChecked())
                        diasAlarma.add(Alarm.DIAS_ALARMA.DOMINGO);
                    break;
            }

        }

        Alarm.DIAS_ALARMA[] dias = new Alarm.DIAS_ALARMA[diasAlarma.size()];
        for (int i = 0; i < diasAlarma.size(); i++) {
            dias[i] = diasAlarma.get(i);
        }
        alarma.setDiasAlarma(dias);
    }

    private void mostrarInformacionSpotify() {
        //Material Tap Target
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.spotifyRbt)
                    .setPrimaryText(getString(R.string.Material_info_primary_Spotify))
                    .setSecondaryText(getString(R.string.materuial_info_Spotify))
                    .setBackButtonDismissEnabled(true)
                    .setPromptBackground(new RectanglePromptBackground())
                    .show();
    }
}