package ipvc.estg.municipioalerta.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ipvc.estg.municipioalerta.dao.NotaDao
import ipvc.estg.municipioalerta.entities.Nota

class NotaDB {
    @Database(entities = arrayOf(Nota::class), version = 1, exportSchema = false)
    public abstract class NotaRoomDatabase : RoomDatabase() {

        abstract fun notaDao(): NotaDao

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: NotaRoomDatabase? = null

            fun getDatabase(context: Context): NotaRoomDatabase {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotaRoomDatabase::class.java,
                        "nota_database"
                    ).build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }
}