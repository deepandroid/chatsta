package com.tridhya.basesetupnew.Service

import android.content.Context
import com.tridhya.basesetupnew.Service.NetworkConstants.ApiCode.SESSION_EXCEPTION
import com.tridhya.basesetupnew.Service.NetworkConstants.ApiCode.SESSION_TIMEOUT_EXCEPTION

import com.tridhya.basesetupnew.database.BrifecaseDataBase
import com.tridhya.basesetupnew.utils.Constant

import com.tridhya.basesetupnew.utils.Constant.isNetWork
import com.tridhya.basesetupnew.utils.PrefUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import java.net.SocketException
import java.net.SocketTimeoutException

class NetworkStatusInterceptor(
    private val context: Context,
    val brifecaseDataBase: BrifecaseDataBase,
    val sharedPreferences: PrefUtils
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return onOnIntercept(chain)
    }

    private fun onOnIntercept(chain: Interceptor.Chain): Response = runBlocking {
        var sendResponse: Response
        val request = chain.request()
        val urlLink = request.url.toUrl().toString()

        val apiName = try {
            if (urlLink.contains("/Login/"))
                "Login"
            else {
                val startIndex = urlLink.lastIndexOf('/') + 1
                val endIndex = urlLink.indexOf('.', startIndex)
                if (urlLink.contains(".php")) urlLink.substring(startIndex, endIndex)
                else urlLink
            }
        } catch (e: Exception) {
            "No Name"
        }

        try {
            val response = chain.proceed(chain.request())
            val responseBody = response.peekBody(2048)
            sendResponse = try {
                val convertedBody = responseBody.string()
                if (convertedBody.isNotEmpty()) {
                    val firstCharOfResponseBody = convertedBody[0].toString()
                    if (firstCharOfResponseBody == "[" || firstCharOfResponseBody == "{" || firstCharOfResponseBody == "\"") {
                        response
                    } else {
                        generateDummyResponse(chain, "$apiName-->$convertedBody")
                    }
                } else {
                    generateDummyResponse(chain, "$apiName-->$convertedBody")
                }
            } catch (exception: Exception) {

                val msg =
                    if (isNetWork(context))
                        "$apiName-->" + exception.message.toString() else NetworkConstants.ErrorMsg.NO_NETWORK
                generateDummyResponse(chain, msg)
            }
        } catch (exception: SocketTimeoutException) {

            val msg =
                if (isNetWork(context)) "$apiName-->" + exception.message.toString() else NetworkConstants.ErrorMsg.NO_NETWORK
            sendResponse = generateDummyResponse(
                chain, msg, SESSION_TIMEOUT_EXCEPTION
            )
        } catch (exception: SocketException) {

            val msg =
                if (isNetWork(context)) "$apiName-->" + exception.message.toString() else NetworkConstants.ErrorMsg.NO_NETWORK
            sendResponse = generateDummyResponse(
                chain, msg, SESSION_EXCEPTION
            )
        } catch (exception: Exception) {

            val msg =
                if (isNetWork(context)) "$apiName-->" + exception.message.toString() else NetworkConstants.ErrorMsg.NO_NETWORK
            sendResponse = generateDummyResponse(chain, msg)
        }
        //firebaseDatabase(context, sendResponse, request, brifecaseDataBase, sharedPreferences)

        sendResponse
    }

    private fun generateDummyResponse(
        chain: Interceptor.Chain,
        exception: String,
        code: Int = NetworkConstants.ApiCode.INTERNAL_ERROR_CODE
    ): Response {

        val builder: Response.Builder = Response.Builder()
        builder.code(code)
        builder.request(chain.request())
        builder.protocol(Protocol.HTTP_2)
        builder.message(exception)
        return builder.build()
    }
}