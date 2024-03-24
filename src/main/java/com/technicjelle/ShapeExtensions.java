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
import de.bluecolored.bluemap.api.math.Shape;
import org.jetbrains.annotations.NotNull;

/**
 * Utility functions for {@link Shape}s
 */
public class ShapeExtensions {
	private ShapeExtensions() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Expands a {@link Shape} by a given amount, in all directions.<br>
	 * This function is more intuitive for rectangular shapes, but technically not exactly mathematically accurate.<br>
	 * Use {@link #expandShapeAccurate(Shape, double)} for more free-form shapes, like ellipses and circles.<br>
	 * <br>
	 * Example: expanding a 16x16 square by 1 in each direction results in an 18x18 square
	 *
	 * @param amount The amount (in blocks) to expand the shape by. May be negative to shrink the shape.
	 * @see #shrinkShapeRect(Shape, double)
	 */
	public static Shape expandShapeRect(@NotNull Shape shape, double amount) {
		final int size = shape.getPointCount();

		//copy points list. this is the one we'll be modifying
		Vector2d[] copies = new Vector2d[size];
		for (int i = 0; i < size; i++) {
			copies[i] = shape.getPoint(i);
		}

		//offset each edge
		for (int i = 0; i < size; i++) {
			int endIndex = (i + 1) % size;

			//read from original shape, to not be
			// influenced by the already-modified data
			Vector2d start = shape.getPoint(i);
			Vector2d end = shape.getPoint(endIndex);

			//find each edge's normal
			Vector2d dir = end.sub(start).normalize();
			Vector2d norm = Vector2d.from(dir.getY(), -dir.getX()).mul(amount);

			//offset original points in direction of normal
			// we add, instead of overwrite, because the
			// next edge's start is this one's end
			copies[i] = copies[i].add(norm);
			copies[endIndex] = copies[endIndex].add(norm);
		}

		//return the modified shape
		return new Shape(copies);
	}

	/**
	 * Shrinks a {@link Shape} by a given amount, in all directions.<br>
	 * This function is more intuitive for rectangular shapes, but technically not exactly mathematically accurate.<br>
	 * Use {@link #shrinkShapeAccurate(Shape, double)} for more free-form shapes, like ellipses and circles.<br>
	 * <br>
	 * Example: shrinking a 16x16 square by 1 in each direction results in a 14x14 square
	 *
	 * @param amount The amount (in blocks) to expand the shape by. May be negative to expand the shape.
	 * @see #expandShapeRect(Shape, double)
	 */
	public static Shape shrinkShapeRect(@NotNull Shape shape, double amount) {
		return expandShapeRect(shape, -amount);
	}

	/**
	 * Expands a {@link Shape} by a given amount, in all directions.<br>
	 * More accurate for free-form shapes, like ellipses and circles, but less intuitive for rectangular shapes<br>
	 * Use {@link #expandShapeRect(Shape, double)} for more rectangular shapes.<br>
	 * <br>
	 * Example: expanding a square by 1 in each direction will only offset each edge by (√2)/2
	 *
	 * @param amount The amount to expand the shape by. May be negative to shrink the shape.
	 * @see #shrinkShapeAccurate(Shape, double)
	 */
	public static Shape expandShapeAccurate(@NotNull Shape shape, double amount) {
		return shrinkShapeAccurate(shape, -amount);
	}

	/**
	 * Shrinks a {@link Shape} by a given amount, in all directions.<br>
	 * More accurate for free-form shapes, like ellipses and circles, but less intuitive for rectangular shapes<br>
	 * Use {@link #shrinkShapeRect(Shape, double)} for more rectangular shapes.<br>
	 * <br>
	 * Example: shrinking a square by 1 in each direction will only offset each edge by (√2)/2
	 *
	 * @param amount The amount to expand the shape by. May be negative to shrink the shape.
	 * @see #expandShapeAccurate(Shape, double)
	 */
	public static Shape shrinkShapeAccurate(@NotNull Shape shape, double amount) {
		final int size = shape.getPointCount();

		//copy points list. this is the one we'll be modifying
		Vector2d[] copies = new Vector2d[size];
		for (int i = 0; i < size; i++) {
			copies[i] = shape.getPoint(i);
		}

		//1. don't loop edge by edge, loop point by point
		for (int i = 0; i < size; i++) {
			// find the previous and next point
			int prevIndex = Math.floorMod(i - 1, size);
			int nextIndex = Math.floorMod(i + 1, size);

			// find positions of the previous and next point
			Vector2d prevStart = shape.getPoint(prevIndex);
			Vector2d current = shape.getPoint(i);
			Vector2d nextEnd = shape.getPoint(nextIndex);

			//2. take the normals of both edges that are using that point

			// find the direction and normal of the previous edge
			Vector2d dirFromPrev = current.sub(prevStart).normalize();
			Vector2d normFromPrev = Vector2d.from(dirFromPrev.getY(), -dirFromPrev.getX());

			// find the direction and normal of the next edge
			Vector2d dirToNext = nextEnd.sub(current).normalize();
			Vector2d normToNext = Vector2d.from(dirToNext.getY(), -dirToNext.getX());

			//3. average the two normals, and normalize it again
			Vector2d averageNorm = normFromPrev.add(normToNext).div(2.0).normalize();
			//4. scale the normal and add it to the point
			copies[i] = copies[i].add(averageNorm.mul(amount));
		}

		//return the modified shape
		return new Shape(copies);
	}

	/**
	 * Scales a shape by a given factor, around the origin (0, 0)
	 */
	public static Shape scaleShapeOrigin(@NotNull Shape shape, double scale) {
		Vector2d[] points = new Vector2d[shape.getPointCount()];
		for (int i = 0; i < shape.getPointCount(); i++) {
			points[i] = shape.getPoint(i).mul(scale);
		}
		return new Shape(points);
	}

	/**
	 * Scales a shape by a given factor, around the middle of the shape
	 */
	public static @NotNull Shape scaleShapeMiddle(@NotNull Shape shape, double scale) {
		Vector2d middle = shape.getMin().add(shape.getMax()).div(2);
		return scaleShapeAround(shape, scale, middle);
	}

	/**
	 * Scales a shape by a given factor, around a given origin
	 */
	public static Shape scaleShapeAround(@NotNull Shape shape, double scale, Vector2d origin) {
		Vector2d[] points = new Vector2d[shape.getPointCount()];
		for (int i = 0; i < shape.getPointCount(); i++) {
			points[i] = origin.add(shape.getPoint(i).sub(origin).mul(scale));
		}
		return new Shape(points);
	}
}
