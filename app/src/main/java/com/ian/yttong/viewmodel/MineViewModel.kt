package com.ian.yttong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ian.yttong.network.RequestApi
import com.ian.yttong.network.entity.AboutUs
import com.ian.yttong.network.entity.ErrorMessage
import com.ian.yttong.network.key

class MineViewModel : BaseViewModel() {
    private val _about = MutableLiveData<AboutUs>()

    val about: LiveData<AboutUs> = _about


    fun getAbout() {
        getData {
            val result = RequestApi.retrofitService.getAboutUs(key)
            when (result.code) {
                0 -> {
                    _about.value = result.data!!
                }
                else -> {
                    _msg.value = ErrorMessage(result.code, result.msg)
                }
            }
        }
    }

}