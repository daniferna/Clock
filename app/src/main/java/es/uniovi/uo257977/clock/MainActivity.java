package es.uniovi.uo257977.clock;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bar);

        bottomAppBar.replaceMenu(R.menu.navigation);

        context = this;

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            private static final int NUM_FRAGMENTS = 4;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        // Fragment de la alarma
                        return Fragment.instantiate(context, AlarmsFragment.class.getName());
                    case 1:
                        //TODO Fragment del cronometro
                        return Fragment.instantiate(context, AlarmsFragment.class.getName());
                    case 2:
                        //TODO Fragment del temporizador
                        return Fragment.instantiate(context, AlarmsFragment.class.getName());
                    case 3:
                        //TODO Fragment de la puntacion
                        return Fragment.instantiate(context, AlarmsFragment.class.getName());
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return NUM_FRAGMENTS;
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_alarms:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.navigation_stopwatch:
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.navigation_timer:
                        viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.navigation_scoreboard:
                        viewPager.setCurrentItem(3, true);
                        break;
                }
                return true;
            }
        });

    }
}
