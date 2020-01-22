@echo off

REM java -classpath ".;jars\opencsv-4.6.jar;jars\commons-lang3-3.8.1.jar;build" -d "build" src\com\rqr\core\*.java

java -classpath ".;jars\opencsv-4.6.jar;build;" RQRDemo
echo on