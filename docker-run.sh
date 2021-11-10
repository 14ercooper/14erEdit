mkdir mms
chmod 755 mms
docker run -it -v `pwd`/mms:/14erEdit/mms -p 127.0.0.1:25565:25565/tcp ghcr.io/14ercooper/mapmaking_megaserver:1.0.1-java17

