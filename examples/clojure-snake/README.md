Simple Snake game.

![](extras/screenshot.png)

## Pre-requisite

- JDK 11
- Clojure 1.10.1
- `clojure` command-line tool 1.10.1.727 or later

## Configuring Skija dependency

Linux/Windows: Update all LWJGL dependencies:

`natives-macos` -> `natives-linux` / `natives-windows`

All: Update Skija dependency to the current version & platform. Replace `org.jetbrains.skija/skija-native {:mvn/version "0.0.0-SNAPSHOT"}` with one of:

Artifact ID         | `mvn/version`
--------------------|-------------
`skija-macos-x64`   | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-macos-x64%2Fmaven-metadata.xml&query=//release)
`skija-macos-arm64` | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-macos-arm64%2Fmaven-metadata.xml&query=//release)
`skija-linux`       | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-linux%2Fmaven-metadata.xml&query=//release)
`skija-windows`     | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-windows%2Fmaven-metadata.xml&query=//release)

Alternatively, use locally built version:

`org.jetbrains.skija/skija-native {:mvn/version "0.0.0-SNAPSHOT"}`

## Running

From CLI run the below command:

Windows/Linux:

```sh
clj -M -m snake.main
```

macOS:

```sh
clj -J-XstartOnFirstThread -M -m snake.main
```
