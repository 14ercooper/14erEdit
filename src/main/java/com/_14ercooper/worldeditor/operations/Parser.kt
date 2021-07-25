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

object Parser {
    // Store operators that we support
    val operators: MutableMap<String, Node> = HashMap()

    // Add a new operator
    @JvmStatic
    fun addOperator(name: String, node: Node) {
        if (operators.containsKey(name)) {
            return
        }
        operators[name] = node
    }

    // Get the node of an operator that corresponds with a string
    @JvmStatic
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

    @JvmStatic
    fun parseOperation(currentPlayer: CommandSender, op: String): EntryNode? {
        // Here there be parsing magic
        // A massive recursive nightmare
        val splitParts = Arrays.stream(op.split(" ").toTypedArray()).map { s: String ->
            if (s.matches(".*\\[.+=.+].*".toRegex())) s.replace(
                "[()]+".toRegex(),
                ""
            ) else s.replace("[()\\[\\]]+".toRegex(), "")
        }.toList()
        val state = ParserState(currentPlayer, splitParts)
        val rootNode = parsePart(state)

        // This is an error if this is true
        // Probably user error with an invalid operation
        if (rootNode == null) {
            logError("Operation parse failed. Please check your syntax.", currentPlayer, null)
            return null
        }

        // Generate the entry node of the operation
        logDebug("Building entry node from root node") // -----
        return EntryNode(rootNode, state.index + 1)
    }

    // This is the massive recursive nightmare
    @JvmOverloads
    @JvmStatic
    fun parsePart(parserState: ParserState, numberNode: Boolean = false): Node? {
        parserState.index++
        return try {
            // Comments
            var commentTicker = -1
            if (parserState.parts[parserState.index].equals("/*", ignoreCase = true)) {
                while (commentTicker > 0 || commentTicker == -1) {
                    if (commentTicker == -1) {
                        commentTicker++
                    }
                    if (parserState.parts[parserState.index].equals("/*", ignoreCase = true)) {
                        commentTicker++
                    } else if (parserState.parts[parserState.index].equals("*/", ignoreCase = true)) {
                        commentTicker--
                    }
                    logDebug("Skipped " + parserState.parts[parserState.index] + " with comment ticker " + commentTicker)
                    parserState.index++
                }
            }
            val oldIndex = parserState.index
            if (operators.containsKey(parserState.parts[parserState.index])) {
                val n = operators[parserState.parts[parserState.index]]!!.newNode(parserState)
                logDebug(parserState.parts[oldIndex] + " node created: " + n.toString())
                n
            } else {
                var numNode = numberNode
                if (parserState.parts[parserState.index].matches(Regex("\\d+"))) {
                    numNode = true
                }
                if (!numNode) {
                    parserState.index--
                    val strNode = parseStringNode(parserState)
                    val bn = if (strNode.text.isNotBlank())
                        BlockNode().newNode(strNode.text, parserState)
                    else null
                    if (bn != null) {
                        logDebug("Block node created: $bn")
                        bn
                    } else {
                        logDebug("String node created: $strNode") // -----
                        strNode
                    }
                } else {
                    NumberNode().newNode(parserState)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    @JvmStatic
    fun parseNumberNode(parserState: ParserState): NumberNode? {
        logDebug("Number node created") // -----
        return try {
            parsePart(parserState, true) as NumberNode
        } catch (e: Exception) {
            logError("Number expected. Did not find a number.", parserState.currentPlayer, e)
            null
        }
    }

    @JvmStatic
    fun parseRangeNode(parserState: ParserState): RangeNode? {
        parserState.index++
        logDebug("Range node created") // -----
        return try {
            RangeNode().newNode(parserState)
        } catch (e: Exception) {
            logError("Range node expected. Could not create a range node.", parserState.currentPlayer, e)
            null
        }
    }

    @JvmStatic
    fun parseStringNode(parserState: ParserState): StringNode {
        parserState.index++
        logDebug("String node created") // -----
        return try {
            val node = StringNode()
            node.contents = parserState.parts[parserState.index]
            logDebug(node.contents)
            node
        } catch (e: Exception) {
            logError(
                "Ran off end of operator (could not create string node). Are you missing arguments?",
                parserState.currentPlayer, e
            )
            val node = StringNode()
            node.contents = ""
            node
        }
    }
}