package es.uniovi.uo257977.clock.Logic;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import androidx.legacy.content.WakefulBroadcastReceiver;
import br.com.kots.mob.complex.preferences.ComplexPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Cargar las alarmas de las shared preferences
        List<Alarm> alarmas = cargarSharedPreferences(context);

        //Comprobar que alarma es la que se esta disparando

    }

    private List<Alarm> cargarSharedPreferences(Context context) {
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context , "AlarmAppPreferences", MODE_PRIVATE);
            ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
            return alarmasList.getAlarms();
        }
    }
