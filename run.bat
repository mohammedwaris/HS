@echo off
set JARS=jars\common\*;jars\excel-streaming-reader\*;jars\opencsv-4.6.jar;jars\commons-lang3-3.8.1.jar;jars\jackson-core-2.9.8.jar;jars\mapdb-3.0.7.jar;jars\guava-27.1-jre.jar;jars\lz4-1.3.0.jar;jars\kotlin-stdlib-1.3.31.jar;jars\eclipse-collections-10.0.0.M3.jar;jars\eclipse-collections-api-10.0.0.M3.jar;jars\elsa-3.0.0-M5.jar;jars\commons-math3-3.6.1.jar;jars\poi-4.0.1.jar;jars\commons-codec-1.12.jar;jars\commons-collections4-4.3.jar;jars\poi-ooxml-4.0.1.jar;jars\curvesapi-1.05.jar;jars\commons-compress-1.18.jar;jars\poi-ooxml-schemas-4.0.1.jar;jars\xmlbeans-3.0.2.jar;jars\zstd-jni-1.3.3-3.jar;jars\dec-0.1.2.jar;jars\xz-1.8.jar;jars\org.osgi.core-6.0.0.jar
REM java -Xms256m -Xmx1024m -classpath ".;%JARS%;build;practice" Test
java -Xms256m -Xmx1024m  -classpath ".;%JARS%;build;practice" com.silverbrain.hs.core.DataSetView

echo on