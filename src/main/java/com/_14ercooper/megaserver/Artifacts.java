package com._14ercooper.megaserver;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Artifacts {

    // Offline mode?
    public static boolean forceOffline = false;

    // Root of artifacts
    public static final String artifactsBase = "https://files.14erc.com/edit/mms/";
    // Map of artifacts by type
    private static List<String> buildTools = new ArrayList<String>();
    private static List<List<String>> servers = new ArrayList<List<String>>();
    private static List<List<String>> plugins = new ArrayList<List<String>>();

    // Local artifacts
    // Format in file Type;MinecraftVersion;Name;File;Version
    private static Map<String, Integer> localArtifacts = new HashMap<String, Integer>();

    // List all artifacts of a certain type
    public static List<String> getVersions() {
	Set<String> versions = new HashSet<String>();
	for (Entry<String, Integer> entry : localArtifacts.entrySet()) {
	    versions.add(entry.getKey().split(";")[1]);
	}
	List<String> toReturn = new ArrayList<String>();
	for (String s : versions) {
	    if (s.equalsIgnoreCase("all"))
		continue;
	    toReturn.add(s);
	}
	return toReturn;
    }

    // Get the file path of the artifact
    public static String getArtifactPath(String type, String version, String name) {
	String key = type + ";" + version + ";" + name;
	for (Entry<String, Integer> entry : localArtifacts.entrySet()) {
	    if (entry.getKey().contains(key)) {
		return "artifacts/" + entry.getKey().split(";")[3];
	    }
	}
	return "";
    }

    // Load local artifacts into the file
    public static void loadLocalArtifacts() throws IOException {
	localArtifacts = new HashMap<String, Integer>();
	if (!FileIO.exists("artifacts/local.mms"))
	    return;
	String[] artifacts = FileIO.readFromFile("artifacts/local.mms").split("&");
	for (String s : artifacts) {
	    List<String> data = new ArrayList<String>();
	    data.addAll(Arrays.asList(s.split(";")));
	    String data4 = data.get(4);
	    data4 = data4.replace("\n", "").replace("\r", "");
	    int ver = Integer.parseInt(data4);
	    data.remove(4);
	    String dat = "";
	    for (int i = 0; i < 4; i++) {
		dat += data.get(i) + ";";
	    }
	    dat = dat.substring(0, dat.length() - 1);
	    localArtifacts.put(dat, ver);
	}
    }

    // Save local artifacts to file
    public static void saveLocalArtifacts() throws IOException {
	String toSave = "";
	for (Entry<String, Integer> e : localArtifacts.entrySet()) {
	    toSave += e.getKey() + ";" + e.getValue() + "&";
	}
	toSave = toSave.substring(0, toSave.length() - 1);
	FileIO.writeToFile("artifacts/local.mms", false, toSave);
    }

    // Get version of a local artifact
    private static int getLocalVersion(String type, String version, String name) {
	String key = type + ";" + version + ";" + name;
	for (Entry<String, Integer> entry : localArtifacts.entrySet()) {
	    if (entry.getKey().contains(key))
		return entry.getValue();
	}
	return 0;
    }

    // Start tracking a local artifact
    private static void addLocalArtifact(String type, String version, String name, String file, int ver) {
	String key = "_" + type + ";" + version + ";" + name + ";" + file;
	localArtifacts.put(key, ver);
    }

    // Download updates to all artifacts
    public static void updateArtifacts() throws InterruptedException, IOException {
	if (!internetConnected()) {
	    System.out.println("This function requires internet connectivity");
	    Thread.sleep(5000);
	    return;
	}
	updateNetworkArtifactsList();
	loadLocalArtifacts();
	if (Integer.parseInt(buildTools.get(1)) > getLocalVersion("BuildTools", "All", "BuildTools")) {
	    System.out.println("Downloading BuildTools");
	    downloadFromURL(artifactsBase + buildTools.get(0), "artifacts/BuildTools.jar");
	    addLocalArtifact("BuildTools", "All", "BuildTools", "BuildTools.jar", Integer.parseInt(buildTools.get(1)));
	}
	for (List<String> s : servers) {
	    if (Integer.parseInt(s.get(3)) > getLocalVersion("Server", s.get(0), s.get(1))) {
		System.out.println("Downloading server " + s.get(0) + " " + s.get(1));
		downloadFromURL(artifactsBase + s.get(2), "artifacts/" + s.get(2));
		addLocalArtifact("Server", s.get(0), s.get(1), s.get(2), Integer.parseInt(s.get(3)));
	    }
	}
	for (List<String> s : plugins) {
	    if (Integer.parseInt(s.get(3)) > getLocalVersion("Plugin", s.get(0), s.get(1))) {
		System.out.println("Downloading plugin " + s.get(0) + " " + s.get(1));
		downloadFromURL(artifactsBase + s.get(2), "artifacts/" + s.get(2));
		addLocalArtifact("Plugin", s.get(0), s.get(1), s.get(2), Integer.parseInt(s.get(3)));
	    }
	}
	saveLocalArtifacts();
    }

    public static List<String> getLocalArtifacts(String type, String version) {
	String key = type + ";" + version;
	List<String> artifacts = new ArrayList<String>();
	for (Entry<String, Integer> entry : localArtifacts.entrySet()) {
	    if (entry.getKey().contains(key)) {
		artifacts.add(entry.getKey());
	    }
	}
	return artifacts;
    }

    // Check for internet connectivity
    public static boolean internetConnected() {
	if (forceOffline)
	    return false;
	try {
	    URL url = new URL(artifactsBase + "Artifacts.txt");
	    URLConnection connection = url.openConnection();
	    connection.connect();
	    return true;
	}
	catch (Exception e) {
	    return false;
	}
    }

    // Grab new artifacts from the network
    private static void updateNetworkArtifactsList() throws IOException {
	ArrayList<String> rows = new ArrayList<String>();
	buildTools = new ArrayList<String>();
	servers = new ArrayList<List<String>>();
	plugins = new ArrayList<List<String>>();
	rows.addAll(Arrays.asList(getTextFromURL(artifactsBase + "Artifacts.txt").split("&")));
	int found = 0;
	while (rows.size() > 0) {
	    String[] data = rows.get(0).split(";");
	    found++;
	    rows.remove(0);
	    if (data[0].equalsIgnoreCase("artifacts")) {
		if (data[1].equalsIgnoreCase("this"))
		    continue;
		else
		    rows.addAll(Arrays.asList(getTextFromURL(data[1]).split("&")));
	    }
	    if (data[0].equalsIgnoreCase("buildtools")) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(data));
		list.remove(0);
		buildTools = list;
	    }
	    if (data[0].equalsIgnoreCase("server")) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(data));
		list.remove(0);
		servers.add(list);
	    }
	    if (data[0].equalsIgnoreCase("plugin")) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(data));
		list.remove(0);
		plugins.add(list);
	    }
	}
	System.out.println("Found " + found + " artifacts.");
    }

    // Returns the contents of a URL as a string
    private static String getTextFromURL(String url) throws IOException {
	URL website = new URL(url);
	URLConnection connection = website.openConnection();
	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	StringBuilder response = new StringBuilder();
	String inputLine;
	while ((inputLine = in.readLine()) != null)
	    response.append(inputLine);
	in.close();
	return response.toString();
    }

    // Downloads the file at the URL to the target
    public static void downloadFromURL(String sourceURL, String target) throws IOException {
	URL website = new URL(sourceURL);
	ReadableByteChannel rbc = Channels.newChannel(website.openStream());
	FileOutputStream fos = new FileOutputStream(target);
	fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	fos.close();
    }
}
