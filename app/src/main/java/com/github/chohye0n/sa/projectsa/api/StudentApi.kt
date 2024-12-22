package com.github.chohye0n.sa.projectsa.api

import com.github.chohye0n.sa.projectsa.model.Student
import retrofit2.http.GET
import retrofit2.http.Query

interface StudentApi {
    @GET("characters")
    suspend fun getStudents(
        @Query("apikey") apiKey: String = StudentApiConfig.API_KEY
    ): List<Student>
}
