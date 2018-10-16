package es.uniovi.uo257977.clock;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements AlarmsFragment.OnFragmentInteractionListener {

    Fragment fragmentTest;
    BottomAppBar bottomAppBar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bar);
        fragmentTest = getSupportFragmentManager().findFragmentById(R.id.fragment);

        bottomAppBar.replaceMenu(R.menu.navigation);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO No se muy bien que va aqui
    }
}
