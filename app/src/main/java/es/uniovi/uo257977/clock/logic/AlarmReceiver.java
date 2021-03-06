package es.uniovi.uo257977.clock.logic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.R;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private NotificationManager notifManager;
    private SpotifyConnection spotifyConnection;
    public static final int ALARM_NOTIFICATION_ID = 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notificacion", "Entra");
        //Cargar las alarmas de las shared preferences
        List<Alarm> alarmas = cargarSharedPreferences(context);


        spotifyConnection = SpotifyConnection.getSpotifyConnection();

        //Comprobar que alarma es la que se esta disparando
        for (Alarm alarm : alarmas) {
            Calendar tiempoActual = Calendar.getInstance();
            tiempoActual.set(Calendar.SECOND, 0);
            tiempoActual.set(Calendar.MILLISECOND, 0);

            Date tiempoActualDate = tiempoActual.getTime();
            if (tiempoActualDate.getTime() == (alarm.getFecha_alarma().getTime())) {
                notificacionAlarma(alarm, context, intent);
            }
        }
    }

    private void notificacionAlarma(Alarm alarm, Context context, Intent intent) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        createNotification("Alarma de las : "+df.format(alarm.getFecha_alarma()) + " Nombre Alarma: " + alarm.getNombre(), context, alarm);
        if (alarm.getSpotify() == true) {
            //  spotifyConnection= new SpotifyConnection();
            spotifyConnection.activateSpotifyAlarm();
        }

    }

    private List<Alarm> cargarSharedPreferences(Context context) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "AlarmAppPreferences", MODE_PRIVATE);
        ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
        return alarmasList.getAlarms();
    }

    public void createNotification(String aMessage, Context context, Alarm alarm) {
        long[] pattern = {0, 100, 500};
        final int NOTIFY_ID = 0; // ID of notification
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = context.getString(R.string.default_notification_channel_title); // Default Channel

        Intent intent = new Intent(context, StopAlarmReceiver.class);
        intent.putExtra("Tiempo",System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id);

        builder.setContentTitle(aMessage)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentText(context.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false)
                .setCategory(Notification.CATEGORY_ALARM)
                .setTicker(aMessage)
                .addAction(R.drawable.ic_volume_mute, context.getString(android.R.string.ok), pendingIntent);
        if (alarm.getSpotify()== false)
           builder.setSound(Uri.parse(alarm.getSonido()), AudioManager.STREAM_ALARM);

        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableLights(true);
                if (alarm.getSpotify()==false)
                    mChannel.setSound(Uri.parse(alarm.getSonido()), new AudioAttributes.Builder().
                        setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                if (alarm.isVibrar()) {
                    mChannel.setVibrationPattern(pattern);
                }
                notifManager.createNotificationChannel(mChannel);

            }
        } else {
            if(alarm.isVibrar()){
                builder.setVibrate(pattern);
            }
            builder.setSound(Uri.parse(alarm.getSonido()), AudioManager.STREAM_ALARM);
            Log.d("Notification", "Entro por aca");
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

        Log.d("Notification", "SONIDITOOOO");
        AlarmIntentUtil.añadirAlarmManager(alarm, context);
    }
}
