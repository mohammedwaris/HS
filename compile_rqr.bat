@echo off

javac -classpath ".;jars\opencsv-4.6.jar;jars\commons-lang3-3.8.1.jar;build" -d "build" src\com\rqr\core\*.java

javac -classpath ".;jars\opencsv-4.6.jar;build;" *.java 
echo on