package com.fbatista.regex_testing

import java.lang.StringBuilder
import java.util.regex.Pattern

enum class ValidationRequisits {
    /**
     * Validation expects at least 1 number
     */
    DIGIT,
    /**
     * Validation expects at least 1 letter (either lower or upper case)
     */
    UPPER_CASE_LETTER,
    /**
     * Validation expects at least 1 number and 1 upper case letter
     */
    DIGIT_AND_UPPER_CASE_LETTER,
    /**
     * Validation expects at least 1 digit, 1 upper case letter, 1 lower case letter and 1 special char
     */
    DIGIT_AND_UPPER_CASE_LETTER_AND_SPECIAL_CHAR
}

class PasswordValidationHelper(val minimumLength: Int, val requisits: ValidationRequisits) {

    companion object {
        //The constant values below will be used to construct a basic regex code that will match strings accordingly to the rules that will be set
        //Strings that will construct the regex code
        const val start = "(^"
        const val end = "$)"

        //Code to make regex match numbers
        const val digitRule = "(?=.*[0-9])"

        //Code to make regex match upper case letters
        const val upperCaseLetterRule = "(?=.*[A-Z])"

        //code to make regex match special chars
        const val specialCharRule = "(?=.*[!@#\\$%\\^\\&\\*])"
    }

    //Properties that will be used to generate the regex code
    private var validationRulesList = mutableListOf<String>()
    private var maximumLength = ""
    private var charsQuantity = ".{$minimumLength,$maximumLength}"
    private val wordsBlackList = mutableListOf<String>()

    //Properties that will be used to validate extra rules
    private var isSequenceInvalid = false
    private var isRepetitionsInvalid = false


    init {
        //Sets the requirements to the password be valid
        when (requisits) {
            ValidationRequisits.DIGIT -> {
                validationRulesList.add(digitRule)
            }
            ValidationRequisits.UPPER_CASE_LETTER -> {
                validationRulesList.add(upperCaseLetterRule)
            }
            ValidationRequisits.DIGIT_AND_UPPER_CASE_LETTER -> {
                validationRulesList.add(digitRule)
                validationRulesList.add(upperCaseLetterRule)
            }
            ValidationRequisits.DIGIT_AND_UPPER_CASE_LETTER_AND_SPECIAL_CHAR -> {
                validationRulesList.add(digitRule)
                validationRulesList.add(upperCaseLetterRule)
                validationRulesList.add(specialCharRule)
            }
        }
    }

    private fun getRegex(): String {
        val regex = StringBuilder()
        regex.append(start)
        validationRulesList.forEach {
            regex.append(it)
        }
        regex.append(charsQuantity)
        regex.append(end)

        return regex.toString()
    }

    private fun containsBlackListWord(input: String): Boolean {

        //Variable that will be set as true when the input contains some of the blacklist words
        var inputContainsBlackListWord = false


        //Iterates the blacklist
        wordsBlackList.forEach {
            if (input.contains(it.toRegex())) {
                inputContainsBlackListWord = true
            }
        }

        return inputContainsBlackListWord
    }


    private fun containsDigitRepetition(input: String): Boolean {
        val regex = "000|111|222|333|444|555|666|777|888|999".toRegex()
        return input.contains(regex)
    }

    private fun getNumberSequenceRegex(initialNumber: Int, sequenceSize: Int): String {
        val completeSequence = StringBuilder()
        val endNumber = initialNumber + sequenceSize

        //Create regex for ascending sequence
        /*
        For each number from the initial number to the endNumber (that's calculated using the sequenceSize), a sequence will be created, this sequence contains all numbers between the start and end numbers
        Each sequence will be separated by a " | "
        ex: startNumber = 0 and sequenceSize = 6 -> 012345|123456|234567...
         */
        for (i in initialNumber until endNumber) {
            //This sequence will store the current sequence that's being generated ( "sequence"|"sequence"|"sequence" ... )
            val sequence = StringBuilder()

            //This variable will be used as a control that's will be set to true when the number that will be set in the sequence becomes grater than 9.
            //This is necessary so that a number like "10" or "11" will not be set in the sequence
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
            completeSequence.append("$sequence|")
            completeSequence.append("${sequence.reverse()}|")
        }

        //Removes the last "|" from the string so that it becomes a valid regex
        val lastIndex = completeSequence.lastIndex
        completeSequence.deleteCharAt(lastIndex)

        return completeSequence.toString()
    }

    private fun containsDigitSequence(input: String): Boolean {
        return input.contains(getNumberSequenceRegex(0, minimumLength).toRegex())
    }

    /**
     * This method returns whether the password is valid or not
     * @param input The password that has to be validated
     */
    fun isValid(input: String): Boolean {

        if (!containsBlackListWord(input)) {
            if (!containsDigitRepetition(input)) {
                if (!containsDigitSequence(input)) {
                    return Pattern.matches(getRegex(), input)
                }
            }
        }

        return false
    }

    /**
     * This method sets a maximum length that the password has to match to be valid
     */
    fun setMaximumLength(length: Int) {
        maximumLength = length.toString()
    }

    /**
     * This method will sets whether the validation has to consider sequences as valid or not (Ex: When true, the input "123456" won't be valid)
     * @param isNotValid When true, this rule will be considered
     */
    fun setSequencesAsNotValid(isNotValid: Boolean) {
        isSequenceInvalid = isNotValid
    }

    /**
     * This method will sets whether the validation has to consider repititions of digits as valid or not (Ex: When true, and the repetitionsAllowed is 2, the input "2221234" won't be valid because "222" is repeated 3x in a row.)
     */
    fun setRepeteadDigitsNotValid(isNotValid: Boolean, repetitionsAllowed: Int) {
        isRepetitionsInvalid = isNotValid
    }

    /**
     * This method will sets a string into a "blacklist" that will be used in the validation. To the input be valid, it cannot contain any of the texts inside the blacklist
     * @param text The text that the input cannot contain to be valid
     */
    fun setNotAllowedText(text: String) {
        wordsBlackList.add(text)
    }
}
