package com.ian.yttong.network.interceptor

import android.util.Log
import okhttp3.*
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * Description
 * Created by jianhongxu on 2021/5/19
 */
class LoggingInterceptor : Interceptor {

    private val showMessage = StringBuilder()

    companion object {
        const val TAG = "LoggingInterceptor"
        private const val REQUEST_UP_LINE =
            "┌────── Request ────────────────────────────────────────────────────────────────────────"
        private const val END_LINE =
            "└───────────────────────────────────────────────────────────────────────────────────────"
        private const val RESPONSE_UP_LINE =
            "┌────── Response ───────────────────────────────────────────────────────────────────────"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        val request = oldRequest.newBuilder().addHeader("User-Agent", "Your-App-Name")
            .addHeader("Accept", "application/vnd.yourapi.v1.full+json").build()

        showRequestMessage(request)



        try {
            val currentTime = System.nanoTime()
            val response = chain.proceed(request)
            showResponseMessage(response, System.nanoTime() - currentTime)
            return response
        } catch (e: Exception) {
            throw  e
        }
    }

    private fun showRequestMessage(request: Request) {
        print(REQUEST_UP_LINE)
        print("Method:\t ${request.method}")
        print("Url:\t\t ${request.url}")
        showHeaders(request.headers.toMultimap())


        when (val requestBody = request.body) {
            is FormBody -> {
                requestBody.apply {
                    print("Form submit:")
                    var index = 0
                    while (index < this.size) {
                        print(
                            "\t\t ${requestBody.encodedName(index)} : ${requestBody.encodedValue(index)}"
                        )

                        index++
                    }
                }

            }
            is MultipartBody -> {
                print("Multipart submit:")
               //TODO add detail of the Multipart
                Log.e(TAG, "MultipartBody " + requestBody.toString())
            }
            is RequestBody -> {
                requestBody.apply {
                    print("contentType: ")
                    print("\t\t${requestBody.contentType()}")

                    print("content: ")
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    print("\t\t${buffer.readUtf8()}")
                }

            }
        }
        print(END_LINE)
    }

    private fun showResponseMessage(response: Response, time: Long) {
        print(RESPONSE_UP_LINE)
        print("Request URL:\t ${response.request.url}")
        print("${response.request.method} \t\t- Code: ${response.code} -\t Received on: ${TimeUnit.NANOSECONDS.toMillis(time)} ms")

        print("Successful:\t${response.isSuccessful}")

        showHeaders(response.headers.toMultimap())

        print("Content:")


        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()


        val source = responseBody.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer

        val contentType = responseBody.contentType()
        val charset: Charset = contentType?.charset(StandardCharsets.UTF_8)
            ?: StandardCharsets.UTF_8

        if (contentLength != 0L) {

            val body = getJsonString(
                buffer.clone().readString(charset)
            ) //得到body的buffer 克隆，这样就可以避免只能执行一次string()

            body.split(System.getProperty("line.separator")!!).toTypedArray().forEach {
                print("$it")
            }
        }

        print(END_LINE)
    }


    private fun showHeaders(multimap: Map<String, List<String>>) {
        print("Headers:")
        multimap.map {
//            print("\t\t $it ")
            print("\t ${it.key}:")
            it.value.toTypedArray().forEach { value ->

                getJsonString(value).split(System.getProperty("line.separator")!!).toTypedArray()
                    .forEach { str ->
                        print("\t\t $str")
                    }

            }

        }
    }

    /**
     * String convert json String
     */
    private fun getJsonString(msg: String): String {
        return try {
            when {
                msg.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(3)
                }
                msg.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(3)
                }
                else -> {
                    msg
                }
            }
        } catch (e: JSONException) {
            msg
        } catch (e1: OutOfMemoryError) {
            "Output omitted because of Object size."
        }
    }

    /**
     * print them finally
     */
    private fun print(str: String) {
        if (showMessage.isNotEmpty()) showMessage.clear()
        if (str != REQUEST_UP_LINE && str != RESPONSE_UP_LINE && str != END_LINE)
            showMessage.append("│ ")
        showMessage.append(str)
        Log.d(TAG, showMessage.toString())
    }
}