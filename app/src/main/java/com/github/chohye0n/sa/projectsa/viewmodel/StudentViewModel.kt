package com.github.chohye0n.sa.projectsa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.chohye0n.sa.projectsa.api.StudentApi
import com.github.chohye0n.sa.projectsa.api.StudentApiConfig
import com.github.chohye0n.sa.projectsa.model.Student
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentViewModel() : ViewModel() {
    private val studentApi: StudentApi
    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>>
        get() = _studentList

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(StudentApiConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        studentApi = retrofit.create(StudentApi::class.java)
        fetchStudents()
    }

    fun fetchStudents() {
        viewModelScope.launch {
            try {
                val response = studentApi.getStudents() // API 호출 (getStudents()는 해당 API 메서드)
                _studentList.value = response
            } catch (e: Exception) {
                Log.e("StudentViewModel", "Error fetching students: ${e.message}")
                _studentList.value = emptyList() // 오류 발생 시 빈 리스트로 설정
            }
        }
    }

    // 학생 추가 함수
    fun add(student: Student) {
        val newList = listOf(*_studentList.value?.toTypedArray() ?: arrayOf(), student)
        _studentList.value = newList
    }

    // 학생 수를 반환하는 함수
    fun getStudentCount(): Int {
        return _studentList.value?.size ?: 0
    }

    // 학생 이름으로 찾기
    fun findByName(familyName: String, personalName: String): Student? {
        return _studentList.value?.find {
            it.family_name == familyName && it.personal_name == personalName
        }
    }

    // 학교별 학생 필터링
    fun filterBySchool(school: String): List<Student> {
        return _studentList.value?.filter { it.school == school } ?: emptyList()
    }
}
