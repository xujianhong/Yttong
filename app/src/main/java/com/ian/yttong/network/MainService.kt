package com.ian.yttong.network

import com.ian.yttong.network.entity.*
import com.ian.yttong.network.interceptor.LoggingInterceptor
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 * Description
 * Created by jianhongxu on 2022/5/16
 */
private const val BASE_URL = "http://test.4hand.com.cn/yttong/"
const val key="1a8af1bcf37e4e97b857e6101cc68717"

private val moshi = Moshi.Builder()
    .add(NULL_TO_EMPTY_STRING_ADAPTER)
    .add(KotlinJsonAdapterFactory())
    .build()

object NULL_TO_EMPTY_STRING_ADAPTER {
    @FromJson
    fun fromJson(reader: JsonReader): String {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextString()
        }
        reader.nextNull<Unit>()
        return ""
    }
}

private val client = OkHttpClient.Builder()
    .addInterceptor(LoggingInterceptor())
    .writeTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object  RequestApi{
    val retrofitService:MainService by lazy {
        retrofit.create(MainService::class.java)
    }
}
interface MainService {

    @FormUrlEncoded
    @POST("api/common/getVersionInfo")
    suspend fun getVersionInfo(
        @Field("key") key:String,
        @Field("systemType") systemType:Int
    ): BaseJson<VersionBean>


    @FormUrlEncoded
    @POST("api/news/bannerList")
    suspend fun getBannerList(
        @Field("key") key:String
    ):BaseJson<List<BannerList>>

    @FormUrlEncoded
    @POST("api/common/gradedList")
    suspend fun getGradeList(
        @Field("key") key:String
    ):BaseJson<List<GradeList>>

    @FormUrlEncoded
    @POST("api/common/aboutUs")
    suspend fun getAboutUs(
        @Field("key")key:String
    ):BaseJson<AboutUs>
}