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

import java.io.*
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileIO {
    // Move a file or folder
    @Throws(IOException::class)
    fun copyFile(src: String, dest: String, folder: Boolean) {
        if (!folder) {
            Files.copy(Paths.get(src), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING)
        } else {
            copyDirectory(src, dest)
        }
    }

    // Delete a file or folder
    @Throws(IOException::class)
    fun deleteFile(src: String, folder: Boolean) {
        if (!folder) {
            Files.deleteIfExists(Paths.get(src))
        } else {
            delDirectory(src)
        }
    }

    // Makes a path of folders
    fun makePath(path: String) {
        File(path).mkdirs()
    }

    // Zip a folder
    @Throws(InterruptedException::class)
    fun zipDirectory(
        directoryToCompress: File, outputDirectory: File, topFolderName: String,
        outFile: String
    ) {
        try {
            val dest = FileOutputStream(File(outputDirectory, "$outFile.zip"))
            val zipOutputStream = ZipOutputStream(dest)
            zipDirectoryHelper(directoryToCompress, directoryToCompress, zipOutputStream, topFolderName)
            zipOutputStream.close()
        } catch (e: Exception) {
            println("Zip error")
            Thread.sleep(2000)
        }
    }

    // List all files at path
    fun listFiles(path: String, folders: Boolean): List<String> {
        if (!folders) {
            val f = File(path)
            return listOf(*f.list()!!)
        }
        val file = File(path)
        val directories = file.list { current, name -> File(current, name).isDirectory }
        return listOf(*directories!!)
    }

    // Read a text file into a string
    @Throws(IOException::class)
    fun readFromFile(file: String): String {
        return try {
//            val fileContentsList = Files.readAllLines(Paths.get(file))
            Files.readString(Paths.get(file))
//            for (i in fileContentsList) {
//                fileContents += """
//                $i
//
//                """.trimIndent()
//            }
//            fileContents
        } catch (e: NoSuchFileException) {
            ""
        }
    }

    // File exists?
    fun exists(path: String): Boolean {
        return File(path).exists()
    }

    // Writes a string to a file
    @Throws(IOException::class)
    fun writeToFile(file: String, append: Boolean, text: String) {
        val fileWriter = FileWriter(file, append)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(text)
        bufferedWriter.close()
        fileWriter.close()
    }

    // Used to delete a folder with files
    @Throws(IOException::class)
    private fun delDirectory(target: String) {
        val directory = Paths.get(target)
        Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                Files.delete(file)
                return FileVisitResult.CONTINUE
            }

            @Throws(IOException::class)
            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                Files.delete(dir)
                return FileVisitResult.CONTINUE
            }
        })
    }

    // Helper for zipping folders
    @Throws(Exception::class)
    private fun zipDirectoryHelper(
        rootDirectory: File, currentDirectory: File, out: ZipOutputStream,
        topFolderName: String
    ) {
        val data = ByteArray(2048)
        val files = currentDirectory.listFiles()
        if (files == null) {
            // no files were found or this is not a directory
        } else {
            for (file in files) {
                if (file.isDirectory && file.name != "") {
                    zipDirectoryHelper(rootDirectory, file, out, topFolderName)
                } else {
                    val fi = FileInputStream(file)
                    // creating structure and avoiding duplicate file names
                    val name = file.absolutePath.replace(rootDirectory.absolutePath, topFolderName)
                    val entry = ZipEntry(name)
                    out.putNextEntry(entry)
                    var count: Int
                    val origin = BufferedInputStream(fi, 2048)
                    while (origin.read(data, 0, 2048).also { count = it } != -1) {
                        out.write(data, 0, count)
                    }
                    origin.close()
                }
            }
        }
    }

    // Used to copy a directory (replacing existing files)
    @Throws(IOException::class)
    private fun copyDirectory(src: String, dest: String) {
        val source = File(src)
        val destination = File(dest)
        if (source.isDirectory) {
            if (!destination.exists()) {
                destination.mkdirs()
            }
            val files = source.list()!!
            for (file in files) {
                val srcFile = File(source, file)
                val destFile = File(destination, file)
                copyDirectory(srcFile.toString(), destFile.toString())
            }
        } else {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            try {
                `in` = FileInputStream(source)
                out = FileOutputStream(destination)
                val buffer = ByteArray(1024)
                var length: Int
                while (`in`.read(buffer).also { length = it } > 0) {
                    out.write(buffer, 0, length)
                }
            } catch (e: Exception) {
                // Just ignore the error, it's to be expected
            }
            `in`!!.close()
            out!!.close()
        }
    }

    fun getHash(file: String): String {
        Files.newInputStream(Paths.get(file)).use {
            val crc = CRC32()
            var c = 0
            while (c != -1) {
                c = it.read()
                crc.update(c)
            }
            return Integer.toHexString(crc.value.toInt())
        }
    }
}