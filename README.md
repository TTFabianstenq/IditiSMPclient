# IditiSMP Client

Fabric utility client focused on **IditiSMP** (Shop Survival + CPvP).

Based on the 41 Client codebase, rebranded with a light blue theme.

## Features
- Full module system (combat, movement, ESP, HUD, etc.)
- ClickGUI (Right Shift)
- Config save/load
- Tuned for IditiSMP: mace effects, elytra trails, CPvP, crates/economy helpers

## Requirements
- JDK 21
- Fabric Loader + Fabric API for Minecraft 1.21.11

## Build
```bash
./gradlew build
```
Output: `build/libs/iditismp-client-*.jar`

## Install
1. Install Fabric for 1.21.11
2. Put Fabric API + this jar in `mods/`
3. Launch and press Right Shift for the GUI

## Theme
Light blue accents (#5DADE2 range). Drop your IditiSMP logo into `src/main/resources/assets/iditismp-client/icon.png`.

## Notes
This is a starting rebrand. Full module sources from the original 41 Client should be copied into `src/main/java/dev/iditismp/` (package rename recommended). Donut-specific modules can be removed or adapted.

MIT License.