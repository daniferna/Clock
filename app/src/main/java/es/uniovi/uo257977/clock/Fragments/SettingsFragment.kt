package es.uniovi.uo257977.clock.Fragments


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import es.uniovi.uo257977.clock.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
