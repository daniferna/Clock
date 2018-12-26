package es.uniovi.uo257977.clock

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import es.uniovi.uo257977.clock.Fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}
