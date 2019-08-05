package com.fbatista.regex_testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private var isFieldValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regexEt.setText("Aa1!")
        setListeners()

    }

    private fun setListeners() {
        testBtn.setOnClickListener { validate() }
    }

    private fun validate() {
        val validator = PasswordValidationHelper(6, ValidationRequisits.DIGIT_AND_UPPER_CASE_LETTER_AND_SPECIAL_CHAR)
        validator.setNotAllowedText("08111995")
        validator.setMaximumLength(6)

        isFieldValid = validator.isValid(regexEt.text.toString())
//        isFieldValid = validate(regexEt.text.toString())

        when (isFieldValid) {
            true -> Toast.makeText(this, "VALIDO", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(this, "INVALIDO", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validate(input: String): Boolean {
        val input = regexEt.text.toString()

        val birthday = "08111995"
        val sequence = getNumberSequenceRegex(0, 6, true)
        if (!checkConsecutiveNumbers(input)) {
            val beginning = "(^"
            val minimumLength = "8"
            val maximumLength = ""
            val charsQuantity = ".{$minimumLength,$maximumLength}"
            val numbersRule ="(?=.*[0-9])"
            val lowerCaseLetterRule = "(?=.*[a-z])"
            val upperCaseLetterRule = "(?=.*[A-Z])"
            val specialCharRule = "(?=.*[!@#\\$%\\^\\&\\*])"
            val exceptRules = ""
            val end = "$charsQuantity$)"
            val regex = "$beginning$exceptRules$numbersRule$lowerCaseLetterRule$upperCaseLetterRule$specialCharRule$end"
            isFieldValid = Pattern.matches(regex, input)
        } else {
            isFieldValid = false
        }

        return isFieldValid
    }

    fun checkConsecutiveNumbers(input: String): Boolean {
        val regex = "000|111|222|333|444|555|666|777|888|999".toRegex()
        return input.contains(regex)
    }

    fun getNumberSequenceRegex(initialNumber: Int, sequenceSize: Int,isDescedent: Boolean): String {
        val sequence = StringBuilder()
        val endNumber = initialNumber + sequenceSize

        for (i in initialNumber until endNumber) {
            var breakControl = false
            val end = i + sequenceSize
            for (j in i until end) {
                if (j > 9) {
                    breakControl = true
                    break
                }
                sequence.append(j.toString())
            }
            if (breakControl) {
                break
            }
            sequence.append("|")
        }

        return sequence.toString()
    }
}
