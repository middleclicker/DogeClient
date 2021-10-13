# Doge Client
A Minecraft utility client for 1.12.2, used by shiba inus across the world!

![DogeLogo](https://user-images.githubusercontent.com/60602265/136652986-0e5acb40-581a-49b5-8281-9e1a2d3084ec.jpg)

# Bugs
  - ~~HUD modules don't work~~ Fixed
  - ~~Tracers don't target properly~~ Fixed

# Todos
  - Click Gui:
    * Add more themes, hopefully a custom one
    * Add Gui config loader & saver
  - Modules
    * Rewrite Auto Armour
    * Rewrite Tracers
    * Rewrite Save Load Config
    * Add more modes to Fly
    * Add View Model
    * ~~Make Reach module~~ Added
  - General
    * ~~Add friend function~~ Added
  - Config
    * Save & load the following:
      1. Colors
    * Just rewrite...

# Instruction on setting up dev environment

Requirements:
  - Intellij
  - Java 8

Download the repository, cd into the folder.

If on windows,

`gradlew setupDecompWorkspace idea` (Command Prompt)

`./gradlew setupDecompWorkspace idea` (Powershell)

If on mac,

`./gradlew setupDecompWorkspace idea`

If on linux,

`./gradlew setupDecompWorkspace idea`


If you get the problem `./gradlew: Permission denied`, then run `chmod +x gradlew`.

Developer: middleclicker person#9990, d04#8022, il coma#1427
