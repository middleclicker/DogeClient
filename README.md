# Doge Client
A Minecraft utility client for 1.12.2, used by shiba inus across the world!

![DogeLogo](https://user-images.githubusercontent.com/60602265/136652986-0e5acb40-581a-49b5-8281-9e1a2d3084ec.jpg)

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
    * Just rewrite...

# Instruction on setting up dev environment for Intellij

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

Wait for the command to complete, then open build.gradle in Intellij. Remember to open as project.

Wait for Intellij to finish building. Then go to the top right corner and click "Add run configurations". On the top left corner click the "+" sign.
Select "Application" from the dropdown.

For older versions of intellij:

![image](https://user-images.githubusercontent.com/60602265/137077941-f09492dc-6d11-4b88-8912-2d6a26531f43.png)

For newer versions of Intellij:
~~I'm too lazy to install it use your braincells~~

Developer: middleclicker person#9990, d04#8022, il coma#1427
