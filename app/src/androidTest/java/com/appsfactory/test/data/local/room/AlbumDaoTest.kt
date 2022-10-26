package com.appsfactory.test.data.local.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.appsfactory.test.data.mappers.toAlbumDto
import com.appsfactory.test.data.mappers.toAlbumsDtoList
import com.appsfactory.test.data.mappers.toAlbumsList
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class AlbumDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AlbumDatabase
    private lateinit var dao: AlbumDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAlbum() = runTest {
        val album = Album("name", "url", Artist("test", "url", "url"), "url", emptyList())
        dao.insertAlbum(album.toAlbumDto())

        val allAlbums = dao.getAllAlbums().first().toAlbumsList()
        Truth.assertThat(allAlbums).contains(album)
    }

    @Test
    fun deleteAlbum() = runTest {
        val album = Album("name", "url", Artist("test", "url", "url"), "url", emptyList())
        dao.insertAlbum(album.toAlbumDto())
        dao.deleteAlbum(album.toAlbumDto())

        val allAlbums = dao.getAllAlbums().first().toAlbumsList()
        Truth.assertThat(allAlbums).doesNotContain(album)
    }

    @Test
    fun isFavoriteAlbumExists() = runTest {
        val album = Album("name", "url", Artist("test", "url", "url"), "url", emptyList())
        dao.insertAlbum(album.toAlbumDto())

        val favoriteAlbum = dao.isExists(album.name)
        Truth.assertThat(favoriteAlbum).isTrue()
    }

    @Test
    fun deleteAllAlbums() = runTest {
        val albums = listOf(
            Album("name", "url", Artist("test", "url", "url"), "url", emptyList()),
            Album("name2", "url", Artist("test", "url", "url"), "url", emptyList()),
            Album("name3", "url", Artist("test", "url", "url"), "url", emptyList()),
        )
        dao.insertAlbums(albums.toAlbumsDtoList())
        dao.deleteAllAlbums()

        val allAlbums = dao.getAllAlbums().first().toAlbumsList()
        Truth.assertThat(allAlbums).isEmpty()
    }

    @Test
    fun whenAlbumPresentShouldReturnAlbum() = runTest {
        val album = Album("name", "url", Artist("test", "url", "url"), "url", emptyList())
        dao.insertAlbum(album.toAlbumDto())

        val favoriteAlbum = dao.getAlbum(album.name)
        Truth.assertThat(favoriteAlbum).isNotNull()
    }

    @Test
    fun whenAlbumNotPresentShouldReturnNull() = runTest {
        val album = Album("name", "url", Artist("test", "url", "url"), "url", emptyList())

        val favoriteAlbum = dao.getAlbum(album.name)
        Truth.assertThat(favoriteAlbum).isNull()
    }
}