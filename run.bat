@echo off

if not exist bin mkdir bin

javac -d bin src\main\java\object\*.java src\main\java\utils\*.java src\main\java\algo\*.java src\main\java\cli\Main.java

java -cp bin cli.Main
