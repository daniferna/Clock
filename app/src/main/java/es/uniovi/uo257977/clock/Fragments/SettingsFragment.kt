package es.uniovi.uo257977.clock.Fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import es.uniovi.uo257977.clock.R

class SettingsFragment : PreferenceFragmentCompat() {

    val SELECCION_TONO = 10
    val CONCEDER_PERMISOS = 11
    var tonoSeleccionado : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val preferenceSystemHour = findPreference("preference_modify_system_hour")
        val preferenceTono = findPreference("preference_seleccionar_tono")
        val preferencePermisos = findPreference("preference_conceder_permisos")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (!Settings.System.canWrite(context)){
                preferenceTono.isEnabled = false
                preferenceTono.summary = "No disponible debido a ausencia de permisos"
            } else
                preferencePermisos.isVisible = false

        preferenceSystemHour.setOnPreferenceClickListener {
            val intentHoraSistema = Intent(Settings.ACTION_DATE_SETTINGS)
            startActivity(intentHoraSistema)
            return@setOnPreferenceClickListener true
        }

        preferenceTono.onPreferenceClickListener = Preference.OnPreferenceClickListener {

            val intentTono = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccionar tono")
            intentTono.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, tonoSeleccionado)
            startActivityForResult(intentTono, SELECCION_TONO)

            return@OnPreferenceClickListener true
        }

        preferencePermisos.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS), CONCEDER_PERMISOS)
                activity?.onBackPressed()
            }
            return@OnPreferenceClickListener true
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECCION_TONO && resultCode == RESULT_OK){
            val tonoSeleccionado : Uri = data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) ?: return
            this.tonoSeleccionado = tonoSeleccionado
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, tonoSeleccionado)
        }
    }

}
