package com.alfarouk.task.presentation.ui.postlist.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alfarouk.task.Util.getOrAwaitSecondValue
import com.alfarouk.task.data.Mapper.ToEntity
import com.alfarouk.task.data.local.model.Movie
import com.alfarouk.task.data.remote.dto.MoviesResponse
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.domain.repository.Repository
import com.alfarouk.task.domain.usecase.getOfflineMoviesUseCase.GetOfflineMoviesUseCase
import com.alfarouk.task.domain.usecase.getOnlineMoviesUseCase.GetOnlineMoviesUseCase
import com.alfarouk.task.util.ErrorType
import com.alfarouk.task.util.Resource
import com.alfarouk.task.util.Status
import com.alfarouk.task.util.isInternetAvailable
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class PostsListViewModelTest {

    private lateinit var viewModel: PostsListViewModel
    private lateinit var getOnlineMoviesUseCase: GetOnlineMoviesUseCase
    private lateinit var getOfflineMoviesUseCase: GetOfflineMoviesUseCase

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var repository: Repository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        getOnlineMoviesUseCase = GetOnlineMoviesUseCase(repository)
        getOfflineMoviesUseCase = GetOfflineMoviesUseCase(repository)
        viewModel =
            PostsListViewModel(getOnlineMoviesUseCase, getOfflineMoviesUseCase, connectivityManager)
    }

    @Test
    fun `getMovies with internet connection should load online movies`() = runBlocking {
        // Given
        val context: Context = mock(Context::class.java)

        val networkInfo = mock(NetworkInfo::class.java)

        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(true)

        val m = Movie("1", "Test", "Test")
        val moviesResponse = MoviesResponse(results = listOf(m))
        Mockito.`when`(repository.getOnlineMovies(1)).thenReturn(Resource.success(moviesResponse))

        // When
        viewModel.getMovies()

        // Then
        val response = viewModel.getMovieResponse.getOrAwaitSecondValue() // Skip Loading and get Second Value
        assertThat(response.status, equalTo(Status.SUCCESS))
        assertThat(response.data, equalTo(listOf(m.ToEntity())))
    }

    @Test
    fun `getMovies without internet connection should load offline movies`() = runBlocking {
        // Given
        val context: Context = mock(Context::class.java)

        val networkInfo = mock(NetworkInfo::class.java)

        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(false)

        val movieEntity = MovieEntity("test", "Test")
        Mockito.`when`(repository.getOfflineMovies())
            .thenReturn(Resource.success(listOf(movieEntity)))

        // When
        viewModel.getMovies()

        // Then
        val response = viewModel.getMovieResponse.getOrAwaitSecondValue() // Skip Loading and get Second Value
        assertThat(response.status, equalTo(Status.SUCCESS))
        assertThat(response.data, equalTo(listOf(movieEntity)))
    }

    @Test
    fun `getMovies with internet connection and no more pages should not load movies`() =
        runBlocking {
            // Given
            val context: Context = mock(Context::class.java)

            val networkInfo = mock(NetworkInfo::class.java)

            `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
                connectivityManager
            )
            `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
            `when`(networkInfo.isConnected).thenReturn(true)

            val moviesResponse = MoviesResponse(
                page = 1,
                totalPages = 1,
                results = listOf(Movie("1", "Test", "test"))
            )
            Mockito.`when`(repository.getOnlineMovies(1))
                .thenReturn(Resource.success(moviesResponse))


            // When
            viewModel.getMovies()

            // Then
            val response = viewModel.getMovieResponse.getOrAwaitSecondValue() // Skip Loading and get Second Value
            assertThat(response.status, equalTo(Status.SUCCESS))
            assertThat(response.data, notNullValue())
            assertThat(viewModel.paginator.canLoadNextPage(), equalTo(false))
        }

    @Test
    fun `getMovies with error should return error`() = runBlocking {
        // Given
        val context: Context = mock(Context::class.java)

        val networkInfo = mock(NetworkInfo::class.java)

        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(true)

        Mockito.`when`(repository.getOnlineMovies(1))
            .thenReturn(Resource.error(null, ErrorType.NETWORK))

        // When
        viewModel.getMovies()

        // Then
        val response = viewModel.getMovieResponse.getOrAwaitSecondValue() // Skip Loading and get Second Value
        assertThat(response.status, equalTo(Status.ERROR))
        assertThat(response.message, equalTo(ErrorType.NETWORK))
    }
}
