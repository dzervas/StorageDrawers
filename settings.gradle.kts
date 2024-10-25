pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge Snapshots"
        }
        maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
            name = "Fuzs Mod Resources"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "Storage Drawers"
include("common")
include("forge")
include("fabric")