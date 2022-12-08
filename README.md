# ScaryBlock Forge mod - 1.16.5

## To develop / test

Using IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: `gradlew genIntellijRuns` (`./gradlew genIntellijRuns` if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.
5. Can then run `gradlew runClient` (`./gradlew runClient` if you are on Mac/Linux) to open Minecraft client with mod 
loaded

## Keybinds
- 'k' 
  - press while holding 'Red Sun' to shoot an asteroid
- 'm'
    - press while 'Huggy Wuggy Feet' is equipped to double jump
- 'n'
    - press while 'Entity303 Eye' is equipped to shoot a laser
 
## Commands

Commands:
- /setsoulsescaped [1-666] 
  - Sets amount of souls that have escaped on the boss bar
- /injection pause
  - Pauses the 'Time til injection' timer, if it is running
- /injection set [time]
  - Sets the time (in seconds) for the current 'Time til injection' timer, if it is running
- /setcut [1-2]
  - Set the current 'cut'. Resets the state back to the beginning of that 'cut'.
  - Must use /setcut to progress with the drops, certain items drop in certain cuts
    - Cut 1:
      - Sirenhead Pickaxe
      - HuggyWuggy Feet
      - Red Sun
      - Vllr Staff
      - Entity303 Eye
      - Blood Golem
    - Cut 2:
      - Ghostbuster Gun
      - Giant Alex
      - Entity Spawner