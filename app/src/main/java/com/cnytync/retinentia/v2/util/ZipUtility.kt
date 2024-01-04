package com.cnytync.retinentia.v2.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.*
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 */
class ZipUtility {
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(zipFilePath: String?, destDirectory: String) {
        val destDir = File(destDirectory)
        if (!destDir.exists()) {
            destDir.mkdir()
        }
        val zipIn = ZipInputStream(FileInputStream(zipFilePath))
        var entry = zipIn.nextEntry
        // iterates over entries in the zip file
        while (entry != null) {
            val filePath = destDirectory + File.separator + entry.name
            if (!entry.isDirectory) {
                // If entry contains parent dirs then create them first
                val strArray = entry.name.split(File.separator)
                if (strArray.size > 1) {
                    for (i in 0..strArray.size - 2) {
                        var dirName = ""
                        for (j in 0..i) {
                            if(j > 0 ) dirName += File.separator
                            dirName += strArray[j]
                        }
                        val dir = File(destDirectory + File.separator + dirName)
                        if (!dir.exists())
                            dir.mkdirs()
                    }
                }
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath)
            } else {
                // if the entry is a directory, make the directory
                val dir = File(filePath)
                dir.mkdirs()
            }
            zipIn.closeEntry()
            entry = zipIn.nextEntry
        }
        zipIn.close()
    }

    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun extractFile(zipIn: ZipInputStream, filePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(filePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read = 0
        while (zipIn.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(IOException::class)
    fun compressGzip(source: Path): GZIPOutputStream {
        val gos = GZIPOutputStream(
            ByteArrayOutputStream()
        ).use { gos ->
            FileInputStream(source.toFile()).use { fis ->

                // copy file
                val buffer = ByteArray(1024)
                var len: Int
                while (fis.read(buffer).also { len = it } > 0) {
                    gos.write(buffer, 0, len)
                }
            }

            return gos
        }
    }

    /** Zip the contents of the directory, and save it in the zipfile  */
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(IOException::class, IllegalArgumentException::class)
    fun zipDirectory(bookDir: String, destFile: String): File {
        if(Files.exists(Paths.get(destFile))) return File(destFile)
        val zipFile: Path = Files.createFile(Paths.get(destFile))

        // Check that the directory is a directory, and get its contents
        var d = File(bookDir + File.separator + "OEBPS" + File.separator + "text")
        if(!d.isDirectory()) {
            d = File(bookDir + File.separator + "EPUB")
        }
        val entries: Array<String> = d.list()
        val buffer = ByteArray(4096) // Create a buffer for copying
        var bytes_read: Int

        // Create a stream to compress data and write it to the zipfile
        val out = ZipOutputStream(Files.newOutputStream(zipFile))

        // Loop through all entries in the directory
        for (i in entries.indices) {
            val f = File(d, entries[i])
            if (f.isDirectory()) continue  // Don't zip sub-directories
            val `in`: FileInputStream = FileInputStream(f) // Stream to read file
            val entry = ZipEntry(f.name) // Make a ZipEntry
            out.putNextEntry(entry) // Store entry
            while (`in`.read(buffer).also { bytes_read = it } != -1) // Copy bytes
                out.write(buffer, 0, bytes_read)
            `in`.close() // Close input stream
        }
        // When we're done with the whole loop, close the output stream
        out.close()

        return zipFile.toFile()
    }

    companion object {
        /**
         * Size of the buffer to read/write data
         */
        private const val BUFFER_SIZE = 4096
    }
}