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

package com._14ercooper.worldeditor.undo

import com._14ercooper.worldeditor.async.AsyncManager
import com._14ercooper.worldeditor.main.Main
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import java.io.File
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.util.*

open class UserUndo
    (thisName: String) {

    var undoElements: HashMap<String, UndoElement>
    private var undoList: MutableList<String>
    private var redoList: MutableList<String>
    var name: String = thisName

    // Set up this user undo (including loading from disk if needed)
    init {
        undoElements = HashMap()
        undoList = mutableListOf()
        redoList = mutableListOf()
        if (!thisName.equals("dummy", true)) {
            loadUndoList()
        }
    }

    // Get a new undo element to use to register undos
    open fun getNewUndoElement(): UndoElement {
        val uuid: String = UUID.randomUUID().toString()
        val undoElement = UndoElement(uuid, this)
        undoElements[uuid] = undoElement
        Main.logDebug("New undo for user $name created with ID $uuid")
        return undoElement
    }

    // Finalize an undo after it's done
    open fun finalizeUndo(undo: UndoElement): Boolean {
        if (undoElements.containsKey(undo.name)) {
            runBlocking {
                undo.flush()
            }
            undoList.add(undo.name)
            undoElements.remove(undo.name)
            Main.logDebug("Undo with id ${undo.name} finalized")
            return true
        }
        return false
    }

    // Undo a number of changes (undo elements)
    open fun undoChanges(count: Int): Boolean {
        flush()
        undoList = undoList.filter { it.isNotBlank() } as MutableList<String>
        val undoCount = count.coerceAtMost(undoList.size)
        val undoSet: MutableList<UndoElement> = mutableListOf()
        for (i in 1..undoCount) {
            val s = undoList[undoList.size - 1]
            val ue = UndoElement(s, this)
            ue.startApplyUndo()
            undoSet.add(ue)
            redoList.add(s)
            undoList.removeAt(undoList.size - 1)
        }
        if (undoSet.isEmpty()) {
            Main.logDebug("Nothing to undo for $name")
            return true
        }
        val uuid = UUID.fromString(name)
        AsyncManager.scheduleEdit(undoSet, Bukkit.getServer().getPlayer(uuid) ?: Bukkit.getConsoleSender())
        Main.logDebug("Undoing $count changes for $name")
        return true
    }

    // Redo a number of changes (undo elements)
    open fun redoChanges(count: Int): Boolean {
        flush()
        redoList = redoList.filter { it.isNotBlank() } as MutableList<String>
        val redoCount = count.coerceAtMost(redoList.size)
        val redoSet: MutableList<UndoElement> = mutableListOf()
        for (i in 1..redoCount) {
            val s = redoList[redoList.size - 1]
            val ue = UndoElement(s, this)
            ue.startApplyRedo()
            redoSet.add(ue)
            undoList.add(s)
            redoList.removeAt(redoList.size - 1)
        }
        if (redoSet.isEmpty()) {
            Main.logDebug("Nothing to redo for $name")
            return true
        }
        val uuid = UUID.fromString(name)
        AsyncManager.scheduleEdit(redoSet, Bukkit.getServer().getPlayer(uuid) ?: Bukkit.getConsoleSender())
        Main.logDebug("Redoing $count changes for $name")
        return true
    }

    // Save the undo and redo element lists to disk
    private fun saveUndoList(): Boolean {
        val fileName = "plugins/14erEdit/undo/$name/"
        // Size limit the lists
        while (undoList.size > maxListSize) {
            val undoName = undoList.removeFirst()
            File(fileName + undoName).deleteRecursively()
        }
        while (redoList.size > maxListSize) {
            val redoName = redoList.removeFirst()
            File(fileName + redoName).deleteRecursively()
        }

        // Save the lists
        run {
            Files.deleteIfExists(Path.of(fileName + "undoList"))
            var str = ""
            for (s in undoList) {
                str += s + "\n"
            }
            Files.writeString(Path.of(fileName + "undoList"), str)
        }
        run {
            Files.deleteIfExists(Path.of(fileName + "redoList"))
            var str = ""
            for (s in redoList) {
                str += s + "\n"
            }
            Files.writeString(Path.of(fileName + "redoList"), str)
        }
        Main.logDebug("Undo and redo lists for $name saved to disk")
        return true
    }

    // Load the undo and redo element lists from disk
    private fun loadUndoList(): Boolean {
        val fileName = "plugins/14erEdit/undo/$name/"
        if (Files.exists(Path.of(fileName + "undoList"))) {
            undoList = Files.readString(Path.of(fileName + "undoList")).lines() as MutableList<String>
            undoList = undoList.filter { it.isNotBlank() } as MutableList<String>
        }
        if (Files.exists(Path.of(fileName + "redoList"))) {
            redoList = Files.readString(Path.of(fileName + "redoList")).lines() as MutableList<String>
            redoList = redoList.filter { it.isNotBlank() } as MutableList<String>
        }
        Main.logDebug("Loaded undo and redo lists for $name from disk")
        return true
    }

    // Flush all data to disk
    open fun flush(): Boolean {
        try {
            saveUndoList()
        }
        catch (e : NoSuchFileException) {
            Files.createDirectories(Path.of("plugins/14erEdit/undo/${name}"))
            saveUndoList()
        }
        runBlocking {
            undoElements.forEach { (_, undoElement) -> undoElement.flush() }
        }
        undoElements = HashMap()
        Main.logDebug("Flushed user undo for $name")
        return true
    }

    companion object {
        const val maxListSize: Long = 500
    }
}