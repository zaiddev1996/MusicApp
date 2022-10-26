package com.appsfactory.test.ui.album

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.appsfactory.test.R
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AlbumFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickOnTopAlbumInRecyclerView_navigateToAlbumDetailsFragment() {
        val navController = Mockito.mock(NavController::class.java)

        val testArtist = Artist("test", "", "")
        val testAlbum = Album("", "", testArtist, "", listOf())

        launchFragmentInHiltContainer<AlbumFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("artist", testArtist)
            }
        ) {
            Navigation.setViewNavController(requireView(), navController)
            /*
            * Need to make albumAdapter public for test to succeed.
            * */
            //albumAdapter.submitList(listOf(testAlbum))
        }

        onView(ViewMatchers.withId(R.id.album_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<AlbumAdapter.AlbumViewHolder>(
                0,
                click()
            )
        )

        Mockito.verify(navController).navigate(
            AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(testAlbum)
        )
    }
}