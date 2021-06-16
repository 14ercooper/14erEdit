package com._14ercooper.worldeditor.main

import com._14ercooper.worldeditor.blockiterator.IteratorLoader
import com._14ercooper.worldeditor.blockiterator.IteratorManager
import com._14ercooper.worldeditor.brush.BrushListener
import com._14ercooper.worldeditor.brush.BrushLoader
import com._14ercooper.worldeditor.commands.*
import com._14ercooper.worldeditor.compat.WorldEditCompat
import com._14ercooper.worldeditor.functions.Function
import com._14ercooper.worldeditor.macros.MacroLauncher
import com._14ercooper.worldeditor.macros.MacroLoader
import com._14ercooper.worldeditor.operations.OperatorLoader
import com._14ercooper.worldeditor.operations.Parser
import com._14ercooper.worldeditor.scripts.CraftscriptLoader
import com._14ercooper.worldeditor.scripts.CraftscriptManager
import com._14ercooper.worldeditor.selection.SelectionWandListener
import com._14ercooper.worldeditor.undo.UndoSystem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

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
                // Do nothing
            }
        }

        // Load version numbers
        val ver = server.version.split("MC: ").toTypedArray()[1]
        ver.split(".").toTypedArray()[1].replace("[^\\d.]".toRegex(), "").toInt().also { GlobalVars.majorVer = it }
        try {
            ver.split(".").toTypedArray()[2].replace("[^\\d.]".toRegex(), "").toInt().also { GlobalVars.minorVer = it }
        }
        catch (e : Exception) {
            GlobalVars.minorVer = 0
        }
        println("Using version " + server.version + ": " + GlobalVars.majorVer + "/" + GlobalVars.minorVer)

        // Create folders as needed
        try {
            Files.createDirectories(Paths.get("plugins/14erEdit/schematics"))
            Files.createDirectories(Paths.get("plugins/14erEdit/ops"))
            Files.createDirectories(Paths.get("plugins/14erEdit/Commands"))
            Files.createDirectories(Paths.get("plugins/14erEdit/vars"))
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

        // Set up brush mask
        GlobalVars.brushMask = HashSet()
        GlobalVars.brushMask.add(Material.AIR)
        GlobalVars.brushMask.add(Material.CAVE_AIR)
        GlobalVars.brushMask.add(Material.VOID_AIR)

        // Register listeners for brushes and wands
        server.pluginManager.registerEvents(SelectionWandListener(), this)
        server.pluginManager.registerEvents(BrushListener(), this)

        // These are needed by the plugin, but should only be loaded once as they are
        // very slow to load
        GlobalVars.noiseSeed = Bukkit.getWorlds()[0].seed.toInt() // Seeded using the world seed
        // for variance between worlds
        // but consistency in the same
        // world
        GlobalVars.simplexNoise = SimplexNoise(Bukkit.getWorlds()[0].seed)
        GlobalVars.plugin = this
        GlobalVars.rand.nextDouble() // Toss out a value from the LCG

        // Load config
        loadConfig()

        // Load managers
        GlobalVars.scriptManager = CraftscriptManager()
        GlobalVars.macroLauncher = MacroLauncher()
        GlobalVars.operationParser = Parser()
        //GlobalVars.asyncManager = AsyncManager()
        GlobalVars.iteratorManager = IteratorManager()

        // Register the prepackaged things to managers
        CraftscriptLoader.LoadBundledCraftscripts()
        MacroLoader.LoadMacros()
        BrushLoader.LoadBrushes()
        OperatorLoader.LoadOperators()
        IteratorLoader.LoadIterators()
        Function.SetupFunctions()

        // Set up the world height
        if (GlobalVars.majorVer >= 17) {
            GlobalVars.minEditY = server.worlds[0].minHeight.toLong()
            GlobalVars.maxEditY = server.worlds[0].maxHeight.toLong()
        }

        // Initialize the WE syntax compat layer
        WorldEditCompat.init()
    }

    companion object {
        @JvmStatic
        fun logError(message: String, p: CommandSender?, e: Exception?) {
            var player = p
            GlobalVars.errorLogged = true
            if (player == null) player = Bukkit.getConsoleSender()
            var stackTrace = ""
            if (e != null && GlobalVars.outputStacktrace) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                stackTrace = """
                    
                    $sw
                    """.trimIndent()
            }
            if (!GlobalVars.logErrors)
                player.sendMessage("§6[ERROR] $message$stackTrace")
            else
                GlobalVars.plugin.server.broadcastMessage("§6[ERROR] $message$stackTrace")
            if (GlobalVars.logErrors) {
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
            } else min + GlobalVars.rand.nextInt(max - min + 1)
        }

        private var debugText = ""
        @JvmStatic
        fun logDebug(message: String) {
            if (GlobalVars.isDebug) debugText += "§c[DEBUG] $message\n" // ----
            try {
                if (GlobalVars.logDebugs) {
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
            if (GlobalVars.isDebug && debugText.isNotBlank()) {
                Bukkit.getServer().broadcastMessage(debugText)
                debugText = ""
            }
        }

        private fun loadConfig() {
            GlobalVars.plugin.saveDefaultConfig()
            if (!GlobalVars.plugin.config.isSet("maxLoopLength")) {
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
            GlobalVars.undoLimit = GlobalVars.plugin.config.getLong("undoLimit")
            if (GlobalVars.undoLimit >= 0)
                GlobalVars.undoLimit /= 250
            GlobalVars.blocksPerAsync = GlobalVars.plugin.config.getLong("blocksPerAsync")
            GlobalVars.ticksPerAsync = GlobalVars.plugin.config.getLong("ticksPerAsync")
            GlobalVars.maxLoopLength = GlobalVars.plugin.config.getLong("maxLoopLength")
            GlobalVars.maxFunctionIters = GlobalVars.plugin.config.getLong("maxFunctionIters")
            GlobalVars.logDebugs = GlobalVars.plugin.config.getBoolean("logDebugs")
            GlobalVars.logErrors = GlobalVars.plugin.config.getBoolean("logErrors")
            GlobalVars.isDebug = GlobalVars.plugin.config.getBoolean("defaultDebug")
        }

        fun inEditRegion(x: Long, y: Long, z: Long): Boolean {
            return (x <= GlobalVars.minEditX || y <= GlobalVars.minEditY || z <= GlobalVars.minEditZ
                    || x >= GlobalVars.maxEditX || y >= GlobalVars.maxEditY || z >= GlobalVars.maxEditZ)
        }
    }
}