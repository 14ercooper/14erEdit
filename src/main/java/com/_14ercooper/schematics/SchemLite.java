package com._14ercooper.schematics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com._14ercooper.worldeditor.blockiterator.BlockIterator;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.Main;

public class SchemLite {

    // The size of the schem lite
    int xSize, ySize, zSize;
    // Where this is on disk; within the 14erEdit folder
    String outPath;
    // Creator details
    String authorName, creationDate;
    // For storing the read/write pos
    BufferedReader reader;
    BufferedWriter writer;
    // Mirrors?
    boolean xMirror = false, yMirror = false, zMirror = false;
    int executionOrder = 0;
    // Set air?
    boolean setAir = true;

    // Create a new schem lite object
    public SchemLite(int x, int y, int z, String outPath, String author, String date) {
	this.xSize = x;
	this.ySize = y;
	this.zSize = z;
	this.outPath = "plugins/14erEdit/schematics/" + outPath;
	this.authorName = author;
	this.creationDate = date;
	try {
	    this.resetWrite();
	}
	catch (IOException e) {
	    Main.logDebug("Error initializing schematic file.");
	}
    }

    public SchemLite(String inPath, boolean setAir, int executionOrder) {
	this.outPath = "plugins/14erEdit/schematics/" + inPath;
	this.setAir = setAir;
	this.executionOrder = executionOrder;
    }

    // Get a block iterator corresponding with this schem lite
    public BlockIterator getIterator(int xStart, int yStart, int zStart) {
	// Create a block iterator for the cube
	int xS = xStart;
	int yS = yStart;
	int zS = zStart;
	int xE = xStart + xSize - 1;
	int yE = yStart + ySize - 1;
	int zE = zStart + zSize - 1;
	if (xMirror) {
	    int temp = xS;
	    xS = xE;
	    xE = temp;
	}
	if (yMirror) {
	    int temp = yS;
	    yS = yE;
	    yE = temp;
	}
	if (zMirror) {
	    int temp = zS;
	    zS = zE;
	    zE = temp;
	}
	List<String> iterArgs = new ArrayList<String>();
	iterArgs.add(Integer.toString(xS));
	iterArgs.add(Integer.toString(yS));
	iterArgs.add(Integer.toString(zS));
	iterArgs.add(Integer.toString(xE));
	iterArgs.add(Integer.toString(yE));
	iterArgs.add(Integer.toString(zE));
	iterArgs.add(Integer.toString(executionOrder));
	return GlobalVars.iteratorManager.getIterator("cube").newIterator(iterArgs);
    }

    // Reset the write position of the schem lite object
    public void resetWrite() throws IOException {
	// Delete the file
	Files.deleteIfExists(Paths.get(outPath));
	// Create a new file with the metadata
	writer = Files.newBufferedWriter(Paths.get(outPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
		StandardOpenOption.APPEND);
	writer.write(Integer.toString(xSize));
	writer.newLine();
	writer.write(Integer.toString(ySize));
	writer.newLine();
	writer.write(Integer.toString(zSize));
	writer.newLine();
	writer.write(authorName);
	writer.newLine();
	writer.write(creationDate);
	writer.newLine();
	writer.close();
    }

    // Add a block to the schem lite object
    public void writeBlock(String material, String data, String nbt) throws IOException {
	writer = Files.newBufferedWriter(Paths.get(outPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
		StandardOpenOption.APPEND);
	writer.write(material);
	writer.newLine();
	writer.write(data);
	writer.newLine();
	writer.write(nbt.replaceAll("[\\n\\r]", "新しい線"));
	writer.newLine();
	writer.close();
    }

    public void writeBlock(String[] block) throws IOException {
	// Wrapper for overloaded function
	writeBlock(block[0], block[1], block[2]);
    }

    // Open the reader
    public void openRead() throws IOException {
	// Reload the file buffer
	reader = Files.newBufferedReader(Paths.get(outPath));
	// Refresh the metadata
	xSize = Integer.parseInt(reader.readLine());
	ySize = Integer.parseInt(reader.readLine());
	zSize = Integer.parseInt(reader.readLine());
	authorName = reader.readLine();
	creationDate = reader.readLine();
	Main.logDebug("Opened read");
    }

    // Get the next block in this schem lite
    public String[] readNext() throws IOException {
	// Load the next three pieces of data into the array
	String[] array = { reader.readLine(), reader.readLine(), reader.readLine() };
	if (array[2] != null) {
	    array[2] = array[2].replaceAll("新しい線", "\n");
	}
	return array;
    }

    // Close the reader
    public void closeRead() throws IOException {
	reader.close();
    }

    // Move a schem lite object's disk reference
    public void moveRef(String newPath) throws IOException {
	// Move the file
	Files.move(Paths.get(outPath), Paths.get("plugins/14erEdit/schematics/" + newPath),
		StandardCopyOption.REPLACE_EXISTING);
	// Update the path
	outPath = "plugins/14erEdit/" + newPath;
    }

    // Clone the schem lite's data to a persistent file
    public void cloneTo(String path) throws IOException {
	// Copy the file
	Files.copy(Paths.get(outPath), Paths.get("plugins/14erEdit/schematics/" + path),
		StandardCopyOption.REPLACE_EXISTING);
    }

    // Load the schem lite's data from a persistent file
    public void loadFrom(String path) throws IOException {
	// Clone the new file
	Files.copy(Paths.get("plugins/14erEdit/schematics/" + path), Paths.get(outPath),
		StandardCopyOption.REPLACE_EXISTING);
    }

    // Mirror the schem lite
    public void mirror(boolean x, boolean y, boolean z) {
	xMirror = x;
	yMirror = y;
	zMirror = z;
    }

    // Get dimensions
    public int getXSize() {
	return xSize;
    }

    public int getYSize() {
	return ySize;
    }

    public int getZSize() {
	return zSize;
    }

    // Is this marked as setting air?
    public boolean setAir() {
	return setAir;
    }
}
