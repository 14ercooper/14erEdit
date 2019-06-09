@echo off
call mvn package
xcopy target\14erEdit-0.0.5-SNAPSHOT.jar D:\Caleb\SpigotServer\plugins /y
echo(
echo ----  Update Completed  ----
echo( 
pause