package ipvc.estg.municipioalerta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import ipvc.estg.municipioalerta.api.Anomalias
import ipvc.estg.municipioalerta.api.EndPoints
import ipvc.estg.municipioalerta.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array.getInt

class InserirAnomalia : AppCompatActivity() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDesc: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_anomalia)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    fun inserirAnomalia(view: View) {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    val currentLatLng = LatLng(location.latitude, location.longitude)

                    var id: Int? = 0

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences),
                            Context.MODE_PRIVATE
                    )

                    id = sessaoAuto.all[getString(R.string.id)] as Int?


                    editTextTitle = findViewById(R.id.tituloAnomalia)
                    editTextDesc = findViewById(R.id.descricaoAnomalia)

                    val titulo = editTextTitle.text.toString()
                    val descricao = editTextDesc.text.toString()
                    val foto = ""
                    val login_id = id
                    val latitude = lastLocation.latitude
                    val longitude = lastLocation.longitude

                    Log.i("latitude", latitude.toString())
                    Log.i("longitude", longitude.toString())
                    Log.i("login_id", login_id.toString())
                    Log.i("titulo", titulo)
                    Log.i("descricao", descricao)

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.inserirAnomalia(titulo,descricao,latitude.toString(),longitude.toString(),foto,login_id.toString().toInt())


                    call.enqueue(object : Callback<Anomalias> {
                        override fun onResponse(call: Call<Anomalias>, response: Response<Anomalias>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@InserirAnomalia, "Adicionado Com Sucesso!", Toast.LENGTH_SHORT).show()
                            }

                            val intent = Intent(this@InserirAnomalia, MapaAnomalia::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(call: Call<Anomalias>?, t: Throwable?) {
                            Toast.makeText(applicationContext, t!!.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}

