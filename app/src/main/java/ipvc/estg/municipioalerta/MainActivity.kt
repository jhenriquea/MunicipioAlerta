package ipvc.estg.municipioalerta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun entrar(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun notasPessoais(view: View) {
        val intent = Intent(this, Notas::class.java)
        startActivity(intent)
    }


}