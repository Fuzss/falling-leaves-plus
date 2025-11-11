# Falling Leaves Plus

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects)
and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/fallingleavesplus/banner.png)

## Configuration

This mod supports two configuration methods:

- **Global configuration** via the mod menu (used as the default and fallback).
- **Per-block configuration files** included in resource packs, which can override the global settings.

---

### Global settings

The global configuration is a standard text file located in `.minecraft/config`.  
Its fields mirror the per-block configuration options and act as **fallback values** whenever a setting is not defined
for a specific block.

---

### Per-block settings

You can optionally define **custom settings for individual leaves blocks** through resource packs.  
If no configuration file exists for a particular block, it will still generate particles, using the **global
configuration** instead.

It’s also possible to override only specific fields: any value not defined in the per-block file will default to the
corresponding global setting.

All per-block configuration files must be placed under:

> assets/<namespace>/fallingleavesplus/leaves/<path>.json

For example, to configure `minecraft:birch_leaves`, the file should be located at:

> assets/minecraft/fallingleavesplus/leaves/birch_leaves.json

---

#### Available fields

| Field                        | Allowed Values                                                                            | Description                                                                                                                                                 |
|------------------------------|-------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `collide_with_visual_shapes` | `true` / `false`                                                                          | If `true`, particles collide with block visual shapes instead of only their collision boxes.                                                                |
| `decay_on_ground_mode`       | `immediate` / `fast` / `slow`                                                             | Controls how quickly leaf particles disappear after landing on the ground.                                                                                  |
| `leaf_particle_chance`       | `0.0 ~ 1.0`                                                                               | The probability of a leaf particle spawning below a leaves block on each animation tick.                                                                    |
| `flow_away`                  | `true` / `false`                                                                          | Determines whether particles are affected by wind.                                                                                                          |
| `gravity_multiplier`         | `0.0 >=`                                                                                  | Scales the effect of gravity on falling leaf particles.                                                                                                     |
| `initial_falling_speed`      | `0.0 >=`                                                                                  | The initial downward velocity of a particle when it spawns.                                                                                                 |
| `leaf_size`                  | `0.0 >=`                                                                                  | Adjusts both the visual and physical size of leaf particles.                                                                                                |
| `lifetime_in_seconds`        | `0 >=`                                                                                    | The maximum lifetime (in seconds) of a particle before it disappears.                                                                                       |
| `rain_amplifier`             | `0.0 >=`                                                                                  | Increases spawn rate and wind strength during rain or snow.                                                                                                 |
| `spawn_snowflakes`           | `true` / `false`                                                                          | If `true`, spawns snowflakes instead of leaves when under snow-covered blocks.                                                                              |
| `swirl_around`               | `true` / `false`                                                                          | Enables swirling, circular motion while falling.                                                                                                            |
| `textures`                   | List of `ResourceLocation` or [Particle Texture Definition](#particle-texture-definition) | Defines one or more particle textures. A random one is chosen for each spawned particle. Can be simple resource IDs or complex objects with extra settings. |
| `thunderstorm_amplifier`     | `0.0 >=`                                                                                  | Increases particle spawn rate and wind strength during thunderstorms.                                                                                       |
| `wind_direction`             | `0.0 ~ 360.0`                                                                             | Wind direction in degrees (0° = north, 90° = east, etc.).                                                                                                   |
| `wind_strength`              | `0.0 >=`                                                                                  | Strength factor for wind affecting particle movement.                                                                                                       |

---

#### Particle texture definition

| Field           | Allowed Values                  | Description                                                                                                                                                            |
|-----------------|---------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `asset_id`      | `ResourceLocation`              | Path to the texture in `assets/<namespace>/textures/particle/<path>.png`. For example, `minecraft:leaf_0` resolves to `assets/minecraft/textures/particle/leaf_0.png`. |
| `texture_scale` | `0.0 >`                         | Scales the particle based on its texture. Multiplied by `leaf_size`.                                                                                                   |
| `tint_color`    | ARGB as `Integer` or `Vector4f` | A fixed tint color for particles, independent of biome color. Cannot be used together with `tinted`.                                                                   |
| `tinted`        | `true` / `false`                | Applies biome-based foliage coloring. Cannot be used together with `tint_color`.                                                                                       |

---

#### Example

All fields have default values where applicable.

```json
{
  "collide_with_visual_shapes": true,
  "decay_on_ground_mode": "fast",
  "leaf_particle_chance": 0.01,
  "flow_away": true,
  "gravity_multiplier": 0.07,
  "initial_falling_speed": 0.021,
  "leaf_size": 2.0,
  "lifetime_in_seconds": 15,
  "rain_amplifier": 1.0,
  "spawn_snowflakes": true,
  "swirl_around": true,
  "textures": [
    "minecraft:leaf_0",
    {
      "asset_id": "minecraft:leaf_1",
      "texture_scale": 1.0,
      "tint_color": -9399763
    },
    {
      "asset_id": "minecraft:leaf_2",
      "texture_scale": 1.0,
      "tint_color": [
        1.0,
        0.439,
        0.573,
        0.176
      ]
    },
    {
      "asset_id": "minecraft:leaf_3",
      "texture_scale": 1.0,
      "tinted": true
    }
  ],
  "thunderstorm_amplifier": 2.0,
  "wind_direction": 0.0,
  "wind_strength": 10.0
}
```
