package com.appsfactory.test.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.appsfactory.test.data.mappers.toAlbums
import com.appsfactory.test.data.mappers.toArtists
import com.appsfactory.test.data.mappers.toTracks
import com.appsfactory.test.domain.util.Result
import com.appsfactory.test.utils.Constants.BASE_URL
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class RetrofitClientTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var lastFMApi: LastFMApi

    private lateinit var fakeApiRepository: FakeApiRepository

    @Before
    fun setup() {
        hiltRule.inject()
        fakeApiRepository = FakeApiRepository()
    }

    @Test
    fun testRetrofitInstanceBaseUrlMatchesBaseUrl() {
        val baseUrl = retrofit.baseUrl().toUrl().toString()
        assertThat(baseUrl == BASE_URL)
    }

    @Test
    fun testPostRequestGenericFunctionConversionFromDtoToDomainModelForArtists() = runTest {
        val result = fakeApiRepository.makeRequest(
            request = {
                lastFMApi.searchArtist("cher")
            },
            response = {
                toArtists()
            }
        ).first()

        assertThat(result is Result.Success)
    }

    @Test
    fun testPostRequestGenericFunctionConversionFromDtoToDomainModelForAlbums() = runTest {
        val result = fakeApiRepository.makeRequest(
            request = {
                lastFMApi.getTopAlbums("cher")
            },
            response = {
                toAlbums()
            }
        ).first()

        assertThat(result is Result.Success)
    }

    @Test
    fun testPostRequestGenericFunctionConversionFromDtoToDomainModelForTracks() = runTest {
        val result = fakeApiRepository.makeRequest(
            request = {
                lastFMApi.getTracks("cher", "cher")
            },
            response = {
                toTracks()
            }
        ).first()

        assertThat(result is Result.Success)
    }

    @Test
    fun testGetTopAlbumOnLastFMApi() = runTest {
        val response = lastFMApi.getTopAlbums("cher")

        val errorBody = response.errorBody()
        assertThat(errorBody).isNull()

        val responseWrapper = response.body()
        assertThat(responseWrapper).isNotNull()
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun testSearchArtistOnLastFMApi() = runTest {
        val response = lastFMApi.searchArtist("cher")

        val errorBody = response.errorBody()
        assertThat(errorBody).isNull()

        val responseWrapper = response.body()
        assertThat(responseWrapper).isNotNull()
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun testGetTracksOnLastFMApi() = runTest {
        val response = lastFMApi.getTracks("cher", "cher")

        val errorBody = response.errorBody()
        assertThat(errorBody).isNull()

        val responseWrapper = response.body()
        assertThat(responseWrapper).isNotNull()
        assertThat(response.code()).isEqualTo(200)
    }
}