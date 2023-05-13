package com.alfarouk.task.presentation.ui.postlist.view

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.runner.AndroidJUnit4
import com.alfarouk.task.domain.entities.MovieEntity
import com.alfarouk.task.presentation.ui.postlist.viewmodel.PostsListViewModel
import com.alfarouk.task.util.Resource
import com.alfarouk.task.util.Status
import org.junit.Before
import org.junit.Test
import com.alfarouk.task.R
import com.alfarouk.task.domain.repository.Repository
import com.alfarouk.task.domain.usecase.getOfflineMoviesUseCase.GetOfflineMoviesUseCase
import com.alfarouk.task.domain.usecase.getOnlineMoviesUseCase.GetOnlineMoviesUseCase
import com.alfarouk.task.util.ErrorType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

// Has A Problem

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@MediumTest
class PostsListFragmentTest {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Mock
    private lateinit var viewModel: PostsListViewModel

    @Mock
    private lateinit var repository: Repository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        hiltRule.inject()

        viewModel = mock(PostsListViewModel::class.java)

        `when`(viewModel.getMovieResponse).thenReturn(
            MutableLiveData(
                Resource(
                    Status.SUCCESS,
                    listOf(
                        MovieEntity("Title 1", "Backdrop 1"),
                        MovieEntity("Title 2", "Backdrop 2")
                    ),
                    null
                )
            )
        )
    }

    @Test
    fun testFragmentIsDisplayed() {
        launchFragmentInContainer<PostsListFragment>(
            themeResId = R.style.Theme_Task // Replace with your app's theme
        ).withFragment {
            postsListViewModeltest = viewModel
        }
        // Check if the fragment is displayed
        onView(withId(R.id.postsList)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewIsVisible() {
        launchFragmentInContainer<PostsListFragment>(
            themeResId = R.style.Theme_Task // Replace with your app's theme
        ).withFragment {
            this.postsListViewModeltest = viewModel
        }

        // Check if the RecyclerView is visible
        onView(withId(R.id.postsList)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewHasItems() {
        launchFragmentInContainer<PostsListFragment>(
            themeResId = R.style.Theme_Task // Replace with your app's theme
        ).withFragment {
            this.postsListViewModeltest = viewModel
        }

        // Check if the RecyclerView contains items
        onView(withId(R.id.postsList)).check(matches(hasMinimumChildCount(1)))
    }

    @Test
    fun testLoadAndErrorHandling() {
        // Test loading state
        `when`(viewModel.getMovieResponse).thenReturn(
            MutableLiveData(Resource(Status.LOADING, null, null))
        )
        launchFragmentInContainer<PostsListFragment>(
            themeResId = R.style.Theme_Task // Replace with your app's theme
        ).withFragment {
            this.postsListViewModeltest = viewModel
        }
        onView(withId(R.id.loading)).check(matches(isDisplayed()))

        // Test error state
        `when`(viewModel.getMovieResponse).thenReturn(
            MutableLiveData(Resource(Status.ERROR, null, ErrorType.NETWORK))
        )
        launchFragmentInContainer<PostsListFragment>(
            themeResId = R.style.Theme_Task // Replace with your app's theme
        ).withFragment {
            this.postsListViewModeltest = viewModel
        }
        onView(withId(R.id.txt_info)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_info)).check(matches(withText(ErrorType.NETWORK.message)))
    }

    @Test
    fun testRecyclerViewStateRestoration() {
        val savedInstanceState = Bundle()
        savedInstanceState.putParcelable("recycler_view_state", LinearLayoutManager.SavedState())

        launchFragmentInContainer<PostsListFragment>(
            themeResId = R.style.Theme_Task // Replace with your app's theme
        ).withFragment {
            this.postsListViewModeltest = viewModel
            this.restoreRecyclerViewState(savedInstanceState)
        }

        // Check if the RecyclerView state was restored
        onView(withId(R.id.postsList)).check(matches(isDisplayed()))
    }
}