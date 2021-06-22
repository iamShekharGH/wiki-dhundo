package com.iamshekhargh.wikidhundoo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iamshekhargh.wikidhundoo.network.response.Page

@Database(entities = [Page::class], version = 1, exportSchema = false)
abstract class PagesDatabase : RoomDatabase() {
    abstract fun getPagesDao(): PagesDao
}