package com.example.timezdone.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class TimeZoneResponse(
    val datetime: String,
    val timezone: String
)

const val BASE_URL = "http://worldtimeapi.org/api/"

interface TimeZoneApi {
    @GET("timezone/{region}/{city}")
    suspend fun getTimeZone(
        @Path("region") region: String,
        @Path("city") city: String
    ): TimeZoneResponse

    companion object {
        private var instance: TimeZoneApi? = null

        fun getInstance(): TimeZoneApi {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(TimeZoneApi::class.java)
            }
            return instance!!
        }
    }
}
