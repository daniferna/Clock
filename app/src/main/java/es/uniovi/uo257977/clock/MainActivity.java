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
import android.view.MenuItem;

import com.db.chart.model.BarSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import es.uniovi.uo257977.clock.logic.Alarm;
import es.uniovi.uo257977.clock.logic.AlarmRecyclerAdapter;
import es.uniovi.uo257977.clock.logic.SpotifyConnection;
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



    private boolean noDialogUbicacion = false;

    //Localizacion
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        SpotifyConnection.getSpotifyConnection(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);

        comprobacionDePermisos();
        obtencionDeUbicacion();

        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);

        cambiarListenerAlarms();

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
                        cambiarListenerAlarms();
                        break;
                    case 1: //Stopwatch
                        changeFromAddToPlayAnimate();
                        needAnimate = true;
                        mostrarInformacionPlay();
                        cambiarListenerStopwatch();
                        break;
                    case 2: //Timer
                        needAnimate = false;
                        cambiarListenerTimer();
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



    private void cambiarListenerTimer() {
        TimerFragment fragment = (TimerFragment)getSupportFragmentManager().getFragments().get(3);
        fragment.setOnClickFabPlay(fab);
    }

    private void cambiarListenerStopwatch() {
        StopwatchFragment fragment = (StopwatchFragment)getSupportFragmentManager().getFragments().get(2);
        fragment.setOnClickFabPlay(fab);
    }

    private void cambiarListenerAlarms() {
        fab.setOnClickListener(v -> startActivityForResult(new Intent(this, AddAlarmActivity.class), ALARM_REQUEST));
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
              //  Snackbar.make(viewAlarmas, "Alarma creada correctamente", Snackbar.LENGTH_SHORT).show();
            }
        }
    }



}
