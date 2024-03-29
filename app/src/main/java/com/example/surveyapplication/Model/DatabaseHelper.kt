package com.example.surveyapplication.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable

private val DatabaseName = "SurveyDatabase.db"
private val ver: Int = 1

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,DatabaseName,null,ver),
    Serializable {

    /* Student table */
    private val StudentTableName = "Student"
    private val Student_Id = "StudentId"
    private val Student_Login = "StudentLogin"
    private val Student_Password = "Password"
    /*===========================================*/

    /* Admin table */
    private val AdminTableName = "Admin"
    private val Admin_Id = "AdminId"
    private val Admin_Login = "AdminLogin"
    private val Admin_Password = "Password"
    /*===========================================*/

    /* Survey table*/
    private val SurveyTableName = "Survey"
    private val Survey_Id = "SurveyId"
    private val Survey_Title = "SurveyTitle"
    private val Survey_StarDate = "StartDate"
    private val Survey_EndDate = "EndDate"
    /*===========================================*/

    /* Question table */
    private val QuestionTableName = "Question"
    private val Question_Id = "QuestionId"
    private val QuestionSurvey_Id = "SurveyId"
    private val Question_Text = "QuestionText"
    /*===========================================*/

    /* Answer table */
    private val AnswerTableName = "Answer"
    private val Answer_Id = "AnswerId"
    private val Answer_Text = "AnswerText"
    /*===========================================*/

    /* Student Survey Answer table */
    private val StudentSurveyAnswerTableName = "StudentSurveyAnswer"
    private val StudentSurveyAnswer_Id = "StudentSurveyAnswerId"
    private val StudentSurveyAnswer_StudentId = "StudentId"
    private val StudentSurveyAnswer_SurveyId = "SurveyId"
    private val StudentSurveyAnswer_QuestionId = "QuestionId"
    private val StudentSurveyAnswer_AnswerId = "AnswerId"
    /*===========================================*/

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val sqlQuery: String = "CREATE TABLE IF NOT EXISTS " + StudentTableName + " ( " +
                    Student_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Student_Login +
                    " TEXT NOT NULL UNIQUE, " + Student_Password + " TEXT NOT NULL" + " ) "

            db?.execSQL((sqlQuery))
        }
        catch (e: SQLiteException) {}
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    private fun checkExistence(student: Student): Int {

        val db: SQLiteDatabase

        // if database fails, return -2
        try {
            db = this.readableDatabase
        }
        catch (e:SQLiteException) {
            return -2
        }

        val loginName = student.username

        val sqlQuery = "SELECT * FROM $StudentTableName " +
                        "WHERE $Student_Login = ?"
        val studentArray = arrayOf(loginName)

        val cursor: Cursor = db.rawQuery(sqlQuery, studentArray)

        // if student is found, return -3
        if(cursor.moveToFirst()) {
            val n = cursor.getInt(0)
            cursor.close()
            db.close()
            return -3
        }

        // if student not found, proceed to add new user
        cursor.close()
        db.close()
        return 0
    }

    fun addStudent(student: Student): Int {
        val existingStudent = checkExistence(student)

        if(existingStudent < 0) {
            return existingStudent
        }

        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        cv.put(Student_Login, student.username)
        cv.put(Student_Password, student.password)

        val success = db.insert(StudentTableName,null,cv)

        db.close()

        if(success.toInt() == -1) {
            return success.toInt()
        } else {
            return 1
        }
    }

    fun retrieveStudent(student: Student): Int {
        var found = 0

        val db: SQLiteDatabase = this.readableDatabase

        val username = student.username
        val password = student.password

        val sqlQuery = "SELECT * FROM $StudentTableName " +
                "WHERE $Student_Login = ? " +
                "AND $Student_Password = ?"

        val arrayParameters = arrayOf(username,password)

        val cursor: Cursor = db.rawQuery(sqlQuery, arrayParameters)

        if (cursor.moveToFirst()) {
            found = cursor.getInt(0)
            cursor.close()
            db.close()
        }
        cursor.close()
        db.close()
        return found;
    }

    fun retrieveAdmin(admin: Admin): Boolean {
        var found = false

        val db: SQLiteDatabase = this.readableDatabase

        val adminLogin = admin.username
        val password = admin.password

        val sqlQuery = "SELECT * FROM $AdminTableName " +
                "WHERE $Admin_Login = ? " +
                "AND $Admin_Password = ?"

        val arrayParameters = arrayOf(adminLogin, password)

        val cursor: Cursor = db.rawQuery(sqlQuery, arrayParameters)

        if (cursor.moveToFirst()) {
            found = true
            cursor.close()
            db.close()
        }

        cursor.close()
        db.close()
        return found
    }


    fun retrieveAllSurveys(): ArrayList<Survey> {
        val surveyList = ArrayList<Survey>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlQuery = "SELECT * FROM $SurveyTableName"

        val cursor: Cursor = db.rawQuery(sqlQuery, null)

        if(cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(0)
                val surveyTitle: String = cursor.getString(1)
                val surveyStartDate: String = cursor.getString(2)
                val surveyEndDate: String = cursor.getString(3)
                val s = Survey(id, surveyTitle, surveyStartDate, surveyEndDate)
                surveyList.add(s)
            } while (cursor.moveToNext())

            cursor.close()
            db.close()
        }
        return surveyList
    }

    fun checkCompletedSurveys(studentId: Int): ArrayList<Int> {
        val completedSurveys = ArrayList<Int>()
        val db: SQLiteDatabase = this.readableDatabase

        val sqlQuery = "SELECT $StudentSurveyAnswer_SurveyId " +
                        "FROM $StudentSurveyAnswerTableName " +
                        "WHERE $StudentSurveyAnswer_StudentId = $studentId"

        val cursor : Cursor = db.rawQuery(sqlQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val surveyId : Int = cursor.getInt(0)
                completedSurveys.add(surveyId)
            } while (cursor.moveToNext())

            cursor.close()
            db.close()
        }
        return completedSurveys
    }

    fun retrieveAllQuestions(surveyId: Int): ArrayList<Question> {
        val questionList = ArrayList<Question>()

        val db: SQLiteDatabase = this.readableDatabase
        val sqlQuery = "SELECT * FROM $QuestionTableName " +
                "WHERE $QuestionSurvey_Id = $surveyId"

        val cursor: Cursor = db.rawQuery(sqlQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(0)
                val surveyId: Int = cursor.getInt(1)
                val questionText: String = cursor.getString(2)

                val q = Question(id, surveyId, questionText)
                questionList.add(q)
            } while (cursor.moveToNext())

            cursor.close()
            db.close()
        }

        return questionList
    }

    fun retrieveAllAnswers(): ArrayList<Answer> {
        val answerList = ArrayList<Answer>()

        val db: SQLiteDatabase = this.readableDatabase
        val sqlQuery = "SELECT * FROM $AnswerTableName"

        val cursor: Cursor = db.rawQuery(sqlQuery, null)

        if (cursor.moveToNext()) {
            do {
                val id = cursor.getInt(0)
                val answerText = cursor.getString(1)

                val a = Answer(id, answerText)
                answerList.add(a)
            } while (cursor.moveToNext())

            cursor.close()
            db.close()
        }

        return answerList
    }

    fun storeAllAnswers(answers: ArrayList<StudentSurveyAnswer>) {
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        answers.forEach { answer ->
            cv.put(StudentSurveyAnswer_StudentId, answer.studentId)
            cv.put(StudentSurveyAnswer_SurveyId, answer.surveyId)
            cv.put(StudentSurveyAnswer_QuestionId, answer.questionId)
            cv.put(StudentSurveyAnswer_AnswerId, answer.answerId)

            db.insert(StudentSurveyAnswerTableName, null, cv)
        }

        db.close()
    }

    fun addNewSurvey(survey: Survey) {
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        cv.put(Survey_Title, survey.title)
        cv.put(Survey_StarDate, survey.startDate)
        cv.put(Survey_EndDate, survey.endDate)

        db.insert(SurveyTableName, null, cv)

        db.close()
    }

    fun updateSurvey(survey: Survey) {
        val db: SQLiteDatabase = this.writableDatabase
        val sqlQuery = "UPDATE $SurveyTableName " +
                        "SET $Survey_Title=${survey.title} " +
                        "WHERE $Survey_Id = ${survey.id}"
        val cv = ContentValues()

        cv.put(Survey_Title, survey.title)
        cv.put(Survey_StarDate, survey.startDate)
        cv.put(Survey_EndDate, survey.endDate)

        db.update(SurveyTableName, cv,"$Survey_Id = ${survey.id}", null)
        db.close()
    }

    fun storeAllQuestions(questions: ArrayList<Question>) {
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        questions.forEach { question ->
            cv.put(QuestionSurvey_Id, question.surveyId)
            cv.put(Question_Text, question.questionText)

            db.insert(QuestionTableName, null, cv)
        }
        db.close()
    }

    fun updateQuestions(questions: ArrayList<Question>) {
        val db: SQLiteDatabase = this.writableDatabase
        val sqlQuery = "UPDATE $QuestionTableName SET $Question_Text = ? WHERE $Question_Id = ?"

        questions.forEach { question ->
            val cv = ContentValues()
            cv.put(Question_Text, question.questionText)
            db.update(QuestionTableName, cv, "$Question_Id='${question.id}'", null)
        }
        db.close()
    }

    fun retrieveLastSurveyId(): Int {
        var lastSurveyId = 0
        val db: SQLiteDatabase = this.readableDatabase
        val sqlQuery = "SELECT max($Survey_Id) from $SurveyTableName"

        val cursor: Cursor = db.rawQuery(sqlQuery, null)

        if (cursor.moveToFirst()) {
            lastSurveyId = cursor.getInt(0)
        }
        cursor.close()
        db.close()

        return lastSurveyId
    }

    fun listParticipants(surveyId: Int): Int {
        var allParticipants = 0

        val db: SQLiteDatabase = this.readableDatabase
        val sqlQuery = "SELECT DISTINCT $StudentSurveyAnswer_StudentId FROM $StudentSurveyAnswerTableName " +
                "WHERE $StudentSurveyAnswer_SurveyId=$surveyId"

        val cursor: Cursor = db.rawQuery(sqlQuery, null)

        if (cursor.moveToFirst()) {
            do {
                allParticipants++
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return allParticipants
    }

    fun surveyAnswers(questionId: Int, answerId: Int): Double {
        var total = 0.0

        val db: SQLiteDatabase = this.readableDatabase
        val query = "SELECT $StudentSurveyAnswer_Id FROM $StudentSurveyAnswerTableName " +
                "WHERE $StudentSurveyAnswer_QuestionId=$questionId " +
                "AND $StudentSurveyAnswer_AnswerId=$answerId"

        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                total++
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return total
    }
}