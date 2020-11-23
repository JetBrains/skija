Demonstration of Skija APIs from Clojure.

## Pre-requisite

- JDK 11
- Clojure 1.10.1
- `clojure` command-line tool 1.10.1.727 or later

## Configuring Skija dependency

Clojure example requires latest master build of Skija installed locally:

```sh
./script/install.sh
```

If you want to use pre-built Skija, in `deps.edn` replace `org.jetbrains.skija/skija-native {:mvn/version "0.0.0-SNAPSHOT"}` with one of:

Artifact ID         | `mvn/version`
--------------------|-------------
`skija-macos`       | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-macos%2Fmaven-metadata.xml&query=//release)
`skija-linux`       | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-linux%2Fmaven-metadata.xml&query=//release)
`skija-windows`     | ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-windows%2Fmaven-metadata.xml&query=//release)

## Running

From CLI run the below command:

Windows/Linux:

```sh
clj -M -m lwjgl.main
```

macOS:

```sh
clj -J-XstartOnFirstThread -M -m lwjgl.main
```
