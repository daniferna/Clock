package es.uniovi.uo257977.clock;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.db.chart.model.BarSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.Fragments.AlarmsFragment;
import es.uniovi.uo257977.clock.Fragments.StopwatchFragment;
import es.uniovi.uo257977.clock.Fragments.TimerFragment;
import es.uniovi.uo257977.clock.Logic.Alarm;
import es.uniovi.uo257977.clock.Logic.AlarmRecyclerAdapter;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;

//Dependencias de spotify

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREF_KEY = "clock_sdm";
    public static final int REQUEST_PERMISSION_LOCATION_COARSE = 1500;

    private static final String IS_INFO_ADD_ALARM_REQUIRED = "info_tap_target_add_alarm";
    private static final String IS_INFO_CRONO_REQUIRED = "info_tap_target_crono_play_pause";

    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    Context context;
    boolean needAnimate = true;
    static final int ALARM_REQUEST = 1;
    private SharedPreferences sharedPreferences;
    private ComplexPreferences complexPreferences;

    //credenciales spotify
    private static final String CLIENT_ID = "f354fa8aa2dc4a549b0c211d355e6486";
    private static final String REDIRECT_URI = "testschema://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    public static final String TAG = "MyTag";
    RequestQueue mRequestQueue;  // Assume this exists.
    private boolean noDialogUbicacion = false;

    //Localizacion
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);

        comprobacionDePermisos();
        obtencionDeUbicacion();

        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);

        bottomAppBar.replaceMenu(R.menu.navigation);

        complexPreferences = ComplexPreferences.getComplexPreferences(context, "AlarmAppPreferences", MODE_PRIVATE);

        bottomAppBar.setNavigationOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            private static final int NUM_FRAGMENTS = 3;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        // Fragment de la alarma
                        return Fragment.instantiate(context, AlarmsFragment.class.getName());
                    case 1:
                        // Fragment del cronometro
                        return Fragment.instantiate(context, StopwatchFragment.class.getName());
                    case 2:
                        // Fragment del temporizador
                        return Fragment.instantiate(context, TimerFragment.class.getName());
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return NUM_FRAGMENTS;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: // Alarms
                        changeFromPlayToAddAnimate();
                        break;
                    case 1: //Stopwatch
                        changeFromAddToPlayAnimate();
                        needAnimate = true;
                        mostrarInformacionPlay();
                        break;
                    case 2: //Timer
                        needAnimate = false;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (complexPreferences.getObject("Puntuacion", BarSet.class) == null) { // Primer inicio app
            complexPreferences.putObject("Puntuacion", new BarSet());
            complexPreferences.commit();
        }

        viewPager.setOffscreenPageLimit(3); //Esta linea mantiene en memoria todos los fragments

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        mostrarInformacionAñadirAlarma();
    }

    private void mostrarInformacionAñadirAlarma() {
        //Material Tap Target
        if (sharedPreferences.getBoolean(IS_INFO_ADD_ALARM_REQUIRED, true))
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.fab)
                    .setPrimaryText("Añadir alarma")
                    .setSecondaryText("Pulsa aqui si quieres añadir una alarma")
                    .setPromptStateChangeListener((prompt, state) -> {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            sharedPreferences.edit().putBoolean(IS_INFO_ADD_ALARM_REQUIRED, false).apply();
                            comprobacionDePermisos();
                        }
                    })
                    .setBackButtonDismissEnabled(true)
                    .setPromptBackground(new RectanglePromptBackground())
                    .show();
    }

    private void mostrarInformacionPlay() {
        //Material Tap Target
        if (sharedPreferences.getBoolean(IS_INFO_CRONO_REQUIRED, true))
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.fab)
                    .setPrimaryText("Comenzar/Pausar")
                    .setSecondaryText("Pulsa aqui si quieres encender/pausar el cronometro")
                    .setPromptStateChangeListener((prompt, state) -> {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED)
                            sharedPreferences.edit().putBoolean(IS_INFO_CRONO_REQUIRED, false).apply();
                    })
                    .setBackButtonDismissEnabled(true)
                    .setPromptBackground(new RectanglePromptBackground())
                    .show();
    }

    private void obtencionDeUbicacion() {
        //TODO
    }

    protected void comprobacionDePermisos() {
        //Permiso de ubicacion
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Activity activity = this;
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("El permiso de ubicacion es neceario para poder mostrar la informacion del tiempo al apagar las alarmas");
                alertBuilder.setPositiveButton("Conceder", (dialog, which) ->
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION_COARSE));
                alertBuilder.setNegativeButton("No conceder", (dialog, which) -> dialog.dismiss());
                alertBuilder.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_PERMISSION_LOCATION_COARSE);
            }
        }

        //Permiso de modificacion de ajustes del sistema
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Necesario permiso para poder modificar tono", Snackbar.LENGTH_LONG);
                snackbar.setAction("Conceder", v -> {
                    startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));
                });
                snackbar.show();
            }
        }
    }

    private void changeFromAddToPlayAnimate() {
        if (needAnimate) {
            AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.from_add_to_play);
            fab.setImageDrawable(drawable);
            drawable.start();
        }
    }

    private void changeFromPlayToAddAnimate() {
        AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.from_play_to_add);
        fab.setImageDrawable(drawable);
        drawable.start();
    }

    public void clickFab(View view) {
        startActivityForResult(new Intent(this, AddAlarmActivity.class), ALARM_REQUEST);
    }

    public void scoreboardClick(MenuItem item) {
        startActivity(new Intent(this, ScoreboardActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ALARM_REQUEST) {
            if (resultCode == RESULT_OK) {
                RecyclerView viewAlarmas = findViewById(R.id.recycler_alarms);
                ArrayList<Alarm> alarmasExtra = data.getExtras().getParcelableArrayList("Alarmas");
                ((AlarmRecyclerAdapter) viewAlarmas.getAdapter()).updateAlarms(alarmasExtra);
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
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
                        getPlayList();
                        // Now you can start interacting with App Remote
                   //     connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity2.0", throwable.getMessage(), throwable);

                    }
                });


    }


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
                Log.d("vamoh a veh3", error.getMessage()+ " "+ error.getCause());
            }
            }
             );

        queue.add(stringRequest);

// Set the tag on the request.
        stringRequest.setTag(TAG);

// Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
    private void connected() {

        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
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
}
