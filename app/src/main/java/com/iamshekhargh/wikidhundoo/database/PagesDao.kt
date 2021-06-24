package com.iamshekhargh.wikidhundoo.database

import androidx.room.*
import com.iamshekhargh.wikidhundoo.network.response.Page
import kotlinx.coroutines.flow.Flow


@Dao
interface PagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(p: Page)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pages: List<Page>)

    @Delete
    suspend fun deletePage(p: Page)

    @Update
    suspend fun updatePage(p: Page)

    @Query("SELECT * FROM pages_list")
    fun getAllPages(): Flow<List<Page>>

    @Query("SELECT * FROM pages_list where title LIKE '%' || :query || '%'")
    fun getSearchResults(query: String): Flow<List<Page>>

    @Query("SELECT * FROM pages_list where title LIKE '%' || :query || '%'")
    fun getSearchResultsList(query: String): MutableList<Page>


}