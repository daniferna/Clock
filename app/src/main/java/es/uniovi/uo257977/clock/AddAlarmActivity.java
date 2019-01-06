package es.uniovi.uo257977.clock;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.Logic.Alarm;
import es.uniovi.uo257977.clock.Logic.ListAlarms;

public class AddAlarmActivity extends AppCompatActivity {

    private final int TONE_PICKER = 001;

    private TimePicker timePicker;
    FloatingActionButton myFab;


    //credenciales spotify
    private static final String CLIENT_ID = "f354fa8aa2dc4a549b0c211d355e6486";
    private static final String REDIRECT_URI = "testschema://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String OAuthToken ="BQCGacI2HG_5I4qD-ghVrJc397p7eD1stfAk96ACikOi8xm__w_Tkd85EbaMrQHUORORxlJpvfmY3jRtNfU2XdFsOCnZAU5QrhDrFRyyMzKt7So_eNcRIGvxDXwGlDBxMGPAiNqR92xVkg";
    ArrayList<String> SpotifyPlaylist;

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
        crearPlayList();

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
    public void seleccionarTonoSpotifyAdd(View view) {
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
        if (requestCode == TONE_PICKER && resultCode == RESULT_OK){
            Uri tonoSeleccionado = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (tonoSeleccionado == null) return;
            RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, tonoSeleccionado);
        }
    }

    public void añadirAlarma(View view) {
        Alarm alarma = new Alarm();


        TimePicker time = findViewById(R.id.timePicker_timer);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cal.set(Calendar.HOUR_OF_DAY, time.getHour());
            cal.set(Calendar.MINUTE, time.getMinute());
        } else {
            cal.add(Calendar.HOUR_OF_DAY, time.getCurrentHour());
            cal.add(Calendar.MINUTE, time.getCurrentMinute());
        }
        alarma.setFecha_alarma(cal.getTime());
        
        EditText textoAlarma = findViewById(R.id.nombreAlarmaTxt);
        alarma.setNombre(textoAlarma.getText().toString());


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

        Alarm.DIAS_ALARMA[] dias =new Alarm.DIAS_ALARMA[diasAlarma.size()];
        for(int i=0;i<diasAlarma.size();i++){
            dias[i]=diasAlarma.get(i);
        }
        alarma.setDiasAlarma(dias);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SpotifyConnection();

    }


    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void crearPlayList() {

        SpotifyPlaylist= new ArrayList<String>();
        SpotifyPlaylist.add("spotify:playlist:37i9dQZEVXbNFJfN1Vw8d9");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DWZoF06RIo9el");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DWUa8ZRTfalHk");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DX8f6LHxMjnzD");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZEVXbLiRSasKsNU9");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZEVXbMDoHDwVN2tF");
        SpotifyPlaylist.add("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

    }
    private void SpotifyConnection(){
        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity2", "Connected!!");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity2.0", throwable.getMessage(), throwable);

                    }
                });

    }

    public void activarSpotifyForAlarm(){

    }
    public void spotifyAlarm(View view) {

        // Play a playlist
        int playListForToday= (int)(Math.random() *8);
        Log.d("listaaa", " by " + playListForToday);
        mSpotifyAppRemote.getPlayerApi().play(SpotifyPlaylist.get(playListForToday));
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }
    private void getClientConection(){
        String url = " https://accounts.spotify.com/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"callback&scope=user-read-private%20user-read-email&response_type=token&state=123";

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("vamoh a vehjeje","Response is: ");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Display the first 500 characters of the response string.

                        Log.d("vamoh a veh222","Response is:"+ response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Log.d("vamoh a veh3", error+ " "+ error.getCause());
            }
        }
        );

        queue.add(stringRequest);


    }
/*
    private void getPlayList(){

        String url = "https://api.spotify.com/v1/playlists/59ZbFPES4DQwEjBpWHzrtC";



        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("vamoh a veh","Response is: ");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    // Display the first 500 characters of the response string.

                    Log.d("vamoh a veh222","Response is: 2 ");

                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Log.d("vamoh a veh3", error+ " "+ error.getCause());
            }
            }
             );

        queue.add(stringRequest);
    }*/





}
