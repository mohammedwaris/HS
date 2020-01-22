javac -classpath ".:jars/opencsv-3.3.jar:jars/slf4j-api-1.7.26.jar:jars/slf4j-simple-1.7.26.jar:build" -d "build" src/com/epam/parso/*.java


javac -classpath ".:jars/opencsv-3.3.jar:jars/slf4j-api-1.7.26.jar:jars/slf4j-simple-1.7.26.jar:build" -d "build" src/com/epam/parso/impl/*.java

javac -classpath ".:jars/opencsv-3.3.jar:jars/commons-lang3-3.8.1.jar:jars/jmetro-5.3.jar;jars/mapdb-3.0.7.jar;build" -d "build" src/com/silverbrain/hs/core/*.java

javac -classpath ".:jars/opencsv-3.3.jar:jars/controlsfx-8.40.12.jar:jars/richtextfx-fat-0.9.3.jar:build" -d "build" src/com/silverbrain/hs/*.java

javac -classpath ".:jars/opencsv-3.3.jar:build" -d "build" src/com/silverbrain/studio/content/*.java 
javac -classpath ".:jars/opencsv-3.3.jar:build" -d "build" src/com/silverbrain/studio/application/*.java

javac -classpath ".:jars/opencsv-3.3.jar:build" practice/*.java

javac -classpath ".:jars/opencsv-3.3.jar:build" *.java


