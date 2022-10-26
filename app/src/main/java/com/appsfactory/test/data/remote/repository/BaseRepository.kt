package com.appsfactory.test.data.remote.repository

import android.app.Application
import com.appsfactory.test.R
import com.appsfactory.test.domain.util.Result
import com.appsfactory.test.utils.logError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

abstract class BaseRepository {

    @Inject
    lateinit var app: Application

    /**
     * @author Zaid
     * @param T -> DTO
     * @param V -> Domain
     * @param request represents network call and return Response<T> where T is Dto
     * @param response represents extension function on T to perform mapping for domain model
     * and return V which is domain model for Dto
     * @return flow of Result<V> where V is domain model.
     **/

    suspend inline fun <reified T : Any, reified V : Any> makeRequest(
        crossinline request: suspend () -> Response<T>,
        crossinline response: suspend T.() -> V
    ): Flow<Result<V>> = flow {
        try {
            val result = request.invoke()
            if (result.isSuccessful) {
                val dto = result.body()!!
                emit(Result.Success(dto.response()))
            } else {
                emit(Result.Error(app.getString(R.string.api_request_failed)))
            }
        } catch (e: IOException) {
            emit(Result.Error(app.getString(R.string.internet_connection_error)))
        } catch (e: HttpException) {
            emit(Result.Error(app.getString(R.string.api_request_failed)))
        }
    }.catch { e ->
        logError<BaseRepository>(msg = "(${T::class.java}, ${V::class.java}) -> ${e.message}")
        emit(Result.Error(app.getString(R.string.unknown_error_msg)))
    }
}