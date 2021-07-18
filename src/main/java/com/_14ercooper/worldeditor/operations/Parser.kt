package com._14ercooper.worldeditor.operations

import com._14ercooper.worldeditor.main.Main.Companion.logDebug
import com._14ercooper.worldeditor.main.Main.Companion.logError
import com._14ercooper.worldeditor.operations.operators.Node
import com._14ercooper.worldeditor.operations.operators.core.EntryNode
import com._14ercooper.worldeditor.operations.operators.core.NumberNode
import com._14ercooper.worldeditor.operations.operators.core.StringNode
import com._14ercooper.worldeditor.operations.operators.function.RangeNode
import com._14ercooper.worldeditor.operations.operators.world.BlockNode
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.streams.toList

class Parser {
    // This starts as -1 since the first thing the parser does is increment it
    @JvmField
    var index = -1
    private var parts: List<String>? = null

    @JvmField
    var inSetNode = false

    // Store operators
    val operators: MutableMap<String, Node> = HashMap()
    fun addOperator(name: String, node: Node) {
        if (operators.containsKey(name)) {
            return
        }
        operators[name] = node
    }

    fun getOperator(currentPlayer: CommandSender, name: String): Node? {
        if (!operators.containsKey(name)) {
            logError(
                "Operator \"$name\" not found. Please check that you input a valid operator.",
                currentPlayer, null
            )
            return null
        }
        return operators[name]
    }

    fun parseOperation(currentPlayer: CommandSender, op: String): EntryNode? {

        // Here there be parsing magic
        // A massive recursive nightmare
        index = -1
        parts = Arrays.stream(op.split(" ").toTypedArray()).map { s: String ->
                if (s.matches(".*\\[.+=.+].*".toRegex())) s.replace(
                    "[()]+".toRegex(),
                    ""
                ) else s.replace("[()\\[\\]]+".toRegex(), "")
            }.toList()
        val rootNode = parsePart(currentPlayer)

        // This is an error if this is true
        // Probably user error with an invalid operation
        if (rootNode == null) {
            logError("Operation parse failed. Please check your syntax.", currentPlayer, null)
            return null
        }

        // Generate the entry node of the operation
        logDebug("Building entry node from root node") // -----
        return EntryNode(rootNode)
    }

    // This is the massive recursive nightmare
    @JvmOverloads
    fun parsePart(currentPlayer: CommandSender, numberNode: Boolean = false): Node? {
        index++
        return try {
            // Comments
            var commentTicker = -1
            if (parts!![index].equals("/*", ignoreCase = true)) {
                while (commentTicker > 0 || commentTicker == -1) {
                    if (commentTicker == -1) {
                        commentTicker++
                    }
                    if (parts!![index].equals("/*", ignoreCase = true)) {
                        commentTicker++
                    } else if (parts!![index].equals("*/", ignoreCase = true)) {
                        commentTicker--
                    }
                    logDebug("Skipped " + parts!![index] + " with comment ticker " + commentTicker)
                    index++
                }
            }
            val oldIndex = index
            if (index == 0 && !operators.containsKey(parts!![index])) {
                logError(
                    "First node parsed was a string node. This is likely a mistake. Please check that you used a valid operator.",
                    currentPlayer, null
                )
            }
            if (operators.containsKey(parts!![index])) {
                val n = operators[parts!![index]]!!.newNode(currentPlayer)
                logDebug(parts!![oldIndex] + " node created: " + n.toString())
                n
            } else {
                if (!numberNode) {
                    index--
                    val strNode = parseStringNode(currentPlayer)
                    val bn = if (strNode.text.isNotBlank())
                        BlockNode().newNode(strNode.text, currentPlayer)
                    else null
                    if (bn != null) {
                        logDebug("Block node created: $bn")
                        bn
                    } else {
                        logDebug("String node created: $strNode") // -----
                        strNode
                    }
                } else {
                    NumberNode().newNode(currentPlayer)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun parseNumberNode(currentPlayer: CommandSender): NumberNode? {
        logDebug("Number node created") // -----
        return try {
            parsePart(currentPlayer, true) as NumberNode?
        } catch (e: Exception) {
            logError("Number expected. Did not find a number.", currentPlayer, e)
            null
        }
    }

    fun parseRangeNode(currentPlayer : CommandSender): RangeNode? {
        index++
        logDebug("Range node created") // -----
        return try {
            RangeNode().newNode(currentPlayer)
        } catch (e: Exception) {
            logError("Range node expected. Could not create a range node.", currentPlayer, e)
            null
        }
    }

    fun parseStringNode(currentPlayer : CommandSender): StringNode {
        index++
        logDebug("String node created") // -----
        return try {
            val node = StringNode()
            node.contents = parts!![index]
            logDebug(node.contents)
            node
        } catch (e: Exception) {
            logError(
                "Ran off end of operator (could not create string node). Are you missing arguments?",
                currentPlayer, e
            )
            val node = StringNode()
            node.contents = ""
            node
        }
    }
}