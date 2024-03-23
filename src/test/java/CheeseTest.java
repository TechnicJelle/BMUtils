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
import com.flowpowered.math.vector.Vector2i;
import com.technicjelle.Cheese;
import de.bluecolored.bluemap.api.math.Shape;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;


public class CheeseTest {
	/*@Test
	public void isDelicious() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(Vector2i.from(30, 55));
		Assert.assertTrue(cheese.isDelicious());
	}*/

	@Test
	public void twoByTwoConnected() {
		Assert.assertTrue(Cheese.checkConnected(
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 0), //top right
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 1)  //bottom right
		));
	}

	@Test
	public void twoByTwoDisconnected() {
		// X X
		// X . X
		Assert.assertFalse(Cheese.checkConnected(
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 0), //top right
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(2, 1)  //bottom right
		));
	}

	@Test
	public void farawayDisconnected() {
		Assert.assertFalse(Cheese.checkConnected(
				Vector2i.from(0, 0),
				Vector2i.from(12, 34)
		));
	}

	@Test
	public void diagonalDisconnected() {
		Assert.assertFalse(Cheese.checkConnected(
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 1)  //bottom right
		));
	}

	@Test
	public void singleChunkAtZeroZero() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(Vector2i.from(0, 0));

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(16, 0), shape.getPoint(0));  //top right
		Assert.assertEquals(Vector2d.from(16, 16), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 16), shape.getPoint(2));  //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape.getPoint(3));   //top left
	}

	@Test
	public void singleChunkFarAway() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(Vector2i.from(123, 456));

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(1984, 7296), shape.getPoint(0)); //top right
		Assert.assertEquals(Vector2d.from(1984, 7312), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(1968, 7312), shape.getPoint(2)); //bottom left
		Assert.assertEquals(Vector2d.from(1968, 7296), shape.getPoint(3)); //top left
	}

	@Test
	public void singleChunkNegative() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(Vector2i.from(-12, -34));

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(-176, -544), shape.getPoint(0)); //top right
		Assert.assertEquals(Vector2d.from(-176, -528), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(-192, -528), shape.getPoint(2)); //bottom left
		Assert.assertEquals(Vector2d.from(-192, -544), shape.getPoint(3)); //top left
	}

	@Test
	public void twoByTwoChunks() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 0), //top right
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 1)  //bottom right
		);

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(32, 0), shape.getPoint(0));  //top right
		Assert.assertEquals(Vector2d.from(32, 32), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 32), shape.getPoint(2));  //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape.getPoint(3));   //top left
	}

	@Test
	public void twoByTwoChunksDifferentOrder() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromChunks(
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 0), //top right
				Vector2i.from(1, 1), //bottom right
				Vector2i.from(0, 0)  //top left
		);

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(32, 0), shape.getPoint(0));  //top right
		Assert.assertEquals(Vector2d.from(32, 32), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 32), shape.getPoint(2));  //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape.getPoint(3));   //top left
	}

	@Test
	public void threeChunksLaidOutInAnLShape() throws Cheese.InvalidSelectionException {
		/*
		 *               │
		 *               │
		 *               │
		 *               0
		 * ─────0┌───────┬───────┐0─────
		 *       │       │       │
		 *       │ -1, 0 │  0, 0 │
		 *       │       │       │
		 *     16├───────┼───────┘16
		 *       │       │
		 *       │ -1, 1 │
		 *       │       │
		 *     32└───────┘
		 *       -16     0
		 *               │
		 *               │
		 *               │
		 */
		Cheese cheese = Cheese.createFromChunks(
				Vector2i.from(-1, 0), //top left
				Vector2i.from(0, 0),  //top right
				Vector2i.from(-1, 1)  //bottom (left)
		);

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(6, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(16, 0), shape.getPoint(0));   //top right
		Assert.assertEquals(Vector2d.from(16, 16), shape.getPoint(1));  //middle right
		Assert.assertEquals(Vector2d.from(0, 16), shape.getPoint(2));   //center
		Assert.assertEquals(Vector2d.from(0, 32), shape.getPoint(3));   //bottom middle
		Assert.assertEquals(Vector2d.from(-16, 32), shape.getPoint(4)); //bottom left
		Assert.assertEquals(Vector2d.from(-16, 0), shape.getPoint(5));  //top left
	}

	@Test
	public void threeByThreeChunksWithHoleInMiddle() throws Cheese.InvalidSelectionException {
		/*
		 *               │
		 *               │
		 *       -16     0       16
		 *    -16┌───────┬───────┬───────┐-16
		 *       │       │       │       │
		 *       │ -1,-1 │  0,-1 │  1,-1 │
		 *       │       │       │       │
		 * ─────0├───────┼───────┼───────┤0─────
		 *       │       │┌─────┐│       │
		 *       │ -1, 0 ││ 0, 0││  1, 0 │
		 *       │       │└─────┘│       │
		 *     16├───────┼───────┼───────┤16
		 *       │       │       │       │
		 *       │ -1, 1 │  0, 1 │  1, 1 │
		 *       │       │       │       │
		 *     32└───────┴───────┴───────┘32
		 *       -16     0       16
		 *               │
		 *               │
		 */
		Cheese cheese = Cheese.createFromChunks(
				Vector2i.from(-1, -1), //top left
				Vector2i.from(0, -1),  //top middle
				Vector2i.from(1, -1),  //top right
				Vector2i.from(-1, 0),  //middle left
				Vector2i.from(1, 0),   //middle right
				Vector2i.from(-1, 1),  //bottom left
				Vector2i.from(0, 1),   //bottom middle
				Vector2i.from(1, 1)    //bottom right
		);

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(32, -16), shape.getPoint(0));  //top right
		Assert.assertEquals(Vector2d.from(32, 32), shape.getPoint(1));   //bottom right
		Assert.assertEquals(Vector2d.from(-16, 32), shape.getPoint(2));  //bottom left
		Assert.assertEquals(Vector2d.from(-16, -16), shape.getPoint(3)); //top left

		Shape[] holes = cheese.getHoles().toArray(Shape[]::new);
		Assert.assertEquals(1, holes.length);

		Shape hole = holes[0];
		Assert.assertEquals(4, hole.getPointCount());
		Assert.assertEquals(Vector2d.from(0, 16), hole.getPoint(0));  //bottom left
		Assert.assertEquals(Vector2d.from(16, 16), hole.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(16, 0), hole.getPoint(2));  //top right
		Assert.assertEquals(Vector2d.from(0, 0), hole.getPoint(3));   //top left
	}

	@Test
	public void fiveByThreeChunksWithHolesInMiddle() throws Cheese.InvalidSelectionException {
		/*
		 *               │
		 *               │
		 *               │
		 *               │
		 *       -16     0       16      32      48      64
		 *    -16┌───────┬───────┬───────┬───────┬───────┐-16
		 *       │       │       │       │       │       │
		 *       │ -1,-1 │  0,-1 │  1,-1 │  2,-1 │  3,-1 │
		 *       │       │       │       │       │       │
		 * ─────0├───────┼───────┼───────┼───────┼───────┤0─────
		 *       │       │┌─────┐│       │┌─────┐│       │
		 *       │ -1, 0 ││ 0, 0││  1, 0 ││ 2, 0││  3, 0 │
		 *       │       │└─────┘│       │└─────┘│       │
		 *     16├───────┼───────┼───────┼───────┼───────┤16
		 *       │       │       │       │       │       │
		 *       │ -1, 1 │  0, 1 │  1, 1 │  2, 1 │  3, 1 │
		 *       │       │       │       │       │       │
		 *     32└───────┴───────┴───────┴───────┴───────┘32
		 *       -16     0       16      32      48      64
		 *               │
		 *               │
		 *               │
		 */
		Cheese cheese = Cheese.createFromChunks(
				Vector2i.from(-1, -1), //top left
				Vector2i.from(0, -1),  //top left/middle
				Vector2i.from(1, -1),  //top middle
				Vector2i.from(2, -1),  //top middle/right
				Vector2i.from(3, -1),  //top right
				Vector2i.from(-1, 0),  //middle left
				Vector2i.from(1, 0),   //middle
				Vector2i.from(3, 0),   //middle right
				Vector2i.from(-1, 1),  //bottom left
				Vector2i.from(0, 1),   //bottom left/middle
				Vector2i.from(1, 1),   //bottom middle
				Vector2i.from(2, 1),   //bottom middle/right
				Vector2i.from(3, 1)    //bottom right
		);

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(64, -16), shape.getPoint(0));  //top right
		Assert.assertEquals(Vector2d.from(64, 32), shape.getPoint(1));   //bottom right
		Assert.assertEquals(Vector2d.from(-16, 32), shape.getPoint(2));  //bottom left
		Assert.assertEquals(Vector2d.from(-16, -16), shape.getPoint(3)); //top left

		Shape[] holes = cheese.getHoles().toArray(Shape[]::new);
		Assert.assertEquals(2, holes.length);

		Shape leftHole = holes[0];
		Assert.assertEquals(4, leftHole.getPointCount());
		Assert.assertEquals(Vector2d.from(0, 16), leftHole.getPoint(0));  //bottom left
		Assert.assertEquals(Vector2d.from(16, 16), leftHole.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(16, 0), leftHole.getPoint(2));  //top right
		Assert.assertEquals(Vector2d.from(0, 0), leftHole.getPoint(3));   //top left

		Shape rightHole = holes[1];
		Assert.assertEquals(4, rightHole.getPointCount());
		Assert.assertEquals(Vector2d.from(32, 16), rightHole.getPoint(0)); //bottom left
		Assert.assertEquals(Vector2d.from(48, 16), rightHole.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(48, 0), rightHole.getPoint(2));  //top right
		Assert.assertEquals(Vector2d.from(32, 0), rightHole.getPoint(3));  //top left
	}

	@Test
	public void twoSeparatedChunks() {
		Assert.assertThrows(Cheese.InvalidSelectionException.class, () ->
				Cheese.createFromChunks(
						Vector2i.from(0, 0),
						Vector2i.from(12, 34)
				));
	}

	@Test
	public void customCellSize() throws Cheese.InvalidSelectionException {
		Cheese cheese = Cheese.createFromCells(Vector2d.from(10),
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 0), //top right
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 1)  //bottom right
		);

		Assert.assertTrue(cheese.getHoles().isEmpty());

		Shape shape = cheese.getShape();
		Assert.assertEquals(4, shape.getPointCount());
		Assert.assertEquals(Vector2d.from(20, 0), shape.getPoint(0)); //top right
		Assert.assertEquals(Vector2d.from(20, 20), shape.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 20), shape.getPoint(2)); //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape.getPoint(3)); //top left
	}

	@Test
	public void disconnectedCustomCells() {
		Assert.assertThrows(Cheese.InvalidSelectionException.class, () ->
				Cheese.createFromCells(Vector2d.from(8),
						Vector2i.from(0, 0),
						Vector2i.from(12, 34)
				));
	}

	@Test
	public void multiCheese() {
		Collection<Cheese> cheeses = Cheese.createMultiCheeseFromChunks(
				Vector2i.from(0, 0),
				Vector2i.from(12, 34)
		);

		Assert.assertEquals(2, cheeses.size());

		Cheese[] cheeseArray = cheeses.toArray(Cheese[]::new);

		Cheese cheese1 = cheeseArray[0];
		Assert.assertTrue(cheese1.getHoles().isEmpty());
		Shape shape1 = cheese1.getShape();
		Assert.assertEquals(4, shape1.getPointCount());
		Assert.assertEquals(Vector2d.from(16, 0), shape1.getPoint(0));  //top right
		Assert.assertEquals(Vector2d.from(16, 16), shape1.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(0, 16), shape1.getPoint(2));  //bottom left
		Assert.assertEquals(Vector2d.from(0, 0), shape1.getPoint(3));   //top left

		Cheese cheese2 = cheeseArray[1];
		Assert.assertTrue(cheese2.getHoles().isEmpty());
		Shape shape2 = cheese2.getShape();
		Assert.assertEquals(4, shape2.getPointCount());
		Assert.assertEquals(Vector2d.from(208, 544), shape2.getPoint(0)); //top right
		Assert.assertEquals(Vector2d.from(208, 560), shape2.getPoint(1)); //bottom right
		Assert.assertEquals(Vector2d.from(192, 560), shape2.getPoint(2)); //bottom left
		Assert.assertEquals(Vector2d.from(192, 544), shape2.getPoint(3)); //top left
	}
}
