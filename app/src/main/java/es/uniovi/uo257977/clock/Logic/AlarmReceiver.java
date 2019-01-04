package es.uniovi.uo257977.clock.Logic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.MainActivity;
import es.uniovi.uo257977.clock.R;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private NotificationManager notifManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Cargar las alarmas de las shared preferences
        List<Alarm> alarmas = cargarSharedPreferences(context);

        //Comprobar que alarma es la que se esta disparando
        for (Alarm alarm: alarmas){
            for (Intent intentTemp: alarm.getIntents()){
                if (intent.filterEquals(intentTemp)){
                    notificacionAlarma(alarm,context,intent);
                }
            }
        }
    }

    private void notificacionAlarma(Alarm alarm,Context context,Intent intent) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        createNotification(df.format(alarm.getFecha_alarma()) + "Nombre Alarma:" + alarm.getNombre() ,context,alarm);

    }

    private List<Alarm> cargarSharedPreferences(Context context) {
            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context , "AlarmAppPreferences", MODE_PRIVATE);
            ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
            return alarmasList.getAlarms();
        }

    public void createNotification(String aMessage, Context context,Alarm alarm) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = context.getString(R.string.default_notification_channel_title); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            Log.d("Notification","Entro por aqui");
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                    Log.d("Notification","Entro por aca");
        }

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

    }
    }
