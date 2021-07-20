package com._14ercooper.worldeditor.main

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.blockiterator.IteratorLoader
import com._14ercooper.worldeditor.brush.BrushListener
import com._14ercooper.worldeditor.brush.BrushLoader
import com._14ercooper.worldeditor.commands.*
import com._14ercooper.worldeditor.compat.WorldEditCompat
import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.macros.MacroLoader
import com._14ercooper.worldeditor.operations.OperatorLoader
import com._14ercooper.worldeditor.operations.ParserState
import com._14ercooper.worldeditor.operations.operators.loop.WhileNode
import com._14ercooper.worldeditor.player.PlayerManager
import com._14ercooper.worldeditor.scripts.CraftscriptLoader
import com._14ercooper.worldeditor.selection.SelectionWandListener
import com._14ercooper.worldeditor.undo.UndoSystem
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

class Main : JavaPlugin() {
    override fun onDisable() {
        // Flush the undo system to disk (make sure nothing's still in RAM)
        UndoSystem.flush()
    }

    override fun onEnable() {
        // Check Java 11 (ensure that the user didn't somehow bypass the earlier class
        // version check)
        val javaVersion = System.getProperty("java.class.version").toFloat()
        if (javaVersion < 55) {
            println(
                """
    Java 11 or never is required to run this server. https://adoptopenjdk.net is a good place to find Java 11.
    The server will now shut down.
    """.trimIndent()
            )
            try {
                Thread.sleep(15000)
                Bukkit.shutdown()
            } catch (e1: InterruptedException) {
                Bukkit.shutdown()
            }
        }

        // Set the plugin
        plugin = this

        // Load version numbers
        val ver = server.version.split("MC: ").toTypedArray()[1]
        ver.split(".").toTypedArray()[1].replace("[^\\d.]".toRegex(), "").toInt().also { majorVer = it }
        try {
            ver.split(".").toTypedArray()[2].replace("[^\\d.]".toRegex(), "").toInt().also { minorVer = it }
        }
        catch (e : Exception) {
            minorVer = 0
        }
        println("Using version " + server.version + ": " + majorVer + "/" + minorVer)

        // Create folders as needed
        try {
            Files.createDirectories(Paths.get("plugins/14erEdit/schematics"))
            Files.createDirectories(Paths.get("plugins/14erEdit/templates"))
            Files.createDirectories(Paths.get("plugins/14erEdit/multibrushes"))
            Files.createDirectories(Paths.get("plugins/14erEdit/functions"))
            Files.createDirectories(Paths.get("plugins/14erEdit/undo"))
        } catch (e: IOException) {
            logDebug("Error creating directory structure. 14erEdit may not work properly until this is resolved.")
        }

        // Register commands with the server
        getCommand("fx")!!.setExecutor(CommandFx())
        getCommand("fx")!!.tabCompleter = CommandFx.TabComplete()
        val undoCmd = CommandUndo()
        getCommand("un")!!.setExecutor(undoCmd)
        getCommand("un")!!.tabCompleter = CommandUndo.TabComplete()
        getCommand("re")!!.setExecutor(undoCmd)
        getCommand("re")!!.tabCompleter = CommandUndo.TabComplete()
        getCommand("script")!!.setExecutor(CommandScript())
        getCommand("script")!!.tabCompleter = CommandScript.TabComplete()
        getCommand("run")!!.setExecutor(CommandRun())
        getCommand("run")!!.tabCompleter = CommandRun.TabComplete()
        getCommand("runat")!!.setExecutor(CommandRunat())
        getCommand("runat")!!.tabCompleter = CommandRunat.TabComplete()
        getCommand("debug")!!.setExecutor(CommandDebug())
        getCommand("debug")!!.tabCompleter = CommandDebug.TabComplete()
        getCommand("error")!!.setExecutor(CommandError())
        getCommand("error")!!.tabCompleter = CommandError.TabComplete()
        getCommand("14erEdit")!!.setExecutor(CommandInfo())
        getCommand("14erEdit")!!.tabCompleter = CommandInfo.TabComplete()
        getCommand("async")!!.setExecutor(CommandAsync())
        getCommand("async")!!.tabCompleter = CommandAsync.TabComplete()
        getCommand("brmask")!!.setExecutor(CommandBrmask())
        getCommand("brmask")!!.tabCompleter = CommandBrmask.TabComplete()
        getCommand("template")!!.setExecutor(CommandTemplate())
        getCommand("template")!!.tabCompleter = CommandTemplate.TabComplete()
        getCommand("funct")!!.setExecutor(CommandFunction())
        getCommand("funct")!!.tabCompleter = CommandFunction.TabComplete()
        getCommand("limit")!!.setExecutor(CommandLimit())
        getCommand("limit")!!.tabCompleter = CommandLimit.TabComplete()

        // Register listeners for brushes and wands
        server.pluginManager.registerEvents(SelectionWandListener(), this)
        server.pluginManager.registerEvents(BrushListener(), this)

        // These are needed by the plugin, but should only be loaded once as they are
        // very slow to load
        SimplexNoise.noiseSeed = Bukkit.getWorlds()[0].seed.toInt() // Seeded using the world seed
        // for variance between worlds
        // but consistency in the same
        // world
        SimplexNoise.simplexNoise = SimplexNoise(Bukkit.getWorlds()[0].seed)
        rand.nextDouble() // Toss out a value from the LCG

        // Load config
        loadConfig()

        // Register the prepackaged things to managers
        CraftscriptLoader.LoadBundledCraftscripts()
        MacroLoader.LoadMacros()
        BrushLoader.LoadBrushes()
        OperatorLoader.LoadOperators()
        IteratorLoader.LoadIterators()
        Function.SetupFunctions()

        // Initialize the WE syntax compat layer
        WorldEditCompat.init()
    }

    companion object {
        // Variables for in main
        private var isDebugDefault = false
        @JvmStatic
        lateinit var plugin : Plugin
        var majorVer : Int = 0
        var minorVer : Int = 0
        @JvmStatic
        val rand = Random()
        @JvmStatic
        var outputStacktrace = false
        var logDebugs = false
        var logErrors = false

        @JvmStatic
        fun logError(message: String, p: ParserState, e: Exception?) {
            logError(message, p.currentPlayer, e)
        }

        @JvmStatic
        fun logError(message: String, p: CommandSender?, e: Exception?) {
            var player = p
            if (player == null) player = Bukkit.getConsoleSender()
            var stackTrace = ""
            if (e != null && outputStacktrace) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                stackTrace = """
                    
                    $sw
                    """.trimIndent()
            }
            if (!logErrors)
                player.sendMessage("§6[ERROR] $message$stackTrace")
            else
                plugin.server.broadcastMessage("§6[ERROR] $message$stackTrace")
            if (logErrors) {
                try {
                    var errMessage = ""
                    errMessage += """
                        $message$stackTrace
                        
                        """.trimIndent()
                    if (!Files.exists(Paths.get("plugins/14erEdit/error.log"))) Files.createFile(Paths.get("plugins/14erEdit/error.log"))
                    Files.write(
                        Paths.get("plugins/14erEdit/error.log"),
                        errMessage.toByteArray(),
                        StandardOpenOption.APPEND
                    )
                } catch (e2: Exception) {
                    // Also not super important
                }
            }
        }

        @JvmStatic
        fun randRange(min: Int, max: Int): Int {
            return if (min == max) {
                min
            } else min + rand.nextInt(max - min + 1)
        }

        private var debugText = ""
        @JvmStatic
        fun logDebug(message: String) {
            var hasADebug = false
            for (p in Bukkit.getOnlinePlayers()) {
                val playerWrapper = PlayerManager.getPlayerWrapper(p)
                hasADebug = hasADebug || playerWrapper.isDebug
            }
                if (hasADebug) debugText += "§c[DEBUG] $message\n" // ----
                try {
                    if (hasADebug) {
                        if (!Files.exists(Paths.get("plugins/14erEdit/debug.log"))) Files.createFile(Paths.get("plugins/14erEdit/debug.log"))
                        Files.writeString(
                            Paths.get("plugins/14erEdit/debug.log"), """
     $message
     
     """.trimIndent(), StandardOpenOption.APPEND
                        )
                    }
                } catch (e: Exception) {
                    // Do nothing, this isn't super important
                }
        }

        fun outputDebug() {
            for (p in Bukkit.getOnlinePlayers()) {
                val playerWrapper = PlayerManager.getPlayerWrapper(p)
                if (playerWrapper.isDebug && debugText.isNotBlank()) {
                    Bukkit.getServer().broadcastMessage(debugText)
                    debugText = ""
                }
            }
        }

        private fun loadConfig() {
            plugin.saveDefaultConfig()
            if (!plugin.config.isSet("maxLoopLength")) {
                println("Updating configuration file.")
                try {
                    val configUpdate1 = """
                        
                        # Max execution lengths
                        maxLoopLength: 5000
                        maxFunctionIters: 100000
                        
                        # Should debugs/errors be logged to a file?
                        logDebugs: false
                        logErrors: true
                        
                        # Should debug be on by default?
                        defaultDebug: false
                        
                        """.trimIndent()
                    Files.write(
                        Paths.get("/plugins/14erEdit/config.yml"), configUpdate1.toByteArray(),
                        StandardOpenOption.APPEND
                    )
                } catch (e: IOException) {
                    println(
                        """
    Error updating configuration. Please manually delete the existing configuration (14erEdit/config.yml).
    The server will now shut down.
    """.trimIndent()
                    )
                    try {
                        Thread.sleep(15000)
                        Bukkit.shutdown()
                    } catch (e1: InterruptedException) {
                        // Do nothing
                    }
                }
            }
            AsyncManager.blocksPerAsync = plugin.config.getLong("blocksPerAsync")
            AsyncManager.ticksPerAsync = plugin.config.getLong("ticksPerAsync")
            WhileNode.maxLoopLength = plugin.config.getLong("maxLoopLength")
            Function.maxFunctionIters = plugin.config.getLong("maxFunctionIters")
            logDebugs = plugin.config.getBoolean("logDebugs")
            logErrors = plugin.config.getBoolean("logErrors")
            this.isDebugDefault = plugin.config.getBoolean("defaultDebug")
        }
    }
}