package com.fbatista.regex_testing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private var isFieldValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setListeners()

    }

    private fun setListeners() {
        testBtn.setOnClickListener { validate() }
    }

    private fun validate() {
        val input = regexEt.text


        val regex = ("^(?!.*([0-9])\1{2,}")

        isFieldValid = Pattern.matches(regex, input)

        when (isFieldValid) {
            true -> Toast.makeText(this, "VALIDO", Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(this, "INVALIDO", Toast.LENGTH_SHORT).show()
        }
    }
}
