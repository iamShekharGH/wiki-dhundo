package com.iamshekhargh.wikidhundoo.util

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.*

/**
 * Created by <<-- iamShekharGH -->>
 * on 24 June 2021, Thursday
 * at 4:40 PM
 */

fun <ResultType, RequestType, SearchType> networkBoundResource(
    searchThis: () -> SearchType, // text query
    query: (SearchType) -> Flow<ResultType>, //database search
    fetch: suspend (SearchType) -> RequestType, // api call
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query(searchThis()).first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch(searchThis()))
            query(searchThis()).map {
                Resource.Success(it)
            }
        } catch (throwable: Throwable) {
            query(searchThis()).map {
                throwable.printStackTrace()
                Resource.Error("Failed ${throwable.message}", it)
            }
        }
    } else {
        query(searchThis()).map {
            Resource.Success(it)
        }
    }

    emitAll(flow)
}
