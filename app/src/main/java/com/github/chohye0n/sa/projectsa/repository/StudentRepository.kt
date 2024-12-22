package com.github.chohye0n.sa.projectsa.repository

import com.github.chohye0n.sa.projectsa.api.StudentApi
import com.github.chohye0n.sa.projectsa.model.Student

class StudentRepository(private val api: StudentApi) {
    suspend fun getStudents(): List<Student> {
        val studentDtos = api.getStudents()
        return studentDtos.map { it.toStudent() } // DTO를 Student로 변환
    }
}