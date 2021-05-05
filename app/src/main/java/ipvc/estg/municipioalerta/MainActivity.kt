package ipvc.estg.municipioalerta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun entrar(view: View) {
        val sessaoAutomatica: SharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
        )

        if (sessaoAutomatica.getBoolean("loged", false)) {
            val intent = Intent(this@MainActivity, MapaAnomalia::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun notasPessoais(view: View) {
        val intent = Intent(this, Notas::class.java)
        startActivity(intent)
    }

    //asdasd
    //asdasdasdasdasd
    //asdasdasdasdasdasd
    //dev
}