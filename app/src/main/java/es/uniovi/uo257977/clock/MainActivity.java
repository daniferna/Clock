package es.uniovi.uo257977.clock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.db.chart.model.BarSet;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.Fragments.AlarmsFragment;
import es.uniovi.uo257977.clock.Fragments.StopwatchFragment;
import es.uniovi.uo257977.clock.Fragments.TimerFragment;
import es.uniovi.uo257977.clock.Logic.Alarm;
import es.uniovi.uo257977.clock.Logic.AlarmRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    Context context;
    boolean needAnimate = true;
    static final int ALARM_REQUEST = 1;
    private ComplexPreferences complexPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);

        bottomAppBar.replaceMenu(R.menu.navigation);

        context = this;

        complexPreferences = ComplexPreferences.getComplexPreferences(context, "AlarmAppPreferences", MODE_PRIVATE);

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


}
