package ipvc.estg.municipioalerta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.municipioalerta.adapter.NotaListAdapter
import ipvc.estg.municipioalerta.entities.Nota
import ipvc.estg.municipioalerta.viewModel.NotaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Notas : AppCompatActivity() {
    private val newNoteActivityRequestCode = 1

    private lateinit var notaViewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = NotaListAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, { notas ->
            // Update the cached copy of the notes in the adapter.
            notas.let {
                adapter.setNotas(it)
            }
        })

        val fab = findViewById<FloatingActionButton>(R.id.inserirNota)
        fab.setOnClickListener {
            val intent = Intent(this@Notas, InserirNota::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(InserirNota.EXTRA_REPLY_TITLE)?.let { titulo ->
                intentData.getStringExtra(InserirNota.EXTRA_REPLY_DESC)?.let { descricao ->
                    val nota = Nota(titulo = titulo, descricao = descricao)
                    notaViewModel.insert(nota)
                }
            }
        }
    }

    fun delete(id: Int?) {
        notaViewModel.deleteNoteById(id)
    }
}
