package ipvc.estg.municipioalerta.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.municipioalerta.dao.NotaDao
import ipvc.estg.municipioalerta.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

    @Database(entities = [Nota::class], version = 1, exportSchema = false)
    public abstract class NotaDB : RoomDatabase() {

        abstract fun notaDao(): NotaDao

        private class NoteDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        var notasDao = database.notaDao()
                    }
                }
            }
        }

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: NotaDB? = null

            fun getDatabase(context: Context, scope: CoroutineScope): NotaDB {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotaDB::class.java,
                        "nota_database"
                    ).build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }
