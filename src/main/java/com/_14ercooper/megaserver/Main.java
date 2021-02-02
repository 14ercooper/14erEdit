package com._14ercooper.megaserver;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

	boolean updateArtifacts = false;

	while (true) {
	    try {
		// Parse arguments
		for (String s : args) {
		    if (s.equalsIgnoreCase("--force-offline")) {
			Artifacts.forceOffline = true;
		    }
		    if (s.equalsIgnoreCase("--update")) {
			updateArtifacts = true;
		    }
		}

		// Splash text
		clearConsole();
		System.out.println("14erEdit - Mapmaking Megaserver");
		System.out.println("Contributors on the GitHub. Licensed under GPL-3.0. All rights reserved.");
		System.out.println("Not affiliated with Mojang, Minecraft, or Microsoft.");
		System.out.println("This is free software. You should not have paid for this.");
		System.out.println("This software comes with no warranties.");
		Thread.sleep(5000);
		clearConsole();

		// First time setup
		if (!FileIO.exists("init.mms")) {
		    setup();
		}
		if (Artifacts.internetConnected()) {
		    if (updateArtifacts) {
			FileIO.deleteFile("artifacts", true);
			FileIO.makePath("artifacts");
			System.out.println("Redownloading all artifacts (this could take a LONG time)...");
		    }
		    Artifacts.updateArtifacts();
		    System.out.println("Updated all artifacts");
		}
		Artifacts.loadLocalArtifacts();

		// Pick a profile loop until exit
		while (true) {
		    clearConsole();
		    System.out.println("What profile?");
		    ArrayList<String> profiles = new ArrayList<String>();
		    profiles.addAll(FileIO.listFiles("profiles", true));
		    if (profiles.size() == 0) {
			newProfile();
			continue;
		    }
		    profiles.add("New profile");
		    profiles.add("Exit Server");
		    String profile = UserInput.fromList(profiles);
		    if (profile.equalsIgnoreCase("new profile"))
			profile = newProfile();
		    if (profile.equalsIgnoreCase("exit server"))
			return;
		    inProfile(profile);
		}
	    }
	    catch (Exception e) {
		onError(e);
	    }
	}
    }

    // What to do in profile function
    private static void inProfile(String profile) throws IOException, InterruptedException {
	// Loop until back selected
	while (true) {
	    // Get choice
	    clearConsole();
	    System.out.println("Options for " + profile + ":");
	    System.out.println("\t1) Start server");
	    System.out.println("\t2) Change version");
	    System.out.println("\t3) Change plugins");
	    System.out.println("\t4) Change RAM");
	    System.out.println("\t5) Delete backups");
	    System.out.println("\t6) Delete profile");
	    System.out.println("\t7) Back");
	    System.out.print("  > ");
	    int input = UserInput.numberRange(1, 7);
	    // Load vars from disk
	    ArrayList<String> vars = new ArrayList<String>();
	    vars.addAll(Arrays.asList(FileIO.readFromFile("profiles/" + profile + "/data.mms").split("&")));
	    String version = vars.get(0).replace("\n", "").replace("\r", "");
	    String RAM = "";
	    try {
		RAM = vars.get(1).replace("\n", "").replace("\r", "");
	    }
	    catch (IndexOutOfBoundsException e) {
		RAM = "2048";
	    }
	    List<String> plugins = new ArrayList<String>();
	    for (int i = 2; i < vars.size(); i++) {
		plugins.add(vars.get(i).replace("\n", "").replace("\r", ""));
	    }
	    // Start server
	    if (input == 1) {
		// Copy artifacts & 14erEdit data
		String jarPath = Artifacts.getArtifactPath("Server", version, "Paper");
		if (jarPath.equalsIgnoreCase("")) {
		    System.out.println("Could not locate jar file");
		    Thread.sleep(5000);
		}
		FileIO.copyFile(jarPath, "profiles/" + profile + "/server.jar", false);
		for (String s : plugins) {
		    String path = Artifacts.getArtifactPath("Plugin", version, s);
		    if (!path.equalsIgnoreCase("")) {
			FileIO.copyFile(path, "profiles/" + profile + "/plugins/" + s + ".jar", false);
		    }
		}
		FileIO.copyFile("14erEdit", "profiles/" + profile + "/plugins/14erEdit", true);
		// Start server
		String quarterRam = String.valueOf(Integer.parseInt(RAM) / 4);
		String eighthRam = String.valueOf(Integer.parseInt(RAM) / 8);
		String command = "java -jar -DIReallyKnowWhatIAmDoingISwear=true -Xmx" + RAM + "M -Xms" + RAM + "M -Xss"
			+ eighthRam + "M -Xmn" + quarterRam + "M -XX:+UseParallelGC server.jar nogui";
		Process p = runProcess(command, "profiles/" + profile);
		while (p.isAlive()) {
		    Thread.sleep(5000);
		    // Things to do while server is running can go here
		}
		// Clean up server & move 14erEdit data
		FileIO.copyFile("profiles/" + profile + "/plugins/14erEdit", "14erEdit", true);
		FileIO.deleteFile("profiles/" + profile + "/plugins/14erEdit", true);
		FileIO.deleteFile("profiles/" + profile + "/logs", true);
		FileIO.deleteFile("profiles/" + profile + "/server.jar", false);
		for (String s : plugins) {
		    FileIO.deleteFile("profiles/" + profile + "/plugins/" + s + ".jar", false);
		}
		// Make backup
		FileIO.zipDirectory(new File("profiles/" + profile), new File("backups"), profile,
			profile + "_" + String.valueOf(Instant.now().getEpochSecond()));
	    }
	    // Change version
	    if (input == 2) {
		List<String> versions = Artifacts.getVersions();
		System.out.println("What Minecraft version?");
		version = UserInput.fromList(versions);
		input = 3;
	    }
	    // Change plugins
	    if (input == 3) {
		plugins = new ArrayList<String>();
		while (true) {
		    System.out.println("Plugins to install:\nInstalled: ");
		    for (String s : plugins) {
			System.out.print(s + " ");
		    }
		    System.out.println();
		    System.out.println("Install which plugins?");
		    List<String> artifactList = Artifacts.getLocalArtifacts("Plugin", version);
		    List<String> pluginList = new ArrayList<String>();
		    for (String s : artifactList) {
			pluginList.add(s.split(";")[2]);
		    }
		    pluginList.add("Done");
		    String plugin = UserInput.fromList(pluginList);
		    if (plugin.equalsIgnoreCase("done"))
			break;
		    if (plugins.contains(plugin))
			plugins.remove(plugin);
		    else
			plugins.add(plugin);
		}
	    }
	    // Change RAM
	    if (input == 4) {
		System.out.println("How much RAM should the server use (MB)?");
		int max = 8192;
		RAM = String.valueOf(UserInput.numberRange(512, max));
	    }
	    // Delete backups
	    if (input == 5) {
		List<String> backups = FileIO.listFiles("backups", false);
		for (String s : backups) {
		    if (s.contains(profile)) {
			FileIO.deleteFile("backups/" + s, false);
		    }
		}
	    }
	    // Delete profile
	    if (input == 6) {
		System.out.println("Please type the profile name to confirm deletion.");
		String confirmDelete = UserInput.patternMatch("[A-Za-z0-9\\-_]");
		if (profile.contentEquals(confirmDelete)) {
		    FileIO.deleteFile("profiles/" + profile, true);
		    return;
		}
	    }
	    // Save vars to disk
	    String data = version + "&";
	    data += RAM + "&";
	    for (String s : plugins) {
		data += s + "&";
	    }
	    data = data.substring(0, data.length() - 1);
	    FileIO.writeToFile("profiles/" + profile + "/data.mms", false, data);
	    // Back to profile select
	    if (input == 7)
		return;
	}
    }

    // Make a new profile
    private static String newProfile() throws IOException {
	// Name
	System.out.println("Profile name? (alphanumeric only)");
	String name = UserInput.patternMatch("[A-Za-z0-9\\-_]");
	// Spigot version
	List<String> versions = Artifacts.getVersions();
	System.out.println("What Minecraft version?");
	String version = UserInput.fromList(versions);
	// Initial plugins
	Set<String> plugins = new HashSet<String>();
	while (true) {
	    System.out.println("Plugins to install:\nInstalled: ");
	    for (String s : plugins) {
		System.out.print(s + " ");
	    }
	    System.out.println();
	    System.out.println("Install which plugins?");
	    List<String> artifactList = Artifacts.getLocalArtifacts("Plugin", version);
	    List<String> pluginList = new ArrayList<String>();
	    for (String s : artifactList) {
		pluginList.add(s.split(";")[2]);
	    }
	    pluginList.add("Done");
	    String plugin = UserInput.fromList(pluginList);
	    if (plugin.equalsIgnoreCase("done"))
		break;
	    if (plugins.contains(plugin))
		plugins.remove(plugin);
	    else
		plugins.add(plugin);
	}
	// Save to profile folder
	String data = version + "&";
	data += "2048&";
	for (String s : plugins) {
	    data += s + "&";
	}
	data = data.substring(0, data.length() - 1);
	FileIO.makePath("profiles/" + name);
	FileIO.makePath("profiles/" + name + "/plugins");
	FileIO.writeToFile("profiles/" + name + "/data.mms", false, data);
	// Make EULA and properties files
	FileIO.writeToFile("profiles/" + name + "/" + "eula.txt", false, "eula=true");
	String serverProps = Properties.server + "level-name=" + name;
	FileIO.writeToFile("profiles/" + name + "/server.properties", false, serverProps);
	FileIO.writeToFile("profiles/" + name + "/spigot.yml", false, Properties.spigot);
	FileIO.writeToFile("profiles/" + name + "/paper.yml", false, Properties.paper);
	return name;
    }

    // First time setup
    private static void setup() throws Exception {
	if (!Artifacts.internetConnected()) {
	    System.out.println("Internet required for setup. Please connect to the internet.");
	    throw new Exception();
	}
	System.out.println("Performing first time setup...\nMaking directories...");
	FileIO.makePath("artifacts");
	FileIO.makePath("profiles");
	FileIO.makePath("backups");
	FileIO.makePath("14erEdit/Commands");
	FileIO.makePath("14erEdit/ops");
	FileIO.makePath("14erEdit/schematics");
	FileIO.makePath("14erEdit/vars");
	FileIO.makePath("14erEdit/templates");
	FileIO.makePath("14erEdit/multibrushes");
	FileIO.makePath("14erEdit/functions");
	System.out.println("Downloading artifacts (this could take a LONG time)...");
	Artifacts.updateArtifacts();
	FileIO.writeToFile("init.mms", false, "");
    }

    // Runs a process inside of the current console
    private static Process runProcess(String cmd, String dir) throws IOException {
	String[] cmdList = cmd.split(" ");
	ProcessBuilder pb = new ProcessBuilder(cmdList);
	pb.directory(new File(dir));
	return pb.inheritIO().start();
    }

    // Wipes the console clean
    private static void clearConsole() {
	System.out.print("\033[H\033[2J");
	System.out.flush();
	try {
	    if (System.getProperty("os.name").contains("Windows"))
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	    else
		Runtime.getRuntime().exec("clear");
	}
	catch (Exception ex) {
	}
    }

    private static void onError(Exception e) {
	try {
	    System.out.println("Something went wrong. Restarting in 5 seconds...");
	    String out = e.getMessage() + "\n";
	    e.printStackTrace();
	    for (StackTraceElement ste : e.getStackTrace()) {
		out += ste.toString() + "\n";
	    }
	    FileIO.writeToFile("error_" + String.valueOf(Instant.now().getEpochSecond()) + ".txt", false, out);
	    Thread.sleep(5000);
	}
	catch (Exception e1) {
	    System.out.println("Error");
	}
    }
}
