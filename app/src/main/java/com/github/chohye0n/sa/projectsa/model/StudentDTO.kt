package com.github.chohye0n.sa.projectsa.model

data class StudentDto(
    val Id: Int,
    val School: String,
    val Club: String,
    val FamilyName: String,
    val PersonalName: String,
    val SchoolYear: String,
    val CharacterAge: String,
    val Birthday: String,
    val CharacterSSRNew: String,
    val ProfileIntroduction: String,
    val Hobby: String,
    val CharacterVoice: String,
    val Illustrator: String,
    val Designer: String,
    val CharHeightMetric: String
) {
    // DTO를 Student로 변환
    fun toStudent(): Student {
        return Student(
            Id = Id,
            School = School,
            Club = Club,
            FamilyName = FamilyName,
            PersonalName = PersonalName,
            SchoolYear = SchoolYear,
            CharacterAge = CharacterAge,
            Birthday = Birthday,
            CharacterSSRNew = CharacterSSRNew,
            ProfileIntroduction = ProfileIntroduction,
            Hobby = Hobby,
            CharacterVoice = CharacterVoice,
            Illustrator = Illustrator,
            Designer = Designer,
            CharHeightMetric = CharHeightMetric
        )
    }
}

