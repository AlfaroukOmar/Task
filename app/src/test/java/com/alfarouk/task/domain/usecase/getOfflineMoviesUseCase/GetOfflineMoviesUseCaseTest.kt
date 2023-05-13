package com.alfarouk.task.domain.usecase.getOfflineMoviesUseCase

import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.domain.repository.Repository
import com.alfarouk.task.util.ErrorType
import com.alfarouk.task.util.Resource
import com.alfarouk.task.util.Status
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class GetOfflineMoviesUseCaseTest {

    private lateinit var useCase: GetOfflineMoviesUseCase

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        useCase = GetOfflineMoviesUseCase(repository)
    }

    @Test
    fun `load should return offline movies`() = runBlocking {
        // Given
        val movieEntity = MovieEntity("test", "test")
        Mockito.`when`(repository.getOfflineMovies())
            .thenReturn(Resource.success(listOf(movieEntity)))
        // When
        val result = useCase.load()
        // Then
        MatcherAssert.assertThat(result.status, CoreMatchers.equalTo(Status.SUCCESS))
        MatcherAssert.assertThat(result.data, CoreMatchers.equalTo(listOf(movieEntity)))
    }

    @Test
    fun `load with error should return error`() = runBlocking {
        // Given
        Mockito.`when`(repository.getOfflineMovies())
            .thenReturn(Resource.error(null, ErrorType.NETWORK))
        // When
        val result = useCase.load()
        // Then

        MatcherAssert.assertThat(result.status, CoreMatchers.equalTo(Status.ERROR))
        MatcherAssert.assertThat(result.message, CoreMatchers.equalTo(ErrorType.NETWORK))
    }
}

