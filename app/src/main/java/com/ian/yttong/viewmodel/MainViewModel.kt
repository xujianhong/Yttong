package com.ian.yttong.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ian.yttong.network.RequestApi
import com.ian.yttong.network.entity.BannerList
import com.ian.yttong.network.entity.ErrorMessage
import com.ian.yttong.network.entity.GradeList
import com.ian.yttong.network.key

class MainViewModel : BaseViewModel() {
    private val _bannerList =MutableLiveData<List<BannerList>>()
    private val _gradeList = MutableLiveData<List<GradeList>>()

    val bannerList:LiveData<List<BannerList>> = _bannerList
    val gradeList:LiveData<List<GradeList>> = _gradeList


    fun getBannerList(){
        getData {
            val result  = RequestApi.retrofitService.getBannerList(key)
            when (result.code) {
                0 -> {
                    _bannerList.value = result.data!!
                }
                else -> {
                    _msg.value = ErrorMessage(result.code, result.msg)
                }
            }
        }
    }

    fun getGrade(){
        getData {
            val result = RequestApi.retrofitService.getGradeList(key)
            when(result.code){
                0->{
                    _gradeList.value =result.data!!
                }
                else ->{
                    _msg.value = ErrorMessage(result.code,result.msg)
                }
            }
        }
    }


}