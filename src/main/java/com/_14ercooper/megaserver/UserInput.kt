/**
 * This file is part of 14erEdit.
 * 
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

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