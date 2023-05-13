package com.alfarouk.task.domain.usecase.getOnlineMoviesUseCase

import com.alfarouk.task.data.local.model.Movie
import com.alfarouk.task.data.remote.dto.MoviesResponse
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.domain.repository.Repository
import com.alfarouk.task.domain.usecase.getOfflineMoviesUseCase.GetOfflineMoviesUseCase
import com.alfarouk.task.util.ErrorType
import com.alfarouk.task.util.Resource
import com.alfarouk.task.util.Status
import kotlinx.coroutines.runBlocking
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class GetOnlineMoviesUseCaseTest {

    private lateinit var useCase: GetOnlineMoviesUseCase

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        useCase = GetOnlineMoviesUseCase(repository)
    }

    @Test
    fun `load should return online movies`() = runBlocking {
        // Given
        val moviesResponse = MoviesResponse(results = listOf(Movie("1", "test", "Test")))
        Mockito.`when`(repository.getOnlineMovies(1)).thenReturn(Resource.success(moviesResponse))
        // When
        val result = useCase.load(1)
        // Then
        assertThat(result.status, equalTo(Status.SUCCESS))
        assertThat(result.data, equalTo(moviesResponse))
    }

    @Test
    fun `load with error should return error`() = runBlocking {
        // Given
        Mockito.`when`(repository.getOnlineMovies(1))
            .thenReturn(Resource.error(null, ErrorType.NETWORK))
        // When
        val result = useCase.load(1)
        // Then
        assertThat(result.status, equalTo(Status.ERROR))
        assertThat(result.message, equalTo(ErrorType.NETWORK))
    }
}