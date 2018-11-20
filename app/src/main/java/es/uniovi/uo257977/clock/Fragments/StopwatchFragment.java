package es.uniovi.uo257977.clock.Fragments;


import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import java.sql.Time;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.uniovi.uo257977.clock.R;

public class StopwatchFragment extends Fragment  {

    Chronometer chrono;
    Boolean isRunnig=false,isPaused=false;
    private long timeWhenStopped=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);



        chrono = view.findViewById(R.id.chrono);

        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chrono = chronometerChanged;
            }
        });


        Button playbt = (Button) view.findViewById(R.id.playbt);
        playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                startChrono();
            }
        });

        Button pausebt = (Button) view.findViewById(R.id.pausebt);
        pausebt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                pauseChrono();
            }
        });

        Button stopbt = (Button) view.findViewById(R.id.stopbt);
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

    public void startChrono(){
        Log.d("start", "here it goes");
        if(!isRunnig && !isPaused){
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
            isRunnig = true;
        }
        if(!isRunnig && isPaused){
            chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            timeWhenStopped=0;
            chrono.start();
            isRunnig = true;
            isPaused=false;
        }
    }

    public void stopChrono(){
        chrono.stop();
        chrono.setBase(SystemClock.elapsedRealtime());
        isRunnig = false;
        isPaused=false;

    }

    public void pauseChrono(){
        Log.d("heree", "hereeeee2");
        if(isRunnig){
            timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
            chrono.stop();
            isPaused=true;
            isRunnig = false;
        }
    }



}
