package es.uniovi.uo257977.clock.Fragments;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import es.uniovi.uo257977.clock.Logic.TimerReceiver;
import es.uniovi.uo257977.clock.R;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class TimerFragment extends Fragment{

    public static final int TIMER_NOTIFICATION_ID = 20;

    protected View view;
    private Boolean isRunnig=false,isPaused=false;
    private MaterialProgressBar progressBar;
    private Long tiempoTotal = 0L;
    private CountDownTimer countDownTimer;
    private TextView txtTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_timer, container, false);

        FloatingActionButton pausebt = view.findViewById(R.id.fabPauseTimer);
        txtTimer = view.findViewById(R.id.txtTimer);



        txtTimer.setOnClickListener(v -> {
            seleccionarSegundos();
            seleccionarMinutos();
        });


        progressBar = view.findViewById(R.id.materialProgressBar);

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

        FloatingActionButton stopbt = view.findViewById(R.id.fabStopTimer);

        stopbt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                isPaused = false;
                isRunnig = false;
                txtTimer.setText("00:00");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    progressBar.setProgress(0, true);
                else
                    progressBar.setProgress(0);
                if (countDownTimer != null)
                    countDownTimer.cancel();
                countDownTimer = null;
            }
        });

        return view;
    }

    private void seleccionarMinutos() {
        MaterialNumberPicker numberPicker = new MaterialNumberPicker(view.getContext());
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(0);
        numberPicker.setTextSize(getResources().getDimensionPixelSize(R.dimen.about_item_text_size));
        numberPicker.setSeparatorColor(ContextCompat.getColor(view.getContext(), R.color.accent));
        numberPicker.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primary_dark));
        numberPicker.setEditable(false);
        new AlertDialog.Builder(view.getContext())
                .setTitle("Seleccione los minutos")
                .setView(numberPicker)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    txtTimer.setText(String.format(Locale.getDefault(),
                            "%d:%02d", numberPicker.getValue(), Integer.valueOf(txtTimer.getText().toString().split(":")[1])));
                }).show();
    }

    private void seleccionarSegundos() {
        MaterialNumberPicker numberPicker = new MaterialNumberPicker(view.getContext());
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(10);
        numberPicker.setTextSize(getResources().getDimensionPixelSize(R.dimen.about_item_text_size));
        numberPicker.setSeparatorColor(ContextCompat.getColor(view.getContext(), R.color.accent));
        numberPicker.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primary_dark));
        numberPicker.setEditable(false);
        new AlertDialog.Builder(view.getContext())
                .setTitle("Seleccione los segundos")
                .setView(numberPicker)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    txtTimer.setText(String.format(Locale.getDefault(),
                            "%d:%02d", Integer.valueOf(txtTimer.getText().toString().split(":")[0]), numberPicker.getValue()));})
                .show();
    }

    public void setOnClickFabPlay(FloatingActionButton fab) {

        fab.setOnClickListener(v -> {
            if(getTimeLeft() == 0)
                return;

            if (isPaused && countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }

            isRunnig = true;
            isPaused = false;

            if (countDownTimer == null) {
                tiempoTotal = getTimeLeft();
                countDownTimer = new CountDownTimer(getTimeLeft(), 1000) {
                    public void onTick(long millisUntilFinished) {

                        if (isRunnig || isPaused) {
                            if (!isRunnig && isPaused) {
                                cancel();
                            }
                            if (!isPaused) {
                                int seconds2 = (int) (millisUntilFinished / 1000);
                                int minutes2 = seconds2 / 60;
                                seconds2 = seconds2 % 60;

                                TextView txtTimer = view.findViewById(R.id.txtTimer);
                                txtTimer.setText(String.format(Locale.getDefault(), "%d:%02d", minutes2, seconds2));

                                float tiempoIzquierda = millisUntilFinished/1000;
                                float tiempoDerecha = tiempoTotal / 1000;
                                int progreso = (int) ((tiempoIzquierda / tiempoDerecha)*100);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                    progressBar.setProgress(100-progreso, true);
                                else
                                    progressBar.setProgress(100-progreso);
                            }
                        }
                    }

                    public void onFinish() {
                        isRunnig = false;
                        isPaused = false;
                        mostrarNotificacionTimer();
                    }
                }.start();
            }
        });

    }

    private void mostrarNotificacionTimer() {
        Intent intent = new Intent(getContext(), TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(getContext(), getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_timer_black_24dp)
                .setContentTitle("Temporizador finalizado")
                .setContentText("La cuenta atras ha finalizado")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_ALARM), AudioManager.STREAM_ALARM)
                .addAction(R.drawable.ic_volume_mute, getString(android.R.string.ok), pendingIntent)
                .setAutoCancel(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    getString(R.string.default_notification_channel_title), NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(RingtoneManager.getActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_ALARM), new AudioAttributes.Builder().
                    setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(TIMER_NOTIFICATION_ID, notiBuilder.build());
        } else
            NotificationManagerCompat.from(getContext()).notify(TIMER_NOTIFICATION_ID, notiBuilder.build());

    }

    private Long getTimeLeft(){
        String tiempo = ((TextView) view.findViewById(R.id.txtTimer)).getText().toString();
        if (tiempo == "00:00")
            return 0L;
        String[] tiempos = tiempo.split(":");
        Long minutes = Long.parseLong(tiempos[0])*60*1000;
        Long seconds = Long.parseLong(tiempos[1]) * 1000 ;
        return minutes+seconds;
    }


}
