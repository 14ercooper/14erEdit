package com._14ercooper.megaserver

import java.util.regex.Pattern

object UserInput {
    // Get an input in a numeric range
    fun numberRange(min: Int, max: Int): Int {
        return try {
            val i = System.console().readLine().toInt()
            if (i in min..max) i else {
                print("Input outside of valid range.\n> ")
                numberRange(min, max)
            }
        } catch (e: Exception) {
            print("Input invalid.\n  > ")
            numberRange(min, max)
        }
    }

    fun patternMatch(regex: String): String {
        return try {
            val s = System.console().readLine()
            val p = Pattern.compile(regex)
            val m = p.matcher(s)
            if (m.find()) s else {
                print("Input does not match required pattern.\n> ")
                patternMatch(regex)
            }
        } catch (e: Exception) {
            print("Input invalid.\n> ")
            patternMatch(regex)
        }
    }

    fun fromList(choices: List<String>): String {
        var i = 1
        for (s in choices) {
            println("\t$i) $s")
            i++
        }
        print("> ")
        return choices[numberRange(1, i) - 1]
    }
}