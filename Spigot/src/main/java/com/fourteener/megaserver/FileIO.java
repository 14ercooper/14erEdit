package com.fourteener.megaserver;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileIO {
	
	// Move a file or folder
	public static void copyFile(String src, String dest, boolean folder) throws IOException {
		if (!folder) {
			Files.copy(Paths.get(src), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
		}
		else {
			copyDirectory(src, dest);
		}
	}
	
	// Delete a file or folder
	public static void deleteFile(String src, boolean folder) throws IOException {
		if (!folder) {
			Files.delete(Paths.get(src));
		}
		else {
			delDirectory(src);
		}
	}
	
	// Makes a path of folders
	public static void makePath(String path) {
		File f = new File(path);
		f.mkdirs();
	}
	
	// Zip a folder
	public static void zipDirectory (File directoryToCompress, File outputDirectory, String topFolderName, String outFile) throws InterruptedException{
        try {
            FileOutputStream dest = new FileOutputStream(new File(outputDirectory, outFile + ".zip"));
            ZipOutputStream zipOutputStream = new ZipOutputStream(dest);
            zipDirectoryHelper(directoryToCompress, directoryToCompress, zipOutputStream, topFolderName);
            zipOutputStream.close();
        } catch (Exception e) {
        	System.out.println("Zip error");
        	Thread.sleep(2000);
        }
    }
	
	// List all files at path
	public static List<String> listFiles(String path, boolean folders) {
		if (!folders) {
			File f = new File(path);
			return Arrays.asList(f.list());
		}
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return Arrays.asList(directories);
	}
	
	// Read a text file into a string
	public static String readFromFile(String file) throws IOException {
		try {
			List<String> fileContentsList = Files.readAllLines(Paths.get(file));
			String fileContents = "";
			for (String i : fileContentsList) {
				fileContents += i + "\n";
			}
			return fileContents;
		}
		catch (NoSuchFileException e) {
			return "";
		}
	}
	
	// File exists?
	public static boolean exists(String path) {
		File f = new File(path);
		return f.exists();
	}
	
	// Writes a string to a file
	public static void writeToFile (String file, boolean append, String text) throws IOException {
		FileWriter fileWriter = new FileWriter (file, append);
		BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
		bufferedWriter.write(text);
		bufferedWriter.close();
		fileWriter.close();
	}
	
	// Writes the data in textList to file, splitting each element with sepChar
	public static void writeToFile (String file, boolean append, List<String> textList, String sepChar) throws IOException {
		String text = "";
		for (String s : textList) {
			text += s + sepChar;
		}
		FileWriter fileWriter = new FileWriter (file, append);
		BufferedWriter bufferedWriter = new BufferedWriter (fileWriter);
		bufferedWriter.write(text);
		bufferedWriter.close();
		fileWriter.close();
	}
	
	// Used to delete a folder with files
	private static void delDirectory (String target) throws IOException {
		Path directory = Paths.get(target);
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	// Helper for zipping folders
    private static void zipDirectoryHelper (File rootDirectory, File currentDirectory, ZipOutputStream out, String topFolderName) throws Exception {
        byte[] data = new byte[2048];
        File[] files = currentDirectory.listFiles();
        if (files == null) {
          // no files were found or this is not a directory
        } else {
            for (File file : files) {
                if (file.isDirectory() && !file.getName().equals("")) {
                    zipDirectoryHelper(rootDirectory, file, out, topFolderName);
                } else {
                    FileInputStream fi = new FileInputStream(file);
                    // creating structure and avoiding duplicate file names
                    String name = file.getAbsolutePath().replace(rootDirectory.getAbsolutePath(), topFolderName);

                    ZipEntry entry = new ZipEntry(name);
                    out.putNextEntry(entry);
                    int count;
                    BufferedInputStream origin = new BufferedInputStream(fi,2048);
                    while ((count = origin.read(data, 0 , 2048)) != -1){
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
            }
        }
    }
    
    // Used to copy a directory (replacing existing files)
 	private static void copyDirectory (String src, String dest) throws IOException {
 		File source = new File (src);
 		File destination = new File (dest);
 	    if (source.isDirectory()) {
 	        if (!destination.exists()) {
 	            destination.mkdirs();
 	        }
 	        String files[] = source.list();
 	        for (String file : files) {
 	            File srcFile = new File(source, file);
 	            File destFile = new File(destination, file);
 	            copyDirectory(srcFile.toString(), destFile.toString());
 	        }
 	    } else {
 	        InputStream in = null;
 	        OutputStream out = null;
 	        try {
 	            in = new FileInputStream(source);
 	            out = new FileOutputStream(destination);
 	            byte[] buffer = new byte[1024];
 	            int length;
 	            while ((length = in.read(buffer)) > 0)
 	            {
 	                out.write(buffer, 0, length);
 	            }
 	        } catch (Exception e) {
 	            // Just ignore the error, it's to be expected
 	        }
 	        in.close();
 	        out.close();
 	    }
 	}
}
