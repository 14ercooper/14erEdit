package com._14ercooper.megaserver

import java.io.File
import java.io.IOException
import java.time.Instant
import java.util.*

object Main {
    private var javaPath = "java"

    @JvmStatic
    fun main(args: Array<String>) {
        var updateArtifacts = false
        while (true) {
            try {
                // Parse arguments
                for (s in args) {
                    if (s.equals("--force-offline", ignoreCase = true)) {
                        Artifacts.forceOffline = true
                    }
                    if (s.equals("--update", ignoreCase = true)) {
                        updateArtifacts = true
                    }
                    if (s.contains("--javaPath")) {
                        javaPath = s.split("=").toTypedArray()[1]
                    }
                }

                // Splash text
                clearConsole()
                println("14erEdit - Mapmaking Megaserver")
                println("Contributors on the GitHub. Licensed under GPL-3.0. All rights reserved.")
                println("Not affiliated with Mojang, Minecraft, or Microsoft.")
                println("This is free software. You should not have paid for this.")
                println("This software comes with no warranties.")
                Thread.sleep(5000)
                clearConsole()

                // First time setup
                if (!FileIO.exists("init.mms")) {
                    setup()
                }
                if (Artifacts.internetConnected()) {
                    if (updateArtifacts) {
                        FileIO.deleteFile("artifacts", true)
                        FileIO.makePath("artifacts")
                        println("Redownloading all artifacts (this could take a LONG time)...")
                    }
                    Artifacts.updateArtifacts()
                    println("Updated all artifacts")
                }
                Artifacts.loadLocalArtifacts()

                // Pick a profile loop until exit
                while (true) {
                    clearConsole()
                    println("What profile?")
                    val profiles = ArrayList<String>()
                    profiles.addAll(FileIO.listFiles("profiles", true))
                    if (profiles.size == 0) {
                        newProfile()
                        continue
                    }
                    profiles.add("New profile")
                    profiles.add("Exit Server")
                    var profile = UserInput.fromList(profiles)
                    if (profile.equals("new profile", ignoreCase = true)) profile = newProfile()
                    if (profile.equals("exit server", ignoreCase = true)) return
                    inProfile(profile)
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    // What to do in profile function
    @Throws(IOException::class, InterruptedException::class)
    private fun inProfile(profile: String) {
        // Loop until back selected
        while (true) {
            // Get choice
            clearConsole()
            println("Options for $profile:")
            println("\t1) Start server")
            println("\t2) Change version")
            println("\t3) Change plugins")
            println("\t4) Change RAM")
            println("\t5) Delete backups")
            println("\t6) Delete profile")
            println("\t7) Back")
            print("  > ")
            var input = UserInput.numberRange(1, 7)
            // Load vars from disk
            val vars = ArrayList<String>()
            vars.addAll(listOf(*FileIO.readFromFile("profiles/$profile/data.mms").split("&").toTypedArray()))
            var version: String = vars[0].replace("\n", "").replace("\r", "")
            var ramAmt = try {
                vars[1].replace("\n", "").replace("\r", "")
            } catch (e: IndexOutOfBoundsException) {
                "2048"
            }
            var plugins: MutableList<String> = ArrayList()
            for (i in 2 until vars.size) {
                plugins.add(vars[i].replace("\n", "").replace("\r", ""))
            }
            // Start server
            if (input == 1) {
                // Copy artifacts & 14erEdit data
                val jarPath = Artifacts.getArtifactPath("Server", version, "Paper")
                if (jarPath.equals("", ignoreCase = true)) {
                    println("Could not locate jar file")
                    Thread.sleep(5000)
                }
                FileIO.copyFile(jarPath, "profiles/$profile/server.jar", false)
                for (s in plugins) {
                    val path = Artifacts.getArtifactPath("Plugin", version, s)
                    if (!path.equals("", ignoreCase = true)) {
                        FileIO.copyFile(path, "profiles/$profile/plugins/$s.jar", false)
                    }
                }
                FileIO.copyFile("14erEdit", "profiles/$profile/plugins/14erEdit", true)
                // Start server
                val quarterRam = (ramAmt.toInt() / 4).toString()
                val eighthRam = (ramAmt.toInt() / 8).toString()
                val command = (javaPath + " -jar -DIReallyKnowWhatIAmDoingISwear=true -Xmx" + ramAmt + "M -Xms" + ramAmt
                        + "M -Xss" + eighthRam + "M -Xmn" + quarterRam + "M -XX:+UseParallelGC server.jar nogui")
                clearConsole()
                val p = runProcess(command, "profiles/$profile")
                while (p.isAlive) {
                    Thread.sleep(5000)
                    // Things to do while server is running can go here
                }
                // Clean up server & move 14erEdit data
                FileIO.copyFile("profiles/$profile/plugins/14erEdit", "14erEdit", true)
                FileIO.deleteFile("profiles/$profile/plugins/14erEdit", true)
                FileIO.deleteFile("profiles/$profile/logs", true)
                FileIO.deleteFile("profiles/$profile/server.jar", false)
                for (s in plugins) {
                    FileIO.deleteFile("profiles/$profile/plugins/$s.jar", false)
                }
                // Make backup
                FileIO.zipDirectory(
                    File("profiles/$profile"), File("backups"), profile,
                    profile + "_" + Instant.now().epochSecond.toString()
                )
            }
            // Change version
            if (input == 2) {
                val versions = Artifacts.versions
                println("What Minecraft version?")
                version = UserInput.fromList(versions)
                input = 3
            }
            // Change plugins
            if (input == 3) {
                plugins = ArrayList()
                choosePlugins(plugins, version)
            }
            // Change RAM
            if (input == 4) {
                println("How much RAM should the server use (MB)?")
                val max = 8192
                ramAmt = UserInput.numberRange(512, max).toString()
            }
            // Delete backups
            if (input == 5) {
                val backups = FileIO.listFiles("backups", false)
                for (s in backups) {
                    if (s.contains(profile)) {
                        FileIO.deleteFile("backups/$s", false)
                    }
                }
            }
            // Delete profile
            if (input == 6) {
                println("Please type the profile name to confirm deletion.")
                val confirmDelete = UserInput.patternMatch("[A-Za-z0-9\\-_]")
                if (profile.contentEquals(confirmDelete)) {
                    FileIO.deleteFile("profiles/$profile", true)
                    return
                }
            }
            // Save vars to disk
            var data = "$version&"
            data += "$ramAmt&"
            for (s in plugins) {
                data += "$s&"
            }
            data = data.substring(0, data.length - 1)
            FileIO.writeToFile("profiles/$profile/data.mms", false, data)
            // Back to profile select
            if (input == 7) return
        }
    }

    private fun choosePlugins(plugins: MutableList<String>, version: String) {
        while (true) {
            println("Plugins to install:\nInstalled: ")
            for (s in plugins) {
                print("$s ")
            }
            println()
            println("Install which plugins?")
            val artifactList = Artifacts.getLocalArtifacts("Plugin", version)
            val pluginList: MutableList<String> = ArrayList()
            for (s in artifactList) {
                pluginList.add(s.split(";").toTypedArray()[2])
            }
            pluginList.add("Done")
            val plugin = UserInput.fromList(pluginList)
            if (plugin.equals("done", ignoreCase = true)) break
            if (plugins.contains(plugin)) plugins.remove(plugin) else plugins.add(plugin)
        }
    }

    // Make a new profile
    @Throws(IOException::class)
    private fun newProfile(): String {
        // Name
        println("Profile name? (alphanumeric only)")
        val name = UserInput.patternMatch("[A-Za-z0-9\\-_]")
        // Spigot version
        val versions = Artifacts.versions
        println("What Minecraft version?")
        val version = UserInput.fromList(versions)
        // Initial plugins
        val plugins: MutableList<String> = ArrayList()
        choosePlugins(plugins, version)
        // Save to profile folder
        var data = "$version&"
        data += "2048&"
        for (s in plugins) {
            data += "$s&"
        }
        data = data.substring(0, data.length - 1)
        FileIO.makePath("profiles/$name")
        FileIO.makePath("profiles/$name/plugins")
        FileIO.writeToFile("profiles/$name/data.mms", false, data)
        // Make EULA and properties files
        FileIO.writeToFile("profiles/$name/eula.txt", false, "eula=true")
        val serverProps = Properties.server + "level-name=" + name
        FileIO.writeToFile("profiles/$name/server.properties", false, serverProps)
        FileIO.writeToFile("profiles/$name/spigot.yml", false, Properties.spigot)
        FileIO.writeToFile("profiles/$name/paper.yml", false, Properties.paper)
        return name
    }

    // First time setup
    @Throws(Exception::class)
    private fun setup() {
        if (!Artifacts.internetConnected()) {
            println("Internet required for setup. Please connect to the internet.")
            throw Exception()
        }
        println("Performing first time setup...\nMaking directories...")
        FileIO.makePath("artifacts")
        FileIO.makePath("profiles")
        FileIO.makePath("backups")
        FileIO.makePath("14erEdit/Commands")
        FileIO.makePath("14erEdit/ops")
        FileIO.makePath("14erEdit/schematics")
        FileIO.makePath("14erEdit/vars")
        FileIO.makePath("14erEdit/templates")
        FileIO.makePath("14erEdit/multibrushes")
        FileIO.makePath("14erEdit/functions")
        println("Downloading artifacts (this could take a LONG time)...")
        Artifacts.updateArtifacts()
        FileIO.writeToFile("init.mms", false, "")
    }

    // Runs a process inside of the current console
    @Throws(IOException::class)
    private fun runProcess(cmd: String, dir: String): Process {
        val cmdList = cmd.split(" ").toTypedArray()
        val pb = ProcessBuilder(*cmdList)
        pb.directory(File(dir))
        return pb.inheritIO().start()
    }

    // Wipes the console clean
    private fun clearConsole() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
        try {
            if (System.getProperty("os.name").contains("Windows")) ProcessBuilder("cmd", "/c", "cls").inheritIO()
                .start().waitFor() else Runtime.getRuntime().exec("clear")
        } catch (ex: Exception) {
        }
    }

    private fun onError(e: Exception) {
        try {
            println("Something went wrong. Restarting in 5 seconds...")
            var out = """
                ${e.message}
                
                """.trimIndent()
            e.printStackTrace()
            for (ste in e.stackTrace) {
                out += """
                    $ste
                    
                    """.trimIndent()
            }
            FileIO.writeToFile("error_" + Instant.now().epochSecond.toString() + ".txt", false, out)
            Thread.sleep(5000)
        } catch (e1: Exception) {
            println("Error")
        }
    }
}