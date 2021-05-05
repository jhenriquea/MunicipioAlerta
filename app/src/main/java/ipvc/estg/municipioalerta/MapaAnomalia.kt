package ipvc.estg.municipioalerta

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.municipioalerta.api.Anomalias
import ipvc.estg.municipioalerta.api.EndPoints
import ipvc.estg.municipioalerta.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class MapaAnomalia : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    private var results = FloatArray(1)

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_anomalia)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val fab = findViewById<FloatingActionButton>(R.id.inserirAnomalia)
        fab.setOnClickListener {
            val intent = Intent(this@MapaAnomalia, InserirAnomalia::class.java)
            startActivity(intent)
        }

        val fabnotas = findViewById<FloatingActionButton>(R.id.verNotas)
        fabnotas.setOnClickListener {
            val intent = Intent(this@MapaAnomalia, Notas::class.java)
            startActivity(intent)
            finish()
        }

//        val distancia = findViewById<Switch>(R.id.switch1)
//        distancia.setOnCheckedChangeListener { _, isCheked ->
//            if (isCheked) {
//                distanciaMenor()
//            } else {
//                onMapReady(map)
//            }
//        }

        val layout = findViewById<RelativeLayout>(R.id.layout)

        val geek1 = RadioButton(this)
        geek1.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek1.setText(R.string.transito)
        geek1.id = 0

        val geek2 = RadioButton(this)
        geek2.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek2.setText(R.string.iluminacao)
        geek2.id = 1

        val geek3 = RadioButton(this)
        geek3.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek3.setText(R.string.outros)
        geek3.id = 2

        val geek4 = RadioButton(this)
        geek4.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        geek4.setText(R.string.todos)
        geek4.id = 3

        val radioGroup = RadioGroup(this)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(40, 0, 0, 0)
        radioGroup.layoutParams = params

        radioGroup.addView(geek1)
        radioGroup.addView(geek2)
        radioGroup.addView(geek3)
        radioGroup.addView(geek4)
        layout.addView(radioGroup)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                0 -> getTransito()
                1 -> getIluminacao()
                2 -> getOutros()
                3 -> onMapReady(map)
            }
        }

        val logout = findViewById<FloatingActionButton>(R.id.logout)
        logout.setOnClickListener {
            val sessaoAutomatica: SharedPreferences = getSharedPreferences(
                    getString(R.string.shared_preferences),
                    Context.MODE_PRIVATE
            )
            with(sessaoAutomatica.edit()) {
                clear()
                apply()
            }

            val intent = Intent(this@MapaAnomalia, Login::class.java)
            startActivity(intent)
            finish()
        }

        val distancia = findViewById<Switch>(R.id.switch1)
        distancia.setOnCheckedChangeListener { _, isCheked ->
            if (isCheked) {
                distanciaMenor()
            } else {
                onMapReady(map)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.clear()

        setUpMap()

        // 1
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true

        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            }
        }

        val sessaoAuto: SharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
        )

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllAnomalias()

        call.enqueue(object : Callback<List<Anomalias>> {
            override fun onResponse(call: Call<List<Anomalias>>, response: Response<List<Anomalias>>) {
                if (response.isSuccessful) {
                    val anomalias = response.body()!!

                    for (i in anomalias) {
                        val latlong = LatLng(i.latitude, i.longitude)

                        if (i.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                            map.addMarker(MarkerOptions()
                                    .position(latlong)
                                    .title(i.titulo)
                                    .snippet(i.tipo)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                        } else {
                            map.addMarker(MarkerOptions().position(latlong).title(i.titulo).snippet(i.tipo))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Anomalias>>, t: Throwable) {
                Toast.makeText(this@MapaAnomalia, getString(R.string.erro), Toast.LENGTH_SHORT).show()
            }
        })

        map.setOnMarkerClickListener {
            map.setInfoWindowAdapter(MarkerInfo(this))
            false
        }

    }

    fun getTransito() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomaliasTipo(tipo = "Transito")
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Anomalias>> {
                        override fun onResponse(call: Call<List<Anomalias>>, response: Response<List<Anomalias>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (anomalia.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                                .position(latlong)
                                                .title(anomalia.titulo)
                                                .snippet(anomalia.tipo)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Anomalias>>, t: Throwable) {
                            Toast.makeText(this@MapaAnomalia, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    fun getIluminacao() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomaliasTipo(tipo = "Iluminacao")
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Anomalias>> {
                        override fun onResponse(call: Call<List<Anomalias>>, response: Response<List<Anomalias>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (anomalia.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                                .position(latlong)
                                                .title(anomalia.titulo)
                                                .snippet(anomalia.tipo)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Anomalias>>, t: Throwable) {
                            Toast.makeText(this@MapaAnomalia, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    fun getOutros() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomaliasTipo(tipo = "Outros")
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Anomalias>> {
                        override fun onResponse(call: Call<List<Anomalias>>, response: Response<List<Anomalias>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (anomalia.login_id.equals(sessaoAuto.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                                .position(latlong)
                                                .title(anomalia.titulo)
                                                .snippet(anomalia.tipo)
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Anomalias>>, t: Throwable) {
                            Toast.makeText(this@MapaAnomalia, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }


    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        return results[0]
    }

    fun distanciaMenor() {

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.getAllAnomalias()
                    var position: LatLng

                    val sessaoAuto: SharedPreferences = getSharedPreferences(
                            getString(R.string.shared_preferences), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Anomalias>> {
                        override fun onResponse(call: Call<List<Anomalias>>, response: Response<List<Anomalias>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {

                                    position = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (calculateDistance(location.latitude, location.longitude, anomalia.latitude, anomalia.longitude) < 500) {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distância: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                                    } else if (calculateDistance(location.latitude, location.longitude, anomalia.latitude, anomalia.longitude) < 1000) {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distância: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))

                                    } else if (calculateDistance(location.latitude, location.longitude, anomalia.latitude, anomalia.longitude) < 2500) {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distância: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                    } else {
                                        map.addMarker(MarkerOptions()
                                                .position(position)
                                                .title(anomalia.titulo)
                                                .snippet("Distância: " + results[0].roundToInt() + " metros")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Anomalias>>, t: Throwable) {
                            Toast.makeText(this@MapaAnomalia, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
//                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            }
        }
    }
}

