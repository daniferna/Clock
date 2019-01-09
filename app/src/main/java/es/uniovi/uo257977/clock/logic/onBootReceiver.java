package es.uniovi.uo257977.clock.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
public class onBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BOOT","Se ha llegado al on boot");
        ArrayList<Alarm> alarmas= AlarmIntentUtil.cargarSharedPreferences(context);
        if (alarmas!=null){
            Log.d("ON_BOOT","1");
            for (Alarm alarm:alarmas) {
                Log.d("ON_BOOT","2");
                AlarmIntentUtil.añadirAlarmManager(alarm, context);
                Log.d("ON_BOOT","Se han añadido correctamente las alarmas");
            }
        }
        AlarmIntentUtil.actualizarPreferences(context,alarmas);
        Log.d("BOOT","DESPUES");

    }





}
