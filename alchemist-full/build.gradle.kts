import Libs.alchemist

/*
 * Copyright (C) 2010-2019, Danilo Pianini and contributors listed in the main(project"s alchemist/build.gradle file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution"s top directory.
 */
plugins {
    application
}

dependencies {
    runtimeOnly(rootProject)
    rootProject.subprojects.filterNot { it == project }.forEach {
        runtimeOnly(it)
    }
    testImplementation(rootProject)
    testImplementation(alchemist("euclidean-geometry"))
}

application {
    mainClass.set("it.unibo.alchemist.Alchemist")
}
