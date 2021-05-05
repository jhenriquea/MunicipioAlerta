package ipvc.estg.municipioalerta

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ipvc.estg.municipioalerta.api.Anomalias
import retrofit2.Callback

class MarkerInfo(context: Context) : GoogleMap.InfoWindowAdapter{

    var mContext = context
    var mapaWindow = (context as Activity).layoutInflater.inflate(R.layout.activity_marker_info, null)


    private fun janela(marker: Marker, view: View){
        val titulo = view.findViewById<TextView>(R.id.tituloAnomalia)
        val descricao = view.findViewById<TextView>(R.id.descricaoAnomalia)

        val data = marker.snippet.split("+").toTypedArray()

        titulo.text = marker.title.take(20)+"..."
        descricao.text = data[0]
    }
    override fun getInfoWindow(marker: Marker): View {
        janela(marker, mapaWindow)
        return mapaWindow
    }
    override fun getInfoContents(marker: Marker): View? {
        janela(marker, mapaWindow)
        return mapaWindow
    }
}