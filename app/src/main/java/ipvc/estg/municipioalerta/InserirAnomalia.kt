package ipvc.estg.municipioalerta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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

class InserirAnomalia : AppCompatActivity() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDesc: EditText

    private lateinit var spinnerTextType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_anomalia)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val spinner: Spinner = findViewById(R.id.type_spinner)
        ArrayAdapter.createFromResource(
                this,
                R.array.type_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val text = spinner.selectedItem.toString()

        Toast.makeText(this@InserirAnomalia, text, Toast.LENGTH_SHORT).show()

    }

    fun inserirAnomalia(view: View) {

//        val spinner: Spinner = findViewById(R.id.type_spinner)
//        ArrayAdapter.createFromResource(
//                this,
//                R.array.type_array,
//                android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
//
//        val text = spinner.selectedItem.toString()
//
//        Toast.makeText(this@InserirAnomalia, text, Toast.LENGTH_SHORT).show()

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
                    spinnerTextType = findViewById(R.id.type_spinner)

                    val titulo = editTextTitle.text.toString()
                    val descricao = editTextDesc.text.toString()
                    val foto = ""
                    val text = spinnerTextType.selectedItem.toString()
                    val login_id = id
                    val latitude = lastLocation.latitude
                    val longitude = lastLocation.longitude

                    Log.i("latitude", latitude.toString())
                    Log.i("longitude", longitude.toString())
                    Log.i("login_id", login_id.toString())
                    Log.i("titulo", titulo)
                    Log.i("tipo", text)
                    Log.i("descricao", descricao)

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.inserirAnomalia(titulo,descricao,latitude.toString(),longitude.toString(),foto, text,login_id.toString().toInt())


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

