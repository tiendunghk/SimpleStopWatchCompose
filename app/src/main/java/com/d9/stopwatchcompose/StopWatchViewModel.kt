package com.d9.stopwatchcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StopWatchViewModel : ViewModel() {
    private val _timeRange: MutableStateFlow<Long> = MutableStateFlow(0L)
    val timeRange: StateFlow<Long> = _timeRange

    private var currentTime = 0L
    private var job: Job? = null

    fun start(init: Boolean = false) {
        job = viewModelScope.launch {
            withContext(Dispatchers.Default) {
                currentTime = if (init) {
                    System.currentTimeMillis()
                } else {
                    System.currentTimeMillis() - timeRange.value
                }

                while (true) {
                    _timeRange.value = System.currentTimeMillis() - currentTime
                    delay(10)
                }
            }
        }
    }

    fun pause() {
        job?.cancel()
    }

    fun stop() {
        currentTime = 0L
        _timeRange.value = 0L
        job?.cancel()
    }
}