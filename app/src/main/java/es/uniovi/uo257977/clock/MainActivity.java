package es.uniovi.uo257977.clock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /*
    Barra de navegacion de la libreria externa: AHBottomNavigation (Más opciones que la original de Android)
    https://github.com/aurelhubert/ahbottomnavigation
    */
    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AHBottomNavigationItem alarms = new AHBottomNavigationItem(getString(R.string.menu_alarm), R.drawable.ic_alarm_black_24dp);
        AHBottomNavigationItem timer = new AHBottomNavigationItem(getString(R.string.menu_timer), R.drawable.ic_hourglass_empty_black_24dp);
        AHBottomNavigationItem stopwatch = new AHBottomNavigationItem(getString(R.string.menu_stopwatch), R.drawable.ic_timer_black_24dp);
        AHBottomNavigationItem scoreboard = new AHBottomNavigationItem(getString(R.string.menu_scoreboard), R.drawable.ic_sort_black_24dp);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.addItems(Arrays.asList(alarms, timer, stopwatch, scoreboard));
    }

}
