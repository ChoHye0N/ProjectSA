package com.github.chohye0n.sa.projectsa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.chohye0n.sa.projectsa.model.Student
import com.github.chohye0n.sa.projectsa.ui.theme.ProjectSATheme
import com.github.chohye0n.sa.projectsa.viewmodel.StudentViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import coil3.compose.AsyncImage
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectSATheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: StudentViewModel = viewModel()) {
    val studentList by viewModel.studentList.observeAsState(emptyList())
    val navController = rememberNavController()

    // 학생 데이터 가져오기
    LaunchedEffect(Unit) {
        viewModel.fetchStudents()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(navController = navController, startDestination = "student_list", modifier = Modifier.padding(innerPadding)) {
            // 학생 목록 화면
            composable("student_list") {
                StudentList(studentList, navController)
            }

            // 학생 세부 정보 화면
            composable(
                route = "student_detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getInt("id")
                val student = studentList.find { it.id == studentId }
                student?.let {
                    StudentDetail(student = it, navController = navController)
                }
            }
        }
    }
}

@Composable
fun StudentList(studentList: List<Student>, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    // 검색된 학생 리스트 필터링
    val filteredStudents = if (searchQuery.isEmpty()) {
        studentList
    } else {
        studentList.filter {
            it.family_name.contains(searchQuery, ignoreCase = true) ||
                    it.personal_name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 검색 입력 필드
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("학생 이름으로 검색") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("이름을 입력하세요") }
        )

        // 검색된 학생 리스트 표시
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(filteredStudents) { student ->
                StudentItem(student = student, onClick = {
                    navController.navigate("student_detail/${student.id}")
                })
            }
        }
    }
}


@Composable
fun StudentItem(student: Student, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(Color(0xFFF0F0F0)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 사진 부분 (AsyncImage 사용)
            AsyncImage(
                model = "https://schaledb.com/images/student/collection/${student.id}.webp",
                contentDescription = "Profile Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            // 텍스트 부분
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "${student.family_name} ${student.personal_name}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${student.school}, ${student.school_year}",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun StudentDetail(student: Student, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE0E0E0))
    ) {
        AsyncImage(
            model = "https://schaledb.com/images/student/portrait/${student.id}.webp",
            contentDescription = "Profile Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(450.dp)
                .clip(RoundedCornerShape(12.dp))
                .padding(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${student.family_name} ${student.personal_name}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = student.school,
                fontSize = 20.sp
            )
            Text(
                text = student.school_year,
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "나이: ${student.character_age}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "생일: ${student.birthday}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "취미: ${student.hobby}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "성우: ${student.character_voice}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "일러스트레이터: ${student.illustrator}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "디자이너: ${student.designer}", fontSize = 18.sp)
        }
    }
}

// TODO: 여러 요소를 통한 검색 기능 추가
@Composable
fun StudentSearchScreen(viewModel: StudentViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSchool by remember { mutableStateOf("") }
    val studentList by viewModel.studentList.observeAsState(emptyList())

    // 학교 목록
    val schools = studentList.map { it.school }.distinct()

    // 학생 검색
    val filteredStudents = when {
        searchQuery.isNotEmpty() -> studentList.filter {
            it.family_name.contains(searchQuery, ignoreCase = true) ||
                    it.personal_name.contains(searchQuery, ignoreCase = true)
        }
        selectedSchool.isNotEmpty() -> studentList.filter { it.school == selectedSchool }
        else -> studentList
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 학생 이름 검색
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("학생 이름으로 검색") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("이름을 입력하세요") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // TODO: 학교 카탈로그 구현
        // 학교 선택을 위한 TextField
        TextField(
            value = selectedSchool,
            onValueChange = { selectedSchool = it },
            label = { Text("학교 선택") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("학교 이름을 입력하세요") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 검색된 학생 리스트
        LazyColumn {
            items(filteredStudents) { student ->
                Text("${student.family_name} ${student.personal_name} (${student.school})")
            }
        }
    }
}

