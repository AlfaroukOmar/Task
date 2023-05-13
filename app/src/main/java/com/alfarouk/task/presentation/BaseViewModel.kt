package com.alfarouk.task.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


open class BaseViewModel: ViewModel() {

    // In Kotlin, all coroutines run inside a CoroutineScope.
    // A scope controls the lifetime of coroutines through its job.
    private val viewModelJob = Job()
    // Since uiScope has a default dispatcher of Dispatchers.Main, this coroutine will be launched
    // in the main thread.
    val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)


    // onCleared is called when the ViewModel is no longer used and will be destroyed.
    // This typically happens when the user navigates away from the Activity or Fragment that was
    // using the ViewModel.
    override fun onCleared() {
        super.onCleared()
        // When you cancel the job of a scope, it cancels all coroutines started in that scope.
        // It's important to cancel any coroutines that are no longer required to avoid unnecessary
        // work and memory leaks.
        viewModelJob.cancel()
    }

}