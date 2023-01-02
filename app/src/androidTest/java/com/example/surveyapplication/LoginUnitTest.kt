package com.example.surveyapplication

import com.example.surveyapplication.Model.Admin
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.Student
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class LoginUnitTest {
    @Test
    fun student_signInSuccessful() {
        val student = Student(1, "p2595609", "helloworld")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DatabaseHelper(appContext)

        val result = dbHelper.retrieveStudent(student)

        assertEquals(1, result)
    }

    @Test
    fun student_signInUnsuccessful() {
        val student = Student(1, "p2595601", "helloworld")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DatabaseHelper(appContext)

        val result = dbHelper.retrieveStudent(student)

        assertEquals(0, result)
    }

    @Test
    fun admin_signInSuccessful() {
        val admin = Admin(2, "main", "admin")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DatabaseHelper(appContext)

        val result = dbHelper.retrieveAdmin(admin)

        assertTrue(result)
    }

    @Test
    fun admin_signInUnsuccesful() {
        val admin = Admin(1, "Admin", "admin")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dbHelper = DatabaseHelper(appContext)

        val result = dbHelper.retrieveAdmin(admin)

        assertFalse(result)
    }
}