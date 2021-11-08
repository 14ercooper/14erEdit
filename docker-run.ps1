if (!(Test-Path 'mms')) {
	New-Item -Path 'mms' -ItemType Directory
}
docker run -it -v $pwd/mms:/14erEdit/mms -p 127.0.0.1:25565:25565/tcp mapmaking_megaserver:0.0.4

