/*
 * Copyright (C) 2010-2021, Danilo Pianini and contributors
 * listed in the main project's alchemist/build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.model.implementations.routes

import com.google.common.collect.ImmutableList
import it.unibo.alchemist.model.interfaces.GeoPosition
import com.graphhopper.GHResponse
import com.graphhopper.util.shapes.GHPoint3D
import it.unibo.alchemist.model.interfaces.TimedRoute
import it.unibo.alchemist.model.implementations.positions.LatLongPosition
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Models a route on a map, built upon the information provided by a query to GraphHopper.
 */
class GraphHopperRoute(from: GeoPosition, to: GeoPosition, response: GHResponse) : TimedRoute<GeoPosition> {
    private val distance: Double
    private val time: Double
    private val points: ImmutableList<GeoPosition>

    init {
        val errors = response.errors
        if (errors.isEmpty()) {
            val response = response.best
            val points = response.points.map { it.asPosition() }
            val initDistance = from.distanceTo(points.first())
            var pointSequence: Sequence<GeoPosition> = points.asSequence()
            var actualDistance = response.distance
            if (initDistance > 0) {
                actualDistance += initDistance
                pointSequence = sequenceOf(from) + pointSequence
            }
            val endingDistance = points.last().distanceTo(to)
            if (endingDistance > 0) {
                actualDistance += endingDistance
                pointSequence += sequenceOf(to)
            }
            // m / s, times are returned in ms
            val averageSpeed = response.distance * TimeUnit.SECONDS.toMillis(1) / response.time
            time = actualDistance / averageSpeed
            distance = actualDistance
            val builder = ImmutableList.builder<GeoPosition>()
            pointSequence.forEach(builder::add)
            this.points = builder.build()
        } else {
            val msg = errors.stream().map { obj: Throwable -> obj.message }.collect(Collectors.joining("\n"))
            throw IllegalArgumentException(msg, errors[0])
        }
    }

    override fun length(): Double {
        return distance
    }

    override fun getPoint(step: Int): GeoPosition {
        return points[step]
    }

    override fun getPoints(): ImmutableList<GeoPosition> {
        return points
    }

    override fun getTripTime(): Double {
        return time
    }

    override fun iterator(): MutableIterator<GeoPosition> {
        return points.iterator()
    }

    override fun stream(): Stream<GeoPosition?> {
        return points.stream()
    }

    override fun size(): Int {
        return points.size
    }

    companion object {
        private const val serialVersionUID = 0L

        private fun GHPoint3D.asPosition() = LatLongPosition(lat, lon)
    }
}
