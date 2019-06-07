package com.fourteener.schematics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.LinkedList;

/* Material-based schematics system for Minecraft 1.13+
 * Created by the Logan Cooper, 2019.
 * Liscenced under GPL-3.0, available from https://www.gnu.org/licenses/
 * */

// A material-based schematic system for Minecraft
@SuppressWarnings("serial")
public class Schematic implements Serializable {
	private static int formatVersion = 1;		// Stores the format of this schematic
	private int[] origin = {0, 0, 0};			// Stores the origin/offset of this schematic
	private int[] dimensions = {0, 0, 0};		// Stores the dimensions of this schematic
	String author = "Unset", name = "Unset";	// Stores the author and name of this schematic
	private LinkedList<String> blockData;		// This is where the block data of the schematic gets stored
	
	/* On the storage of block data using this format
	 * The blocks should be stored in a LinkedList<String>
	 * The block located at (x,y,z), as measured from the most negative corner of the schematic, should
	 * 		be located at position x + z * dimensions[0] + y * dimensions[2] * dimensions[0] within the list
	 * All elements of the list should be formatted using prefixed block state notation. This looks like
	 * 		minecraft:leaves[persistent=true]; changed based on what block is being stored.
	 * After the creation of a schematic file, the file should not be modified.
	 * */
	
	// A constructor for a schematic
	public Schematic (int originX, int originY, int originZ, int sizeX, int sizeY, int sizeZ, LinkedList<String> blocks) {
		origin[0] = originX;
		origin[1] = originY;
		origin[2] = originZ;
		dimensions[0] = sizeX;
		dimensions[1] = sizeY;
		dimensions[2] = sizeZ;
		blockData = blocks;
	}

	// A constructor for a schematic
	public Schematic (String schematicName, String schematicAuthor, int originX, int originY, int originZ, int sizeX, int sizeY, int sizeZ, LinkedList<String> blocks) {
		origin[0] = originX;
		origin[1] = originY;
		origin[2] = originZ;
		dimensions[0] = sizeX;
		dimensions[1] = sizeY;
		dimensions[2] = sizeZ;
		name = schematicName;
		author = schematicAuthor;
		blockData = blocks;
	}

	// A constructor for a schematic
	// The arrays should be arranged as {x,y,z} and only have 3 elements
	public Schematic (int[] schematicOrigin, int[] schematicDimensions, LinkedList<String> blocks) {
		origin = schematicOrigin;
		dimensions = schematicDimensions;
		blockData = blocks;
	}

	// A constructor for a schematic
	// The arrays should be arranged as {x,y,z} and only have 3 elements
	public Schematic (String schematicName, String schematicAuthor, int[] schematicOrigin, int[] schematicDimensions, LinkedList<String> blocks) {
		origin = schematicOrigin;
		dimensions = schematicDimensions;
		name = schematicName;
		author = schematicAuthor;
		blockData = blocks;
	}

	// A constructor for a schematic
	public Schematic (int sizeX, int sizeY, int sizeZ, LinkedList<String> blocks) {
		dimensions[0] = sizeX;
		dimensions[1] = sizeY;
		dimensions[2] = sizeZ;
		blockData = blocks;
	}

	// A constructor for a schematic
	public Schematic (String schematicName, String schematicAuthor, int sizeX, int sizeY, int sizeZ, LinkedList<String> blocks) {
		dimensions[0] = sizeX;
		dimensions[1] = sizeY;
		dimensions[2] = sizeZ;
		name = schematicName;
		author = schematicAuthor;
		blockData = blocks;
	}

	// A constructor for a schematic
	// The array should be arranged as {x,y,z} and only have 3 elements
	public Schematic (int[] schematicDimensions, LinkedList<String> blocks) {
		dimensions = schematicDimensions;
		blockData = blocks;
	}

	// A constructor for a schematic
	// The array should be arranged as {x,y,z} and only have 3 elements
	public Schematic (String schematicName, String schematicAuthor, int[] schematicDimensions, LinkedList<String> blocks) {
		dimensions = schematicDimensions;
		name = schematicName;
		author = schematicAuthor;
		blockData = blocks;
	}
	
	// Returns the format version of this schematic
	// Schematics should not be loaded using a Schematic.class of a different version
	public int getVersion () {
		return formatVersion;
	}
	
	// Returns the origin of this schematic. The list should have 3 elements.
	public int[] getOrigin () {
		return origin;
	}
	
	// Returns the dimensions of this schematic. The list should have 3 elements.
	public int[] getDimensions () {
		return dimensions;
	}
	
	// Returns the block data of this schematic
	public LinkedList<String> getBlocks () {
		return blockData;
	}
	
	// Returns the name of this schematic, or "Unset" if not set
	public String getName () {
		return name;
	}
	
	// Returns the name of this schematic, or "Unset" if not set
	public String getAuthor () {
		return author;
	}
	
	// Save this schematic to the specified path on disk
	// No file extension should be specified
	// Returns true upon a successful save, false otherwise
	public boolean saveSchematic (String filePath) {
		filePath = filePath.concat(".matschem");
		(new File(filePath)).mkdirs();
		try {
			// Delete any schematics existing at the location
			Files.deleteIfExists((new File(filePath).toPath()));
			// Create an object output stream and output the schematic
	        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(filePath));
	        oo.writeObject(this);
	        oo.close();
	        return true;
		} catch (FileNotFoundException e) {
			System.out.println ("[Schematic] File not found or could not be created while saving schematic.");
			return false;
		} catch (IOException e) {
			System.out.println ("[Schematic] File I/O error while saving schematic.");
			return false;
		}
	}
	
	// Get the version compatibility of the schematic loader
	// If the schematic loaded has a version not in this list, it should not be used and instead loaded with a compatible loader
	public static int[] getLoaderVersion () {
		int[] fV = {1};
		return fV;
	}
	
	// Load a schematic from the specified path on disk
	// No file extension should be specified
	// Returns a schematic upon successful load, null otherwise
	public static Schematic loadSchematic (String filePath) {
		System.out.println((new File(filePath).toPath()).toString());
		Schematic schem = null;
		try {
			ObjectInputStream oi = new ObjectInputStream(new FileInputStream(filePath));
	    	Object schematicObj = oi.readObject();
	    	schem = (Schematic) schematicObj;
	    	oi.close();
		} catch (FileNotFoundException e) {
			System.out.println("[Schematic] File not found while loading schematic");
		} catch (IOException e) {
			System.out.println("[Schematic] File I/O error while loading schematic");
		} catch (ClassNotFoundException e) {
			System.out.println("[Schematic] Class not found while loading schematic");
		}
		return schem;
	}
}
