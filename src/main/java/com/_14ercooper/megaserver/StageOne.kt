package com._14ercooper.megaserver

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object StageOne {

    private const val targetPath = "megaserverStageTwo.jar"

    @JvmStatic
    fun main(args : Array<String>) {
        if (args.contains("--stage-two")) {
            Main.mainStageTwo(args)
            return;
        }

        if (args.contains("--force-offline")) {
            runStageTwo(args);
            return
        }

        if (!Artifacts.internetConnected()) {
            runStageTwo(args);
            return
        }

        // Get the artifacts root megaserver key
        val rootLines = Artifacts.getTextFromURL(Artifacts.artifactTreeRoot).split("&")
        val megaserverKey = rootLines.filter { it -> it.startsWith("MEGASERVER") }[0]
        val keySplit = megaserverKey.split(";")
        val dlPath = keySplit[1]
        val remoteHash = keySplit[2]

        val localHash = if (Files.exists(Paths.get(targetPath))) {
            FileIO.getHash(targetPath)
        }
        else {
            ""
        }

        if (remoteHash != localHash) {
            println("Found remote file with hash $remoteHash, have local file with hash $localHash");
            Files.deleteIfExists(Paths.get(targetPath))
            Artifacts.downloadFromURL(dlPath, targetPath)
            Files.deleteIfExists(Paths.get(dlPath.split("/").toTypedArray().last()))
        }

        runStageTwo(args)
        return
    }

    private fun runStageTwo(args : Array<String>) {
        val command = mutableListOf<String>()
        command.add("java")
        command.add("-jar")
        command.add(targetPath)
        command.addAll(args)
        command.add("--stage-two")
        val p = runProcess(command);
        while (p.isAlive) {
            Thread.sleep(5000)
            // Things to do while server is running can go here
        }
        println("\nGood bye")
    }

    // Runs a process inside of the current console
    @Throws(IOException::class)
    private fun runProcess(cmd: List<String>): Process {
        val pb = ProcessBuilder(*cmd.toTypedArray())
        return pb.inheritIO().start()
    }

}