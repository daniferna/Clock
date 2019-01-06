package es.uniovi.uo257977.clock.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.uniovi.uo257977.clock.R;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class StopwatchFragment extends Fragment  {

    private Chronometer chrono;
    private Boolean isRunnig=false,isPaused=false;
    private long timeWhenStopped=0;
    private MaterialProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        chrono = view.findViewById(R.id.chrono);

        progressBar = view.findViewById(R.id.materialProgressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            progressBar.setProgress(0, true);
        else
            progressBar.setProgress(0);



        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chrono = chronometerChanged;
            }
        });



     /*   FloatingActionButton playbt = view.findViewById(R.id.fab);
        playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                startChrono();
            }
        });
*/
        FloatingActionButton pausebt = view.findViewById(R.id.pausebt);
        pausebt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                pauseChrono();
            }
        });

        FloatingActionButton stopbt = view.findViewById(R.id.stopbt);
        stopbt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                stopChrono();
            }
        });



        return view;

    }

    private void startChrono(){
        if(!isRunnig && !isPaused){
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            float tiempoIzquierda = 0/1000;
            float tiempoDerecha = 60 / 1000;
            int progreso = (int) ((tiempoIzquierda / tiempoDerecha)*100);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                progressBar.setProgress(100-progreso, true);
            else
                progressBar.setProgress(100-progreso);
            isRunnig = true;
        }
        if(!isRunnig && isPaused){
            chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
         /* float tiempoIzquierda = ((Math.abs(chrono.getBase())/1000)%60);
            float tiempoDerecha = 60 / 1000;
            int progreso = (int) ((tiempoIzquierda / tiempoDerecha)*100);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                progressBar.setProgress(100-progreso, true);
            else
                progressBar.setProgress(100-progreso);*/
            timeWhenStopped=0;
            chrono.start();
            isRunnig = true;
            isPaused=false;
        }
    }

    private void stopChrono(){
        chrono.stop();
        chrono.setBase(SystemClock.elapsedRealtime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            progressBar.setProgress(0, true);
        else
            progressBar.setProgress(0);
        isRunnig = false;
        isPaused=false;

    }

    private void pauseChrono(){
        if(isRunnig){
            timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
            chrono.stop();
            isPaused=true;
            isRunnig = false;
        }
    }

    public void setOnClickFabPlay( FloatingActionButton fab ) {
        fab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // do something
                startChrono();
            }
        });
    }


}
