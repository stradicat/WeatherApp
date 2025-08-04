package dev.dmayr.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.dmayr.weatherapp.database.dao.ClimaDao
import dev.dmayr.weatherapp.entities.ClimaEntity
import dev.dmayr.weatherapp.entities.Converters
import dev.dmayr.weatherapp.entities.PronosticoEntity

@Database(
    entities = [ClimaEntity::class, PronosticoEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class ClimaDatabase : RoomDatabase() {
    abstract fun climaDao(): ClimaDao

    companion object {
        @Volatile
        private var INSTANCE: ClimaDatabase? = null

        fun getDatabase(context: Context): ClimaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClimaDatabase::class.java,
                    "clima_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
