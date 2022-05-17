package com.ian.yttong.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ian.yttong.network.entity.ErrorMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Description
 * Created by jianhongxu on 2022/5/16
 */
enum class ProgressStatus { LOADING, ERROR, DONE }
open class BaseViewModel :ViewModel() {

   open val _status = MutableLiveData<ProgressStatus>()
   open val _msg = MutableLiveData<ErrorMessage>()


    val status: LiveData<ProgressStatus> = _status
    val msg:LiveData<ErrorMessage> =_msg


    fun getData(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            _status.value = ProgressStatus.LOADING
            try {
                block()
                _status.value = ProgressStatus.DONE
            } catch (e: Exception) {
                _status.value = ProgressStatus.ERROR
                _msg.value = ErrorMessage(message = e.message.toString())
                Log.e("MainViewModel", e.toString())
            }
        }
    }

}