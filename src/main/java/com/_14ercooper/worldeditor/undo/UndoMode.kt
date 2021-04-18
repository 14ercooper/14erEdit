package com._14ercooper.worldeditor.undo

enum class UndoMode {
    IDLE,
    WAITING_FOR_BLOCKS,
    PERFORMING_UNDO,
    PERFORMING_REDO,
    UNDO_FINISHED,
    REDO_FINISHED
}