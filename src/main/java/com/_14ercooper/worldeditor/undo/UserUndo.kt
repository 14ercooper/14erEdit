package com._14ercooper.worldeditor.undo

class UserUndo {

    var undoElements : HashMap<String, UndoElement> = HashMap()

    // Set up this user undo (including loading from disk if needed)
    constructor() {

    }

    // Get a new undo element to use to register undos
    fun getNewUndoElement() : UndoElement {
        return UndoElement()
    }

    // Look up an undo element by it's ID
    fun findUndoElement(id : String) : UndoElement {
        return UndoElement(id)
    }

    // Finalize an undo after it's done
    fun finalizeUndo(undo : UndoElement) : Boolean {
        return false
    }

    // Finalize undo, including the lookup using the ID
    fun finalizeUndo(id : String) : Boolean {
        return finalizeUndo(findUndoElement(id))
    }

    // Undo a number of changes (undo elements)
    fun undoChanges(count : Int) : Boolean {
        return false
    }

    // Redo a number of changes (undo elements)
    fun redoChanges(count : Int) : Boolean {
        return false
    }

    // Load the undo element list from disk
    fun loadUndoList() : Boolean {
        return false
    }

    // Save the undo element list to disk
    fun saveUndoList() : Boolean {
        return false
    }

    // Flush all data to disk
    fun flush() : Boolean {
        saveUndoList()
        undoElements.forEach { (s, undoElement) -> undoElement.flush() }
        undoElements = HashMap()
        return true
    }
}