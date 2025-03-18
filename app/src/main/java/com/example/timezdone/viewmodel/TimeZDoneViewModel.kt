package com.example.timezdone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timezdone.model.TimeZoneApi
import com.example.timezdone.model.TimeZoneResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimeZDoneViewModel : ViewModel() {
    private val _time = MutableStateFlow("Select a timezone")  // 使用空字符串作为默认值
    val time = _time.asStateFlow()

    fun fetchTime(region: String, city: String) {
        viewModelScope.launch {
            try {
                val response: TimeZoneResponse = TimeZoneApi.getInstance().getTimeZone(region, city)
                _time.value = "Time in $region/$city: ${response.datetime}"
            } catch (e: Exception) {
                _time.value = "Failed to load time"
            }
        }
    }
}

