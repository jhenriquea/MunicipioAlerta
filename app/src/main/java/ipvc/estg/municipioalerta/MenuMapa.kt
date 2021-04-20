package ipvc.estg.municipioalerta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.coroutines.NonCancellable.start

class MenuMapa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_mapa)

        val sessaoAutomatica: SharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )

        val logout = findViewById<Button>(R.id.button_logout)

        logout.setOnClickListener {
            val sessaoAutomatica: SharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
            )
            with(sessaoAutomatica.edit()) {
                clear()
                apply()
            }

            val intent = Intent(this@MenuMapa, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun mapaAnomalias(view: View) {
        val intent = Intent(this@MenuMapa, AlterarNota::class.java)
        startActivity(intent)
        finish()
    }
}