package com.opiumfive.coinapp.domain.network.mapper

import com.opiumfive.coinapp.data.serverModel.apiResponse.ApiResponse
import com.opiumfive.coinapp.data.uiModel.data.Data

object ApiResponseDataMapper {
    fun <T> map(apiResponse: ApiResponse<T>?): Data<T> {
        if (apiResponse?.body != null && apiResponse.code == 200) {
            return Data.success(apiResponse.body, apiResponse.throwable)
        }
        return Data.errorThrowable(apiResponse?.throwable, apiResponse?.code)
    }
}