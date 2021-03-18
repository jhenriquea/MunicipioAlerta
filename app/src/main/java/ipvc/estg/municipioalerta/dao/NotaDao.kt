package ipvc.estg.municipioalerta.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.municipioalerta.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * FROM nota_table")
    fun getAllNotas(): LiveData<List<Nota>>

    @Query(value = "SELECT * FROM nota_table WHERE id = :id")
    fun getNotasId(id: Int): LiveData<Nota>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()

    //Update Notas
    @Update
    suspend fun editNote(nota : Nota)

    //Delete pelo id
    @Query("DELETE FROM nota_table where id ==:id")
    suspend fun deleteNoteById(id: Int?)
}