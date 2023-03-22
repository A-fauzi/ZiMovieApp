package com.afauzi.zimovieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieDetail

@Database(entities = [MovieBookmark::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieBookmarkDao(): MovieBookmarkDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zi_movie"
                ).build().also { instance = it }
            }
        }
    }
}