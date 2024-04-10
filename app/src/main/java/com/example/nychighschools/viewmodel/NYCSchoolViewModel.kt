package com.example.nychighschools.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nychighschools.data.model.NYCHighSchools
import com.example.nychighschools.data.repository.NYCSchoolsRepository
import com.example.nychighschools.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NYCSchoolViewModel @Inject constructor(
    private val nycSchoolsRepository: NYCSchoolsRepository
) : ViewModel() {
    val nycSchoolsData: StateFlow<Result<List<NYCHighSchools>>> =
        nycSchoolsRepository.getSchoolDataFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading
            )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        refreshSchoolData()
    }

    fun refreshSchoolData() {
        _isRefreshing.value = true

        viewModelScope.launch {
            try {
                nycSchoolsRepository.refreshSchoolData()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

}