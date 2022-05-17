package com.ian.yttong.network.entity

import java.io.Serializable

/**
 * Description
 * Created by jianhongxu on 2022/1/14
 */
data class BaseJson<T>(val result: Int =0,val code :Int=0, val msg: String, val data: T?) : Serializable