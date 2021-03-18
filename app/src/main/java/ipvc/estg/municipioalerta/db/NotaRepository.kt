package ipvc.estg.municipioalerta.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ipvc.estg.municipioalerta.dao.NotaDao
import ipvc.estg.municipioalerta.entities.Nota

class NotaRepository( private val notaDao: NotaDao ) {

    val allNotas: LiveData<List<Nota>> = notaDao.getAllNotas()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteNoteById(id:Int?){
        notaDao.deleteNoteById(id)
    }
}