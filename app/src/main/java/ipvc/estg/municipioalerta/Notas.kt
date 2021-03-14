package ipvc.estg.municipioalerta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.municipioalerta.adapter.NotaListAdapter
import ipvc.estg.municipioalerta.entities.Nota
import ipvc.estg.municipioalerta.viewModel.NotaViewModel
import ipvc.estg.municipioalerta.viewModel.NotaViewModelFactory
import ipvc.estg.municipioalerta.NotaApplication

class Notas : AppCompatActivity() {
    private val newNoteActivityRequestCode = 1
    private val notaViewModel: NotaViewModel by viewModels {
        NotaViewModelFactory((application as NotaApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = NotaListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        notaViewModel.allNotas.observe(this, { notas ->
            // Update the cached copy of the notes in the adapter.
            notas.let {
                adapter.submitList(it)
            }
        })

//        val buttonInsertNote = findViewById<Button>(R.id.inserirNota)
//        buttonInsertNote.setOnClickListener {
//            val intent = Intent(this@Notas, InserirNota::class.java)
//            resultLauncher.launch(intent)
//        }
//    }
//
//    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data: Intent? = result.data
//            data?.getStringExtra(InserirNota.EXTRA_REPLY_TITLE)?.let { title ->
//                data.getStringExtra(InserirNota.EXTRA_REPLY_DESC)?.let { desc ->
//                    val note = Nota(titulo = title, descricao = desc)
//                    notaViewModel.insert(note)
//                }}
//        }
//    }

        val fab = findViewById<Button>(R.id.inserirNota)
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
//        } else {
//            Toast.makeText(
//                applicationContext,
//                R.string.empty_not_saved,
//                Toast.LENGTH_LONG
//            ).show()
//        }
        }
    }
}
