package com.github.chohye0n.sa.projectsa.api

import com.github.chohye0n.sa.projectsa.model.StudentDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StudentApi {
    @GET("students")
    suspend fun getStudents(
        @Query("apikey") apiKey: String = StudentApiConfig.API_KEY
    ): List<StudentDto> // DTO로 반환받음
}
