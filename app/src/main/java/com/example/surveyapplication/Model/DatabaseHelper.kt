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
    private val StudentSurveyStudent_Id = "StudentId"
    private val StudentSurveyQuestion_Id = "QuestionId"
    private val StudentSurveyAnswerKey_Id = "AnswerId"
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
        val cv: ContentValues = ContentValues()

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

    fun retrieveStudent(student: Student): Boolean {
        var found = false

        val db: SQLiteDatabase = this.readableDatabase

        val username = student.username
        val password = student.password

        val sqlQuery = "SELECT * FROM $StudentTableName " +
                "WHERE $Student_Login = ? " +
                "AND $Student_Password = ?"

        val arrayParameters = arrayOf(username,password)

        val cursor: Cursor = db.rawQuery(sqlQuery, arrayParameters)

        if (cursor.moveToFirst()) {
            found = true
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

    fun storeAllAnswers(answerList: ArrayList<Answer>, studentId: Int, questionId: Array<Int>) {
        if (answerList.size == 10) {
            for (i in 1..answerList.size) {
                val db: SQLiteDatabase = this.writableDatabase
                val cv: ContentValues = ContentValues()

                cv.put(StudentSurveyStudent_Id, studentId)
                cv.put(StudentSurveyQuestion_Id, questionId[i])
                cv.put(StudentSurveyAnswerKey_Id, answerList[i].id)

                db.insert(StudentSurveyAnswerTableName, null, cv)

                db.close()
            }
        }
    }
}