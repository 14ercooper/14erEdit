package com._14ercooper.worldeditor.undo

import com._14ercooper.worldeditor.main.GlobalVars
import com._14ercooper.worldeditor.main.Main
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap

class UserUndo
    (thisName: String) {

    var undoElements : HashMap<String, UndoElement>
    private var undoList : MutableList<String>
    private var redoList : MutableList<String>
    var name : String = thisName

    // Set up this user undo (including loading from disk if needed)
    init {
        undoElements = HashMap()
        undoList = mutableListOf()
        redoList = mutableListOf()
        loadUndoList()
    }

    // Get a new undo element to use to register undos
    fun getNewUndoElement() : UndoElement {
        val uuid : String = UUID.randomUUID().toString()
        val undoElement = UndoElement(uuid, this)
        undoElements[uuid] = undoElement
        Main.logDebug("New undo for user $name created with ID $uuid")
        return undoElement
    }

    // Finalize an undo after it's done
    fun finalizeUndo(undo : UndoElement) : Boolean {
        if (undoElements.containsKey(undo.name)) {
            undo.flush()
            undoList.add(undo.name)
            undoElements.remove(undo.name)
            Main.logDebug("Undo with id ${undo.name} finalized")
            return true
        }
        return false
    }

    // Undo a number of changes (undo elements)
    fun undoChanges(count : Int) : Boolean {
        flush()
        undoList = undoList.filter { it.isNotBlank() } as MutableList<String>
        var undoCount = count
        if (undoCount > undoList.size)
            undoCount = undoList.size
        val undoSet : MutableList<UndoElement> = mutableListOf()
        for (i in 1..undoCount) {
            val s = undoList[undoList.size - 1]
            val ue = UndoElement(s, this)
            ue.startApplyUndo()
            undoSet.add(ue)
            redoList.add(s)
            undoList.removeAt(undoList.size - 1)
        }
        GlobalVars.asyncManager.scheduleEdit(undoSet)
        Main.logDebug("Undoing $count changes for $name")
        return true
    }

    // Redo a number of changes (undo elements)
    fun redoChanges(count : Int) : Boolean {
        flush()
        redoList = redoList.filter { it.isNotBlank() } as MutableList<String>
        var redoCount = count
        if (redoCount > redoList.size)
            redoCount = redoList.size
        val redoSet : MutableList<UndoElement> = mutableListOf()
        for (i in 1..redoCount) {
            val s = redoList[redoList.size - 1]
            val ue = UndoElement(s, this)
            ue.startApplyRedo()
            redoSet.add(ue)
            undoList.add(s)
            redoList.removeAt(redoList.size - 1)
        }
        GlobalVars.asyncManager.scheduleEdit(redoSet)
        Main.logDebug("Redoing $count changes for $name")
        return true
    }

    // Save the undo and redo element lists to disk
    private fun saveUndoList() : Boolean {
        val fileName = "plugins/14erEdit/undo/$name/"
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
    private fun loadUndoList() : Boolean {
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
    fun flush() : Boolean {
        saveUndoList()
        undoElements.forEach { (_, undoElement) -> undoElement.flush() }
        undoElements = HashMap()
        Main.logDebug("Flushed user undo for $name")
        return true
    }
}