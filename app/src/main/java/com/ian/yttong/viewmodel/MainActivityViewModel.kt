package com.ian.yttong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ian.yttong.network.RequestApi
import com.ian.yttong.network.entity.ErrorMessage
import com.ian.yttong.network.entity.VersionBean
import com.ian.yttong.network.key

/**
 * Description
 * Created by jianhongxu on 2022/5/16
 */

class MainActivityViewModel : BaseViewModel() {

    private val _version = MutableLiveData<VersionBean>()


    val version: LiveData<VersionBean> = _version


    fun getVersionInfo(){
        getData {
            val result = RequestApi.retrofitService.getVersionInfo(key,1)
            when (result.code) {
                0 -> {
                    _version.value = result.data!!
                }
                else -> {
                    _msg.value = ErrorMessage(result.code, result.msg)
                }
            }
        }
    }




}