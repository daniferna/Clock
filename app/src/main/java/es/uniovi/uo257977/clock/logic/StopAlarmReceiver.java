package es.uniovi.uo257977.clock.logic;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.Fragments.TimerFragment;
import es.uniovi.uo257977.clock.MainActivity;

public class StopAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmReceiver.ALARM_NOTIFICATION_ID);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));//Cerramos la barra de notificaciones

        ComplexPreferences prefs = ComplexPreferences.getComplexPreferences(context, "AlarmAppPreferences", Context.MODE_PRIVATE);
        Long tiempo=System.currentTimeMillis();
        Calendar dia = Calendar.getInstance();
        Long media;
        switch(dia.get(Calendar.DAY_OF_WEEK)){
            case (Calendar.MONDAY):
                 media= prefs.getObject("Lunes",long.class);
                if (media ==null)
                    media =0l;
                 media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                 prefs.putObject("Lunes",media);
                break;
            case (Calendar.TUESDAY):
                media= prefs.getObject("Martes",long.class);
                if (media ==null)
                    media =0l;
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.putObject("Martes",media);
                break;

            case (Calendar.WEDNESDAY):
                media= prefs.getObject("Miercoles",long.class);
                if (media ==null)
                    media =0l;
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.putObject("Miercoles",media);
                break;
            case (Calendar.THURSDAY):
                media= prefs.getObject("Jueves",long.class);
                if (media ==null)
                    media =0l;
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.putObject("Jueves",media);
                break;
            case (Calendar.FRIDAY):
                media= prefs.getObject("Viernes",long.class);
                if (media ==null)
                    media =0l;
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.putObject("Viernes",media);
                break;
            case (Calendar.SATURDAY):
                media= prefs.getObject("Sabado",long.class);
                if (media ==null)
                    media =0l;
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.putObject("Sabado",media);
                break;
            case (Calendar.SUNDAY):
                media= prefs.getObject("Domingo",long.class);
                if (media ==null)
                    media =0l;
                media=(tiempo - intent.getLongExtra("Tiempo",0)+media)/2;
                prefs.putObject("Domingo",media);
                break;
        }
        prefs.commit();

    }
}
