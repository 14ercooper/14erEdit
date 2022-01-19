// Licensed Under GPL v3.0
//https://github.com/14ercooper/14erEdit


package com._14ercooper.worldeditor.undo

enum class UndoMode {
    IDLE,
    WAITING_FOR_BLOCKS,
    PERFORMING_UNDO,
    PERFORMING_REDO,
    UNDO_FINISHED,
    REDO_FINISHED
}