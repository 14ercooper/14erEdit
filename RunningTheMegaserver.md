## Running The Megaserver

#### Docker (Reccomended)

This method is reccomended since it ensures a consistent environment for running the Megaserver, including a known Java installation that is separate from your local Java installation, and a known setup for the rest of the runtime environment. Additionally, only bugs within the Megaserver or 14erEdit that are able to be replicated within this environment will be targeted/considered for a fix.

1) Set up and install Docker (on Windows, this can be done with Docker Desktop. On Mac/Linux, you can just directly install Docker. Google can help a lot with this step).
2) Run the Mapmaking Megaserver docker image, mounting a local directory to /14erEdit/mms within the container (this is where all megaserver files will be stored), and exposing port 25565/tcp to some port on your local machine. You also need to enable the interactive shell and tty on the docker run. The docker image for the Mapmaking Megaserver can be found at: `ghcr.io/14ercooper/mapmaking_megaserver:latest`
3) As a helper, a [shell script (Mac and Linux)](https://github.com/14ercooper/14erEdit/blob/master/docker-run.sh) and a [powershell script (Windows)](https://github.com/14ercooper/14erEdit/blob/master/docker-run.ps1) have been provided to run docker with the correct image, a directory `mms` within the working directory of the script mapped to the container, and 25565/tcp in the container mapped to 25565 on the machine. Basically, these scripts should work out-of-the-box for 99% of use cases. Just put the script somewhere and run it.

Notice: This container will only work for Minecraft 1.17.1 and higher, due to Mojang and Spigot doing a lot of strange things with Java versions. If you want to run the megaserver for an older version of Minecraft, please use the alternative image: `ghcr.io/14ercooper/mapmaking_megaserver:java11`. You can also update either script (or duplicate them and edit the copy) to use this image.

The full list of images and tags can be found [here](https://github.com/14ercooper/14erEdit/pkgs/container/mapmaking_megaserver)

#### Manual Install

1) Install Java. If you're going to be using Minecraft  1.17.1 or newer, use Java 17. Otherwise, use Java 11. You can download Java from [Adoptium](https://adoptium.net/), or another vendor of your choice. Make sure to install it in a known location, or set it to the system path.
2) Download the latest jar file for 14erEdit from [the GitHub releases page](https://github.com/14ercooper/14erEdit/releases)
3) Place the jar file in it's own folder - it'll generate a large number of files when run relative to the jar file.
4) Open a command prompt, terminal, or equivalent shell and set the working directory to the same folder as the 14erEdit jar. This can be accomplished using a shell script, right click (linux), shift-right-click (windows), or similar. This can also be accomplished using a `cd` command or similar.
5) Run the 14erEdit jar with Java. e.g. `java -jar 14erEdit.jar`

Multiple Java Versions:
- The Megaserver/14erEdit jar must be run with Java 11 or newer
- If you would like to have multiple Java installs in order to use incompatable Minecraft versions, you can pass the `--javaPath` argument to the Megaserver, providing the path to the java executable to run (e.g. `/java-installs/17/bin/java`). Thus, a full command would look like `java -jar 14erEdit.jar --javaPath=/java-installs/17/bin/java`.
