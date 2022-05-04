package com.andback.pocketfridge.data.api

import com.andback.pocketfridge.BuildConfig
import com.andback.pocketfridge.data.model.BarcodeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BarcodeApi {
    @GET("{serviceKey}/C005/{dataType}/{startIdx}/{endIdx}")
    fun getProductFromBarcode(
        @Path("serviceKey") serviceKey: String = BuildConfig.BARCODE_SERVICE_KEY,
        @Path("dataType") dataType: String = "json",
        @Path("startIdx") startIdx: String = "1",
        @Path("endIdx") endIdx: String = "1",
        @Query("BAR_CD") barcode: String
    ): Single<BarcodeResponse>
}