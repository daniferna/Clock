package es.uniovi.uo257977.clock.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.uniovi.uo257977.clock.R;

public class TimerFragment extends Fragment {


    protected View view;
    private Boolean isRunnig=false,isPaused=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_timer, container, false);

        Button playbt = view.findViewById(R.id.playbt);

        playbt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                    isRunnig = true;
                    isPaused = false;

                    new CountDownTimer(getTimeLeft(), 1000) {

                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {


                            if (isRunnig || isPaused) {
                                if (!isRunnig && isPaused) {
                                    cancel();
                                }
                                int seconds2 = (int) (millisUntilFinished / 1000);
                                int minutes2 = seconds2 / 60;
                                seconds2 = seconds2 % 60;

                                ((EditText) view.findViewById(R.id.seconds)).setText(Long.toString(seconds2));
                                ((EditText) view.findViewById(R.id.minutes)).setText(Long.toString(minutes2));
                            }
                        }
                        public void onFinish() {
                            ((EditText) view.findViewById(R.id.seconds)).setText(R.string.cero);
                            ((EditText) view.findViewById(R.id.seconds)).setText(R.string.cero);
                            isRunnig = false;
                            isPaused = false;
                        }
                    }.start();
            }
        });


        Button pausebt = view.findViewById(R.id.pausebt);

        pausebt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (isRunnig && !isPaused) {
                    isPaused = true;
                    isRunnig = false;
                }
            }
        });

        Button stopbt = view.findViewById(R.id.stop);

        stopbt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                isPaused = false;
                isRunnig = false;
                ((EditText) view.findViewById(R.id.seconds)).setText(R.string.cero);
                ((EditText) view.findViewById(R.id.seconds)).setText(R.string.cero);
            }
        });


        return view;
    }


    private Long getTimeLeft(){
        Long minutes =  Long.parseLong(((EditText)view.findViewById(R.id.minutes)).getText().toString())*60*1000;
        Long seconds =  Long.parseLong(((EditText)view.findViewById(R.id.seconds)).getText().toString())* 1000 ;
        return minutes+seconds;
    }


}
