# Doge Client
A Minecraft utility client for 1.12.2, used by shiba inus across the world!

<img src="https://img.shields.io/github/downloads/middleclicker/DogeClient/total" alt="dwnlds" />

![DogeLogo](https://user-images.githubusercontent.com/60602265/136652986-0e5acb40-581a-49b5-8281-9e1a2d3084ec.jpg)

# Todos
  - Click Gui:
    * Add more themes, hopefully a custom one
  - Modules
    * Rewrite (Auto Armour).
    * Rewrite (Tracers).
    * Add more modes (Fly).
    * Fix bug where the sky will flash when Time option is on (No Render).
    * Write my own auto crystal (learning)
  - General
    * Fix a ton of stuff

# List of Pain...
  - Add baritone
  - Make autocrystal

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

Developers: middleclicker person#9990

Credits:
  - Gamesense for click gui & tracers (Rewrite soon)
  - ~~Postman for config system (Rewrite soon)~~ Rewritten
  - Postman for Auto Crystal (lol i know postman autocrystal...)
