package com.github.chohye0n.sa.projectsa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.chohye0n.sa.projectsa.model.Student
import com.github.chohye0n.sa.projectsa.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    // 학생 데이터를 저장할 LiveData
    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>>
        get() = _students

    // 학생 데이터를 API에서 가져오는 함수
    fun fetchStudents() {
        viewModelScope.launch {
            try {
                // Repository에서 학생 데이터를 가져오고, LiveData에 저장
                _students.value = repository.getStudents()
            } catch (e: Exception) {
                // 예외 처리 (네트워크 오류 등)
                _students.value = emptyList()
            }
        }
    }

    // 학생 추가 함수
    fun add(student: Student) {
        val newList = listOf(*_students.value?.toTypedArray() ?: arrayOf(), student)
        _students.value = newList
    }

    // 학생 수를 반환하는 함수
    fun getStudentCount(): Int {
        return _students.value?.size ?: 0
    }

    // 학생 이름으로 찾기
    fun findByName(familyName: String, personalName: String): Student? {
        return _students.value?.find {
            it.FamilyName == familyName && it.PersonalName == personalName
        }
    }

    // 학교별 학생 필터링
    fun filterBySchool(school: String): List<Student> {
        return _students.value?.filter { it.School == school } ?: emptyList()
    }
}
