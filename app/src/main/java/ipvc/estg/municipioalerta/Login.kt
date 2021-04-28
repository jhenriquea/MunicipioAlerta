package ipvc.estg.municipioalerta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.municipioalerta.api.EndPoints
import ipvc.estg.municipioalerta.api.ServiceBuilder
import ipvc.estg.municipioalerta.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button = findViewById<Button>(R.id.butaoLogin)

        button.setOnClickListener {
            val editNomeText = findViewById<EditText>(R.id.inputUser)
            val editPassText = findViewById<EditText>(R.id.inputPassword)

            val nome = editNomeText.text.toString().trim()
            val password = editPassText.text.toString().trim()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.verificarUtilizador(username = nome, password = password)

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        val user = response.body()!!

                        val intent = Intent(this@Login, MapaAnomalia::class.java)
                        startActivity(intent)
                        finish()

                        val sessaoAutomatica: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences),
                            Context.MODE_PRIVATE
                        )
                        with(sessaoAutomatica.edit()) {
                            putBoolean(getString(R.string.loged), true)
                            putString(getString(R.string.username), nome)
                            putInt(getString(R.string.id), user.id)
                            apply()
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@Login, "Login Errado!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }











//    fun registoLogin (view: View) {
//        val intent = Intent(this, Registo::class.java)
//        startActivity(intent)
//    }
}