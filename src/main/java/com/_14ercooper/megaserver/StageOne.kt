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

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object StageOne {

    private const val targetPath = "megaserverStageTwo.jar"

    @JvmStatic
    fun main(args : Array<String>) {
        if (args.contains("--stage-two")) {
            Main.mainStageTwo(args)
            return
        }

        if (args.contains("--force-offline")) {
            runStageTwo(args)
            return
        }

        if (!Artifacts.internetConnected()) {
            runStageTwo(args)
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
            println("Found remote file with hash $remoteHash, have local file with hash $localHash")
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
        val p = runProcess(command)
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