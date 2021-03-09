@file:Suppress("UNUSED_VARIABLE")

import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
  kotlin("jvm") version "1.4.10"
}

version = `TRAVIS-TAG` ?: "1.5.2"
description = "PDF builder written in Kotlin with a statically typed DSL"

repositories {
  jcenter()
  mavenCentral()
}

tasks {
  wrapper {
    distributionType = ALL
    gradleVersion = "6.2.2"
  }
}

fun searchPropertyOrNull(name: String, vararg aliases: String): String? {
  fun searchEverywhere(name: String): String? =
    findProperty(name) as? String? ?: System.getenv(name)

  searchEverywhere(name)?.let { return it }

  with(aliases.iterator()) {
    while (hasNext()) {
      searchEverywhere(next())?.let { return it }
    }
  }

  return null
}

fun searchProperty(name: String, vararg aliases: String) =
  searchPropertyOrNull(name, *aliases) ?: throw IllegalArgumentException(buildString {
    append("No property found with name $name")
    if (aliases.isNotEmpty())
      append(" or with alias: ${aliases.joinToString(", ")}")
  })

@Suppress("PropertyName")
val `TRAVIS-TAG`
  get() = with(System.getenv("TRAVIS_TAG")) {
    if (isNullOrBlank()) null else this
  }
