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

import com.flowpowered.math.vector.Vector2d;
import com.technicjelle.BMUtils.ShapeExtensions;
import de.bluecolored.bluemap.api.math.Shape;
import org.junit.Assert;
import org.junit.Test;


public class ShapeExtensionsTest {
	private static final double DELTA = 0.0001;

	@Test
	public void expandShapeRect_square() {
		Shape originalShape = Shape.createRect(0.0, 0.0, 16.0, 16.0);
		Shape expandedShape = ShapeExtensions.expandShapeRect(originalShape, 1);

		// Top-left
		Assert.assertEquals(-1, expandedShape.getPoint(0).getX(), DELTA);
		Assert.assertEquals(-1, expandedShape.getPoint(0).getY(), DELTA);

		// Top-right
		Assert.assertEquals(17, expandedShape.getPoint(1).getX(), DELTA);
		Assert.assertEquals(-1, expandedShape.getPoint(1).getY(), DELTA);

		// Bottom-right
		Assert.assertEquals(17, expandedShape.getPoint(2).getX(), DELTA);
		Assert.assertEquals(17, expandedShape.getPoint(2).getY(), DELTA);

		// Bottom-left
		Assert.assertEquals(-1, expandedShape.getPoint(3).getX(), DELTA);
		Assert.assertEquals(17, expandedShape.getPoint(3).getY(), DELTA);
	}

	@Test
	public void shrinkShapeRect_square() {
		Shape originalShape = Shape.createRect(0.0, 0.0, 16.0, 16.0);
		Shape shrunkShape = ShapeExtensions.shrinkShapeRect(originalShape, 1);

		// Top-left
		Assert.assertEquals(1, shrunkShape.getPoint(0).getX(), DELTA);
		Assert.assertEquals(1, shrunkShape.getPoint(0).getY(), DELTA);

		// Top-right
		Assert.assertEquals(15, shrunkShape.getPoint(1).getX(), DELTA);
		Assert.assertEquals(1, shrunkShape.getPoint(1).getY(), DELTA);

		// Bottom-right
		Assert.assertEquals(15, shrunkShape.getPoint(2).getX(), DELTA);
		Assert.assertEquals(15, shrunkShape.getPoint(2).getY(), DELTA);

		// Bottom-left
		Assert.assertEquals(1, shrunkShape.getPoint(3).getX(), DELTA);
		Assert.assertEquals(15, shrunkShape.getPoint(3).getY(), DELTA);
	}

	@Test
	public void expandShapeAccurate_circle() {
		Shape originalShape = Shape.createCircle(0.0, 0.0, 8.0, 16);
		Shape expandedShape = ShapeExtensions.expandShapeAccurate(originalShape, 1);

		Assert.assertEquals(8.0, originalShape.getPoint(0).getY(), DELTA);
		Assert.assertEquals(9.0, expandedShape.getPoint(0).getY(), DELTA);
	}

	@Test
	public void shrinkShapeAccurate_circle() {
		Shape originalShape = Shape.createCircle(0.0, 0.0, 8.0, 16);
		Shape expandedShape = ShapeExtensions.shrinkShapeAccurate(originalShape, 1);

		Assert.assertEquals(8.0, originalShape.getPoint(0).getY(), DELTA);
		Assert.assertEquals(7.0, expandedShape.getPoint(0).getY(), DELTA);
	}

	@Test
	public void scaleShapeOrigin() {
		Shape originalShape = Shape.createRect(0.0, 0.0, 16.0, 16.0);
		Shape scaledShape = ShapeExtensions.scaleShapeOrigin(originalShape, 2.0);

		// Top-left
		Assert.assertEquals(0, scaledShape.getPoint(0).getX(), DELTA);
		Assert.assertEquals(0, scaledShape.getPoint(0).getY(), DELTA);

		// Top-right
		Assert.assertEquals(32, scaledShape.getPoint(1).getX(), DELTA);
		Assert.assertEquals(0, scaledShape.getPoint(1).getY(), DELTA);

		// Bottom-right
		Assert.assertEquals(32, scaledShape.getPoint(2).getX(), DELTA);
		Assert.assertEquals(32, scaledShape.getPoint(2).getY(), DELTA);

		// Bottom-left
		Assert.assertEquals(0, scaledShape.getPoint(3).getX(), DELTA);
		Assert.assertEquals(32, scaledShape.getPoint(3).getY(), DELTA);
	}

	@Test
	public void scaleShapeMiddle() {
		Shape originalShape = Shape.createRect(0.0, 0.0, 16.0, 16.0);
		Shape scaledShape = ShapeExtensions.scaleShapeMiddle(originalShape, 2.0);

		// Top-left
		Assert.assertEquals(-8, scaledShape.getPoint(0).getX(), DELTA);
		Assert.assertEquals(-8, scaledShape.getPoint(0).getY(), DELTA);

		// Top-right
		Assert.assertEquals(24, scaledShape.getPoint(1).getX(), DELTA);
		Assert.assertEquals(-8, scaledShape.getPoint(1).getY(), DELTA);

		// Bottom-right
		Assert.assertEquals(24, scaledShape.getPoint(2).getX(), DELTA);
		Assert.assertEquals(24, scaledShape.getPoint(2).getY(), DELTA);

		// Bottom-left
		Assert.assertEquals(-8, scaledShape.getPoint(3).getX(), DELTA);
		Assert.assertEquals(24, scaledShape.getPoint(3).getY(), DELTA);
	}

	@Test
	public void scaleShapeAround() {
		Shape originalShape = Shape.createRect(0.0, 0.0, 16.0, 16.0);
		Shape scaledShape = ShapeExtensions.scaleShapeAround(originalShape, 2.0, Vector2d.from(16.0, 16.0));

		// Top-left
		Assert.assertEquals(-16, scaledShape.getPoint(0).getX(), DELTA);
		Assert.assertEquals(-16, scaledShape.getPoint(0).getY(), DELTA);

		// Top-right
		Assert.assertEquals(16, scaledShape.getPoint(1).getX(), DELTA);
		Assert.assertEquals(-16, scaledShape.getPoint(1).getY(), DELTA);

		// Bottom-right
		Assert.assertEquals(16, scaledShape.getPoint(2).getX(), DELTA);
		Assert.assertEquals(16, scaledShape.getPoint(2).getY(), DELTA);

		// Bottom-left
		Assert.assertEquals(-16, scaledShape.getPoint(3).getX(), DELTA);
		Assert.assertEquals(16, scaledShape.getPoint(3).getY(), DELTA);
	}
}
