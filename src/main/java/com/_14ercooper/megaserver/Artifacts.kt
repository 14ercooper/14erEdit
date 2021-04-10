package com._14ercooper.megaserver

import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.channels.Channels
import java.util.*

object Artifacts {
    // Offline mode?
    var forceOffline = false

    // Root of artifacts
    private const val artifactsBase = "https://files.14erc.com/edit/mms/"

    // Map of artifacts by type
    private var buildTools: List<String> = ArrayList()
    private var servers: MutableList<List<String>> = ArrayList()
    private var plugins: MutableList<List<String>> = ArrayList()

    // Local artifacts
    // Format in file Type;MinecraftVersion;Name;File;Version
    private var localArtifacts: MutableMap<String, Int> = HashMap()

    // List all artifacts of a certain type
    val versions: List<String>
        get() {
            val versions: MutableSet<String> = HashSet()
            for ((key) in localArtifacts) {
                versions.add(key.split(";").toTypedArray()[1])
            }
            val toReturn: MutableList<String> = ArrayList()
            for (s in versions) {
                if (s.equals("all", ignoreCase = true)) continue
                toReturn.add(s)
            }
            return toReturn
        }

    // Get the file path of the artifact
    fun getArtifactPath(type: String, version: String?, name: String?): String {
        val key = "$type;$version;$name"
        for ((key1) in localArtifacts) {
            if (key1.contains(key)) {
                return "artifacts/" + key1.split(";").toTypedArray()[3]
            }
        }
        return ""
    }

    // Load local artifacts into the file
    @Throws(IOException::class)
    fun loadLocalArtifacts() {
        localArtifacts = HashMap()
        if (!FileIO.exists("artifacts/local.mms")) return
        val artifacts = FileIO.readFromFile("artifacts/local.mms").split("&").toTypedArray()
        for (s in artifacts) {
            val data: MutableList<String> = ArrayList()
            data.addAll(listOf(*s.split(";").toTypedArray()))
            var data4 = data[4]
            data4 = data4.replace("\n", "").replace("\r", "")
            val ver = data4.toInt()
            data.removeAt(4)
            var dat = ""
            for (i in 0..3) {
                dat += data[i] + ";"
            }
            dat = dat.substring(0, dat.length - 1)
            localArtifacts[dat] = ver
        }
    }

    // Save local artifacts to file
    @Throws(IOException::class)
    fun saveLocalArtifacts() {
        var toSave = ""
        for ((key, value) in localArtifacts) {
            toSave += "$key;$value&"
        }
        toSave = toSave.substring(0, toSave.length - 1)
        FileIO.writeToFile("artifacts/local.mms", false, toSave)
    }

    // Get version of a local artifact
    private fun getLocalVersion(type: String, version: String, name: String): Int {
        val key = "$type;$version;$name"
        for ((key1, value) in localArtifacts) {
            if (key1.contains(key)) return value
        }
        return 0
    }

    // Start tracking a local artifact
    private fun addLocalArtifact(type: String, version: String, name: String, file: String, ver: Int) {
        val key = "_$type;$version;$name;$file"
        localArtifacts[key] = ver
    }

    // Download updates to all artifacts
    @Throws(InterruptedException::class, IOException::class)
    fun updateArtifacts() {
        if (!internetConnected()) {
            println("This function requires internet connectivity")
            Thread.sleep(5000)
            return
        }
        updateNetworkArtifactsList()
        loadLocalArtifacts()
        if (buildTools[1].toInt() > getLocalVersion("BuildTools", "All", "BuildTools")) {
            println("Downloading BuildTools")
            downloadFromURL(artifactsBase + buildTools[0], "artifacts/BuildTools.jar")
            addLocalArtifact("BuildTools", "All", "BuildTools", "BuildTools.jar", buildTools[1].toInt())
        }
        for (s in servers) {
            if (s[3].toInt() > getLocalVersion("Server", s[0], s[1])) {
                println("Downloading server " + s[0] + " " + s[1])
                downloadFromURL(artifactsBase + s[2], "artifacts/" + s[2])
                addLocalArtifact("Server", s[0], s[1], s[2], s[3].toInt())
            }
        }
        for (s in plugins) {
            if (s[3].toInt() > getLocalVersion("Plugin", s[0], s[1])) {
                println("Downloading plugin " + s[0] + " " + s[1])
                downloadFromURL(artifactsBase + s[2], "artifacts/" + s[2])
                addLocalArtifact("Plugin", s[0], s[1], s[2], s[3].toInt())
            }
        }
        saveLocalArtifacts()
    }

    fun getLocalArtifacts(type: String, version: String?): List<String> {
        val key = "$type;$version"
        val artifacts: MutableList<String> = ArrayList()
        for ((key1) in localArtifacts) {
            if (key1.contains(key)) {
                artifacts.add(key1)
            }
        }
        return artifacts
    }

    // Check for internet connectivity
    fun internetConnected(): Boolean {
        return if (forceOffline) false else try {
            val url = URL(artifactsBase + "Artifacts.txt")
            val connection = url.openConnection()
            connection.connect()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Grab new artifacts from the network
    @Throws(IOException::class)
    private fun updateNetworkArtifactsList() {
        val rows = ArrayList<String>()
        buildTools = ArrayList()
        servers = ArrayList()
        plugins = ArrayList()
        rows.addAll(listOf(*getTextFromURL(artifactsBase + "Artifacts.txt").split("&").toTypedArray()))
        var found = 0
        while (rows.size > 0) {
            val data = rows[0].split(";").toTypedArray()
            found++
            rows.removeAt(0)
            if (data[0].equals("artifacts", ignoreCase = true)) {
                if (data[1].equals("this", ignoreCase = true)) continue else rows.addAll(
                    listOf(
                        *getTextFromURL(
                            data[1]
                        ).split("&").toTypedArray()
                    )
                )
            }
            if (data[0].equals("buildtools", ignoreCase = true)) {
                val list = ArrayList<String>()
                list.addAll(listOf(*data))
                list.removeAt(0)
                buildTools = list
            }
            if (data[0].equals("server", ignoreCase = true)) {
                val list = ArrayList<String>()
                list.addAll(listOf(*data))
                list.removeAt(0)
                servers.add(list)
            }
            if (data[0].equals("plugin", ignoreCase = true)) {
                val list = ArrayList<String>()
                list.addAll(listOf(*data))
                list.removeAt(0)
                plugins.add(list)
            }
        }
        println("Found $found artifacts.")
    }

    // Returns the contents of a URL as a string
    @Throws(IOException::class)
    private fun getTextFromURL(url: String): String {
        val website = URL(url)
        val connection = website.openConnection()
        val `in` = BufferedReader(InputStreamReader(connection.getInputStream()))
        val response = StringBuilder()
        var inputLine: String?
        while (`in`.readLine().also { inputLine = it } != null) response.append(inputLine)
        `in`.close()
        return response.toString()
    }

    // Downloads the file at the URL to the target
    @Throws(IOException::class)
    fun downloadFromURL(sourceURL: String?, target: String?) {
        val website = URL(sourceURL)
        val rbc = Channels.newChannel(website.openStream())
        val fos = FileOutputStream(target)
        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
        fos.close()
    }
}