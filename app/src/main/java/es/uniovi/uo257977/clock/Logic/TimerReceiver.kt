package es.uniovi.uo257977.clock.Logic

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import es.uniovi.uo257977.clock.Fragments.TimerFragment

class TimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(TimerFragment.TIMER_NOTIFICATION_ID)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) //Cerramos la barra de notificaciones

    }
}