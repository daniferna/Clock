package es.uniovi.uo257977.clock.Logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import br.com.kots.mob.complex.preferences.ComplexPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AlarmIntentUtil {

    public static void añadirAlarmManager(Alarm alarma,Context context) {
        //Elimino todos los intents previos y quito los intents del manager
        List<Intent> intents = borrarIntents(alarma,context);

        //Crear nuevos intents segun los dias de la semana que esten activados
        Intent intent;
        for (Alarm.DIAS_ALARMA dia : alarma.getDiasAlarma())
            switch (dia) {
                case LUNES:
                    intent = scheduleAlarm(Calendar.MONDAY,context,alarma);
                    intents.add(intent);
                    break;
                case MARTES:
                    intent = scheduleAlarm(Calendar.TUESDAY,context,alarma);
                    intents.add(intent);
                    break;
                case MIERCOLES:
                    intent = scheduleAlarm(Calendar.WEDNESDAY,context,alarma);
                    intents.add(intent);
                    break;
                case JUEVES:
                    intent = scheduleAlarm(Calendar.THURSDAY,context,alarma);
                    intents.add(intent);
                    break;
                case VIERNES:
                    intent = scheduleAlarm(Calendar.FRIDAY,context,alarma);
                    intents.add(intent);
                    break;
                case SABADO:
                    intent = scheduleAlarm(Calendar.SATURDAY,context,alarma);
                    intents.add(intent);
                    break;
                case DOMINGO:
                    intent = scheduleAlarm(Calendar.SUNDAY,context,alarma);
                    intents.add(intent);
                    break;
            }
        alarma.setIntents(intents);
    }

    private static Intent scheduleAlarm(int dayOfWeek, Context context, Alarm alarma) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(alarma.getFecha_alarma());
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR));

        // Check we aren't setting it in the past which would trigger it to fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        // Creo un intent que recibira la clase AlarmReceiver
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent yourIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, yourIntent);
        Log.d("Fecha",calendar.getTime().toString());
        return myIntent;
    }


    public static ArrayList<Alarm> cargarSharedPreferences(Context context) {

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context , "AlarmAppPreferences", MODE_PRIVATE);
        ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
        if (alarmasList != null){
            return alarmasList.getAlarms();
        }
        return null;
    }

    public static List<Intent> borrarIntents(Alarm alarma, Context context) {
        List<Intent> intents = alarma.getIntents();
        for (Intent intent:intents) {
            PendingIntent yourIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(yourIntent);
            yourIntent.cancel();
        }
        intents.clear();
        return intents;
    }

    public static void actualizarPreferences(Context context,ArrayList<Alarm> alarms) {

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context , "AlarmAppPreferences", MODE_PRIVATE);
        ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
        alarmasList.setAlarms(alarms);
        complexPreferences.putObject("Alarmas", alarmasList);
        complexPreferences.commit();
    }



}
