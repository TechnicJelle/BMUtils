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

package com.technicjelle;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import de.bluecolored.bluemap.api.math.Shape;
import org.junit.Assert;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CheeseBench {
	private static final Vector2i[] chunkSquare10 = generateChunkSquare(10);
	private static final Vector2i[] chunkSquare11 = generateChunkSquare(11);
	private static final Vector2i[] chunkSquare12 = generateChunkSquare(12);
	private static final Vector2i[] chunkSquare13 = generateChunkSquare(13);
	private static final Vector2i[] chunkSquare14 = generateChunkSquare(14);
	private static final Vector2i[] chunkSquare15 = generateChunkSquare(15);
	private static final Vector2i[] chunkSquare16 = generateChunkSquare(16);

	private static Vector2i[] generateChunkSquare(int size) {
		Vector2i[] chunks = new Vector2i[size * size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				chunks[x * size + y] = new Vector2i(x, y);
			}
		}
		return chunks;
	}

	private void testCheese(Cheese cheese, int size) {
		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());

		Assert.assertEquals(Vector2d.from(16 * size, 0), shape.getPoint(0)); //top right
		Assert.assertEquals(Vector2d.from(16 * size, 16 * size), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 16 * size), shape.getPoint(2)); //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape.getPoint(3)); //top left
	}

	@Benchmark
	public void measureCheese10() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare10);
		testCheese(cheese, 10);
	}

	@Benchmark
	public void measureCheese11() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare11);
		testCheese(cheese, 11);
	}

	@Benchmark
	public void measureCheese12() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare12);
		testCheese(cheese, 12);
	}

	@Benchmark
	public void measureCheese13() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare13);
		testCheese(cheese, 13);
	}

	@Benchmark
	public void measureCheese14() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare14);
		testCheese(cheese, 14);
	}

	@Benchmark
	public void measureCheese15() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare15);
		testCheese(cheese, 15);
	}

	@Benchmark
	public void measureCheese16() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(chunkSquare16);
		testCheese(cheese, 16);
	}

	@Benchmark
	public void measureUnsafeCheese10() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare10);
		testCheese(cheese, 10);
	}

	@Benchmark
	public void measureUnsafeCheese11() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare11);
		testCheese(cheese, 11);
	}

	@Benchmark
	public void measureUnsafeCheese12() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare12);
		testCheese(cheese, 12);
	}

	@Benchmark
	public void measureUnsafeCheese13() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare13);
		testCheese(cheese, 13);
	}

	@Benchmark
	public void measureUnsafeCheese14() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare14);
		testCheese(cheese, 14);
	}

	@Benchmark
	public void measureUnsafeCheese15() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare15);
		testCheese(cheese, 15);
	}

	@Benchmark
	public void measureUnsafeCheese16() {
		Cheese cheese = Cheese.createFromChunksUnsafe(chunkSquare16);
		testCheese(cheese, 16);
	}
}
