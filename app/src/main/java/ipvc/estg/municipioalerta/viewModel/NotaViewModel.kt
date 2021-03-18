package ipvc.estg.municipioalerta.viewModel

import android.app.Application
import androidx.lifecycle.*
import ipvc.estg.municipioalerta.db.NotaDB
import ipvc.estg.municipioalerta.db.*
//import ipvc.estg.municipioalerta.db.NotaDB.NotaRoomDatabase.Companion.getDatabase
import ipvc.estg.municipioalerta.entities.Nota
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository

    val allNotas: LiveData<List<Nota>>

    init {
        val notaDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
        repository = NotaRepository(notaDao)
        allNotas = repository.allNotas
    }
    fun insert(nota: Nota) = viewModelScope.launch {
        repository.insert(nota)
    }

    fun deleteNoteById(id: Int?) = viewModelScope.launch {
        repository.deleteNoteById(id)
    }
}


//class NotaViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: NotaRepository
//
//    val allNotas: LiveData<List<Nota>>
//
//    init {
//        val notaDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
//        repository = NotaRepository(notaDao)
//        allNotas = repository.allNotas
//    }
//
//    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insert(nota)
//    }
//}