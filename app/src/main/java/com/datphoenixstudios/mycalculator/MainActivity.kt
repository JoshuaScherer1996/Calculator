package com.datphoenixstudios.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    var lastNumerical: Boolean = false
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View) {
        //takes the button we pressed and assigns it's text to the input tv
        tvInput?.append((view as Button).text)
        //tells us that the last input was a number and not a dot
        lastNumerical = true
        lastDot = false
    }

    //function sets our input back to an empty string
    fun onClear(view: View) {
        tvInput?.text = ""
    }

    //function display a decimal point if we have already put in a number
    fun onDecimalPoint(view: View) {
        if(lastNumerical && !lastDot) {
            tvInput?.append(".")
            lastNumerical = false
            lastDot = true
        }
    }

    //checks if we just put in a number and if we haven't used an operator already. if not we display the operator pressed
    fun onOperator(view: View) {
        tvInput?.text.let {

            if(lastNumerical && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)
                lastNumerical = false
                lastDot = false
            }
        }
    }

    //function to execute our code
    fun onEqual(view: View) {
        //only execute the code if our last input was a number
        if(lastNumerical) {
            //converting text to string and creating prefix
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try {
                //if number starts with minus we ignore the minus, set the prefix to minus and create a new string without the minus
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                //if string includes minus we split it at the minus in two strings
                if (tvValue.contains("-")) {
                    val splitValue = tvValue.split("-")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        //if we had a prefix we add it now in front of string one
                        one = prefix + one
                    }

                    //calculation and translation back to string
                    tvInput?.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())

                    //the principle now repeats for all operations
                } else if (tvValue.contains("+")) {
                    val splitValue = tvValue.split("+")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        //if we had a prefix we add it now in front of string one
                        one = prefix + one
                    }

                    //calculation and translation back to string
                    tvInput?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                } else if (tvValue.contains("/")) {
                    val splitValue = tvValue.split("/")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        //if we had a prefix we add it now in front of string one
                        one = prefix + one
                    }

                    //calculation and translation back to string
                    tvInput?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                } else {
                    val splitValue = tvValue.split("*")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        //if we had a prefix we add it now in front of string one
                        one = prefix + one
                    }

                    //calculation and translation back to string
                    tvInput?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                }
            } catch (e: java.lang.ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    //if our string starts with minus we ignore it, so that we can start with negative numbers. After that we return true if an operator is added
    private fun isOperatorAdded(value: String) : Boolean {
        return if(value.startsWith("-")) {
            false
        } else {
            value.contains("/") ||
                    value.contains("*") ||
                    value.contains("+") ||
                    value.contains("-")
        }
    }

    //function to get rid of the decimal if it is not needed
    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }
        return value
    }
}