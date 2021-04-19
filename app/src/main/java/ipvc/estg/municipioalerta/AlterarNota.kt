package ipvc.estg.municipioalerta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.municipioalerta.adapter.DESCRICAO
import ipvc.estg.municipioalerta.adapter.ID
import ipvc.estg.municipioalerta.adapter.TITULO
import ipvc.estg.municipioalerta.entities.Nota
import ipvc.estg.municipioalerta.viewModel.NotaViewModel

class AlterarNota : AppCompatActivity() {

    private lateinit var descricao: EditText
    private lateinit var titulo: EditText
    private lateinit var notaViewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_nota)

        val editTitulo = intent.getStringExtra(TITULO)
        val editDescricao = intent.getStringExtra(DESCRICAO)

        findViewById<EditText>(R.id.alterarTitulo).setText(editTitulo)
        findViewById<EditText>(R.id.alterarDescricao).setText(editDescricao)

        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
    }

    fun updateNota(view: View) {
        titulo = findViewById(R.id.alterarTitulo)
        descricao = findViewById(R.id.alterarDescricao)

        var message = intent.getIntExtra(ID, 0)
        val replyIntent = Intent()

        if (TextUtils.isEmpty(titulo.text) || TextUtils.isEmpty(descricao.text)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
            Toast.makeText(
                    applicationContext,
                    R.string.campos2,
                    Toast.LENGTH_LONG).show()
        }
        else {
            val nota = Nota(id = message, titulo = titulo.text.toString(), descricao = descricao.text.toString())
            notaViewModel.editNota(nota)
            finish()
        }

    }

    fun cancelAlterar(view: View) {
        var intent = Intent(this, Notas::class.java)
        startActivity(intent)
    }
}