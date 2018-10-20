package es.uniovi.uo257977.clock;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;
import es.uniovi.uo257977.clock.Fragments.AlarmsFragment;
import es.uniovi.uo257977.clock.Fragments.StopwatchFragment;
import es.uniovi.uo257977.clock.Fragments.TimerFragment;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    Context context;
    boolean needAnimate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);

        bottomAppBar.replaceMenu(R.menu.navigation);

        context = this;

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

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_scoreboard:
                        //TODO Implementar Activity de la puntuacion
                        break;
                }
                return true;
            }
        });

    }

    private void changeFromAddToPlayAnimate() {
        AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.from_add_to_play);
        fab.setImageDrawable(drawable);
        if (needAnimate)
            drawable.start();
    }

    private void changeFromPlayToAddAnimate() {
        AnimatedVectorDrawableCompat drawable = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.from_play_to_add);
        fab.setImageDrawable(drawable);
        drawable.start();
    }
}
