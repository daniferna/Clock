package es.uniovi.uo257977.clock.Logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import br.com.kots.mob.complex.preferences.ComplexPreferences;

import static android.content.Context.MODE_PRIVATE;

public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Alarm> alarmas= AlarmIntentUtil.cargarSharedPreferences(context);
        if (alarmas!=null){
            for (Alarm alarm:alarmas) {
                AlarmIntentUtil.añadirAlarmManager(alarm, context);
                Log.d("ON_BOOT","Se han añadido correctamente las alarmas");
            }
        }
        AlarmIntentUtil.actualizarPreferences(context,alarmas);

    }





}
