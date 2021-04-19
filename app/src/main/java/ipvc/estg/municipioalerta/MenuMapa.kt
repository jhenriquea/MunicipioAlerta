package ipvc.estg.municipioalerta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuMapa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_mapa)

//        val sessaoAutomatica: SharedPreferences = getSharedPreferences(
//            getString(R.string.shared_preferences),
//            Context.MODE_PRIVATE
//        )

//        val logout = findViewById<Button>(R.id.logout)
//
//        logout.setOnClickListener {
////            val sessaoAutomatica: SharedPreferences = getSharedPreferences(
////                getString(R.string.shared_preferences),
////                Context.MODE_PRIVATE
////            )
////            with(sessaoAutomatica.edit()) {
////                clear()
////                apply()
////            }
//
//            val intent = Intent(this@MenuMapa, Login::class.java)
//            startActivity(intent)
//            finish()
//        }

    }
}