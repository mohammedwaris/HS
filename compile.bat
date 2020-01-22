@echo off
set JARS=jars\common\*;jars\excel-streaming-reader\*;jars\opencsv-4.6.jar;jars\commons-lang3-3.8.1.jar;jars\jackson-core-2.9.8.jar;jars\mapdb-3.0.7.jar;jars\kotlin-stdlib-1.3.31.jar;jars\eclipse-collections-10.0.0.M3.jar;jars\eclipse-collections-api-10.0.0.M3.jar;jars\commons-math3-3.6.1.jar;jars\poi-4.0.1.jar;jars\commons-codec-1.12.jar;jars\commons-collections4-4.3.jar;jars\poi-ooxml-4.0.1.jar;jars\curvesapi-1.05.jar;jars\commons-compress-1.18.jar;jars\poi-ooxml-schemas-4.0.1.jar;jars\xmlbeans-3.0.2.jar
set OUTPUT_DIR=build
set PARSO=src/com/epam/parso/*.java
set PARSO_IMPL=src/com/epam/parso/impl/*.java
set HS_CORE=src/com/silverbrain/hs/core/*.java
set HS_CORE_IO=src/com/silverbrain/hs/core/io/*.java
set SB=src/com/silverbrain/*.java
set STUDIO_APPLICATION=src/com/silverbrain/studio/application/*.java
set STUDIO_CONTENT=src/com/silverbrain/studio/content/*.java
set ALL_PACKAGE=%PARSO% %PARSO_IMPL% %HS_CORE% %HS_CORE_IO% %SB% %STUDIO_APPLICATION% %STUDIO_CONTENT%
javac -cp "%JARS%" %ALL_PACKAGE% -d %OUTPUT_DIR%
echo on