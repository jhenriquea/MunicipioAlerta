package ipvc.estg.municipioalerta.viewModel

import android.app.Application
import androidx.lifecycle.*
import ipvc.estg.municipioalerta.db.NotaDB
import ipvc.estg.municipioalerta.db.*
import ipvc.estg.municipioalerta.db.NotaDB.NotaRoomDatabase.Companion.getDatabase
import ipvc.estg.municipioalerta.entities.Nota
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class NotaViewModel(private val repository: NotaRepository) : ViewModel() {

    val allNotas: LiveData<List<Nota>> = repository.allNotas



    fun insert(nota: Nota) = viewModelScope.launch {
        repository.insert(nota)
    }
}

class NotaViewModelFactory(private val repository: NotaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
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