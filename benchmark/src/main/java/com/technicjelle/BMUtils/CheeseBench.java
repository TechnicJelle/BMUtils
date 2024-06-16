/*
 * This file is part of BMUtils, licensed under the MPL2 License (MPL).
 * Please keep tabs on https://github.com/TechnicJelle/BMUtils for updates.
 *
 * Copyright (c) TechnicJelle <https://technicjelle.com>
 * Copyright (c) contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.technicjelle.BMUtils;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import de.bluecolored.bluemap.api.math.Shape;
import org.junit.Assert;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class CheeseBench {

	@State(Scope.Benchmark)
	public static class BenchmarkState {
		@Param({"10", "100"})
		public int size;
		public Vector2i[] chunkSquare;

		@Param({"1", "5", "10"})
		public int amount;
		public Vector2i[] multipleSeparateChunkSquares;

		@Setup(Level.Trial)
		public void setUp() {
			{
				List<Vector2i> chunks = new ArrayList<>(size * size * amount);
				for (int i = 0; i < amount; i++) {
					Collections.addAll(chunks, generateChunkSquare(size, i * size));
				}
				chunkSquare = chunks.toArray(Vector2i[]::new);
			}
			{
				List<Vector2i> chunks = new ArrayList<>(size * size * amount);
				for (int i = 0; i < amount; i++) {
					Collections.addAll(chunks, generateChunkSquare(size, i * size * 2));
				}
				multipleSeparateChunkSquares = chunks.toArray(Vector2i[]::new);
			}

			Assert.assertEquals(chunkSquare.length, multipleSeparateChunkSquares.length);
		}

		private static Vector2i[] generateChunkSquare(int size, int offset) {
			Vector2i[] chunks = new Vector2i[size * size];
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					chunks[x * size + y] = new Vector2i(x + offset, y);
				}
			}
			return chunks;
		}
	}

	@Benchmark
	public void singleCheese(BenchmarkState state) {
		for (int i = 0; i < state.amount; i++) {
			Cheese cheese = Cheese.createSingleFromChunks(state.chunkSquare);
			verifySingleCheese(state, cheese);
		}
	}

	/**
	 * For direct comparison with {@link #singleCheese(BenchmarkState)}
	 * to see how much overhead the Platter adds.
	 */
	@Benchmark
	public void singleCheesePlatter(BenchmarkState state) {
		for (int i = 0; i < state.amount; i++) {
			Collection<Cheese> platter = Cheese.createPlatterFromChunks(state.chunkSquare);
			Assert.assertEquals(1, platter.size());
			Cheese cheese = platter.iterator().next();
			verifySingleCheese(state, cheese);
		}
	}

	private static void verifySingleCheese(BenchmarkState state, Cheese cheese) {
		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());

		Assert.assertEquals(Vector2d.from(16 * state.size * state.amount, 0), shape.getPoint(0)); //top right
		Assert.assertEquals(Vector2d.from(16 * state.size * state.amount, 16 * state.size), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 16 * state.size), shape.getPoint(2)); //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape.getPoint(3)); //top left
	}

	@Benchmark
	public void cheesePlatterSeparated(BenchmarkState state) {
		Collection<Cheese> platter = Cheese.createPlatterFromChunks(state.multipleSeparateChunkSquares);

		Assert.assertEquals(state.amount, platter.size());
	}
}
