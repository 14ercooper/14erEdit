# Compiling

This plugin can be built using maven.

Precompiled versions are available through the megaserver.

1. Build Spigot using BuildTools, making sure to build CraftBukkit
2. Download the entire source of the plugin, either as a zip or using git
3. Package using `mvn package`
4. The jar file will be in the folder `target`

## For Non-Latest Minecraft

You must update both the pom file to the correct Spigot, as well as the NBTExtractor class' Craftbukkit import version.
