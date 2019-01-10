package es.uniovi.uo257977.clock.logic;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import es.uniovi.uo257977.clock.Fragments.TimerFragment;
import es.uniovi.uo257977.clock.MainActivity;

public class StopAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmReceiver.ALARM_NOTIFICATION_ID);

        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));//Cerramos la barra de notificaciones
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.SHARED_PREF_KEY,Context.MODE_PRIVATE);
        Long tiempo=System.currentTimeMillis();
        Calendar dia = Calendar.getInstance();
        Long media;
        switch(dia.get(Calendar.DAY_OF_WEEK)){
            case (Calendar.MONDAY):
                 media= prefs.getLong("Lunes",0);
                 media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                 prefs.edit().putLong("Lunes",media);
            case (Calendar.TUESDAY):
                 media= prefs.getLong("Martes",0);
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.edit().putLong("Martes",media);

            case (Calendar.WEDNESDAY):
                media= prefs.getLong("Miercoles",0);
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.edit().putLong("Miercoles",media);
            case (Calendar.THURSDAY):
                media= prefs.getLong("Jueves",0);
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.edit().putLong("Jueves",media);
            case (Calendar.FRIDAY):
                media= prefs.getLong("Viernes",0);
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.edit().putLong("Viernes",media);
            case (Calendar.SATURDAY):
                media= prefs.getLong("Sabado",0);
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.edit().putLong("Sabado",media);
            case (Calendar.SUNDAY):
                media= prefs.getLong("Domingo",0);
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.edit().putLong("Domingo",media);
        }


    }
}
