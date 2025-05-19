@echo off

if not exist bin mkdir bin

javac -d bin src\object\*.java src\utils\*.java src\algo\*.java src\Main.java

java -cp bin Main
