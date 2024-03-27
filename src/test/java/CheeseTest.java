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

public class CheeseTest {
	/*@Test
	public void isDelicious() {
		Cheese cheese = Cheese.createSingleFromChunks(Vector2i.from(30, 55));
		Assert.assertTrue(cheese.isDelicious());
	}*/

	private static void compareCheese(Cheese cheese, Cheese testCheese) {
		// Compare the shape
		Shape cheeseShape = cheese.getShape();
		Shape testShape = testCheese.getShape();
		Assert.assertEquals("Shape point count", testShape.getPointCount(), cheeseShape.getPointCount());
		Assert.assertArrayEquals("Shape points", testShape.getPoints(), cheeseShape.getPoints());

		// Compare the holes
		Shape[] cheeseHoles = cheese.getHoles().toArray(Shape[]::new);
		Shape[] testHoles = testCheese.getHoles().toArray(Shape[]::new);
		Assert.assertEquals("Holes amount", testHoles.length, cheeseHoles.length);
		for (int i = 0; i < testHoles.length; i++) {
			// Compare the shape of each hole
			Shape hole = cheeseHoles[i];
			Shape testHole = testHoles[i];
			Assert.assertEquals("Hole " + i + " point count", testHole.getPointCount(), hole.getPointCount());
			Assert.assertArrayEquals("Hole " + i + " points", testHole.getPoints(), hole.getPoints());
		}
	}

	//region Single

	//region Chunks

	@Test
	public void singleChunkAtZeroZero() {
		Cheese cheese = Cheese.createSingleFromChunks(Vector2i.from(0, 0));
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(16, 0), //top right
				Vector2d.from(16, 16),//bottom right
				Vector2d.from(0, 16), //bottom left
				Vector2d.from(0, 0)  //top left
		)));
	}

	@Test
	public void singleChunkFarAway() {
		Cheese cheese = Cheese.createSingleFromChunks(Vector2i.from(123, 456));
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(1984, 7296), //top right
				Vector2d.from(1984, 7312), //bottom right
				Vector2d.from(1968, 7312), //bottom left
				Vector2d.from(1968, 7296)  //top left
		)));
	}

	@Test
	public void singleChunkNegative() {
		Cheese cheese = Cheese.createSingleFromChunks(Vector2i.from(-12, -34));
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(-176, -544), //top right
				Vector2d.from(-176, -528), //bottom right
				Vector2d.from(-192, -528), //bottom left
				Vector2d.from(-192, -544)  //top left
		)));
	}

	@Test
	public void twoByTwoChunks() {
		// ##
		// ##
		Cheese cheese = Cheese.createSingleFromChunks(
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 0), //top right
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 1)  //bottom right
		);
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(32, 0),  //top right
				Vector2d.from(32, 32), //bottom right
				Vector2d.from(0, 32),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));
	}

	@Test
	public void twoByTwoChunksDifferentOrder() {
		Cheese cheese = Cheese.createSingleFromChunks(
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 0), //top right
				Vector2i.from(1, 1), //bottom right
				Vector2i.from(0, 0)  //top left
		);
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(32, 0),  //top right
				Vector2d.from(32, 32), //bottom right
				Vector2d.from(0, 32),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));
	}

	@Test
	public void threeChunksLaidOutInAnLShape() {
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
		Cheese cheese = Cheese.createSingleFromChunks(
				Vector2i.from(-1, 0), //top left
				Vector2i.from(0, 0),  //top right
				Vector2i.from(-1, 1)  //bottom (left)
		);
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(16, 0),   //top right
				Vector2d.from(16, 16),  //middle right
				Vector2d.from(0, 16),   //center
				Vector2d.from(0, 32),   //bottom middle
				Vector2d.from(-16, 32), //bottom left
				Vector2d.from(-16, 0)   //top left
		)));
	}

	@Test
	public void threeByThreeChunksWithHoleInMiddle() {
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
		Cheese cheese = Cheese.createSingleFromChunks(
				Vector2i.from(-1, -1), //top left
				Vector2i.from(0, -1),  //top middle
				Vector2i.from(1, -1),  //top right
				Vector2i.from(-1, 0),  //middle left
				Vector2i.from(1, 0),   //middle right
				Vector2i.from(-1, 1),  //bottom left
				Vector2i.from(0, 1),   //bottom middle
				Vector2i.from(1, 1)    //bottom right
		);
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(32, -16), //top right
				Vector2d.from(32, 32),  //bottom right
				Vector2d.from(-16, 32), //bottom left
				Vector2d.from(-16, -16) //top left
		),
				new Shape(
						Vector2d.from(0, 16),  //bottom left
						Vector2d.from(16, 16), //bottom right
						Vector2d.from(16, 0),  //top right
						Vector2d.from(0, 0)    //top left
				)
		));
	}

	@Test
	public void fiveByThreeChunksWithHolesInMiddle() {
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
		Cheese cheese = Cheese.createSingleFromChunks(
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

		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(64, -16), //top right
				Vector2d.from(64, 32),  //bottom right
				Vector2d.from(-16, 32), //bottom left
				Vector2d.from(-16, -16) //top left
		),
				new Shape(
						Vector2d.from(0, 16),  //bottom left
						Vector2d.from(16, 16), //bottom right
						Vector2d.from(16, 0),  //top right
						Vector2d.from(0, 0)   //top left
				),
				new Shape(
						Vector2d.from(32, 16), //bottom left
						Vector2d.from(48, 16), //bottom right
						Vector2d.from(48, 0),  //top right
						Vector2d.from(32, 0)  //top left
				)
		));
	}

	@Test
	public void twoSeparatedChunksWithSingle() {
		Cheese cheese = Cheese.createSingleFromChunks(
				Vector2i.from(0, 0),
				Vector2i.from(12, 34)
		); // Returns just the first shape
		Shape cheeseShape = cheese.getShape();

		Shape testShape = new Shape(
				Vector2d.from(16, 0),  //top right
				Vector2d.from(16, 16), //bottom right
				Vector2d.from(0, 16),  //bottom left
				Vector2d.from(0, 0)    //top left
		);

		Assert.assertEquals(testShape.getPointCount(), cheeseShape.getPointCount());
		Assert.assertArrayEquals(testShape.getPoints(), cheeseShape.getPoints());

		// Somehow, the other chunk is still in the holes
		// But I will not test for that, as it's undefined behaviour
	}

	//endregion Chunks

	//region Cells

	@Test
	public void customCellSize() {
		Cheese cheese = Cheese.createSingleFromCells(Vector2d.from(10),
				Vector2i.from(0, 0), //top left
				Vector2i.from(1, 0), //top right
				Vector2i.from(0, 1), //bottom left
				Vector2i.from(1, 1)  //bottom right
		);
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(20, 0),  //top right
				Vector2d.from(20, 20), //bottom right
				Vector2d.from(0, 20),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));
	}

	@Test
	public void customDecimalCellSize() {
		Cheese cheese = Cheese.createSingleFromCells(Vector2d.from(8.3),
				Vector2i.from(0, 0),
				Vector2i.from(1, 0)
		);
		compareCheese(cheese, new Cheese(new Shape(
				Vector2d.from(16.6, 0),   //top right
				Vector2d.from(16.6, 8.3), //bottom right
				Vector2d.from(0, 8.3),    //bottom left
				Vector2d.from(0, 0)       //top left
		)));
	}

	//endregion Cells

	//endregion Single


	//region Platter

	@Test
	public void cheesePlatterEmpty() {
		// No inputs
		Assert.assertTrue(Cheese.createPlatterFromChunks().isEmpty());
	}

	@Test
	public void cheesePlatterWithSingleCheese() {
		// Single point
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(1, 2)
		).toArray(Cheese[]::new);
		Assert.assertEquals(1, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, 32), //top right
				Vector2d.from(32, 48), //bottom right
				Vector2d.from(16, 48), //bottom left
				Vector2d.from(16, 32)  //top left
		)));
	}

	@Test
	public void cheesePlatterTwoNeighbours() {
		// ##.##
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(0, 0),
				Vector2i.from(1, 0),
				Vector2i.from(3, 0),
				Vector2i.from(4, 0)
		).toArray(Cheese[]::new);
		Assert.assertEquals(2, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, 0),  //top right
				Vector2d.from(32, 16), //bottom right
				Vector2d.from(0, 16),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));

		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(80, 0),  //top right
				Vector2d.from(80, 16), //bottom right
				Vector2d.from(48, 16), //bottom left
				Vector2d.from(48, 0)   //top left
		)));
	}

	@Test
	public void cheesePlatterWithFarawayCheeses() {
		// ##.....
		// .......
		// ......# <- actually even further away
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(0, 0),
				Vector2i.from(1, 0),
				Vector2i.from(12, 34)
		).toArray(Cheese[]::new);
		Assert.assertEquals(2, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, 0),  //top right
				Vector2d.from(32, 16), //bottom right
				Vector2d.from(0, 16),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));

		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(208, 544), //top right
				Vector2d.from(208, 560), //bottom right
				Vector2d.from(192, 560), //bottom left
				Vector2d.from(192, 544)  //top left
		)));
	}

	@Test
	public void cheesePlatter4() {
		// ##..#
		// #..##
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(0, 0),
				Vector2i.from(1, 0),
				Vector2i.from(0, 1),
				Vector2i.from(4, 0),
				Vector2i.from(3, 1),
				Vector2i.from(4, 1)
		).toArray(Cheese[]::new);
		Assert.assertEquals(2, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, 0),  //top right
				Vector2d.from(32, 16), //right bottom
				Vector2d.from(16, 16), //middle
				Vector2d.from(16, 32), //bottom right
				Vector2d.from(0, 32),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));

		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(64, 16), //middle
				Vector2d.from(64, 0),  //top left
				Vector2d.from(80, 0),  //top right
				Vector2d.from(80, 32), //bottom right
				Vector2d.from(48, 32), //bottom left
				Vector2d.from(48, 16)  //left top
		)));
	}

	@Test
	public void cheesePlatterTwoBigDiagonalNeighbours() {
		// .##...
		// .##...
		// ...##.
		// ...##.
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(1, 0),
				Vector2i.from(2, 0),

				Vector2i.from(1, 1),
				Vector2i.from(2, 1),

				Vector2i.from(3, 2),
				Vector2i.from(4, 2),

				Vector2i.from(3, 3),
				Vector2i.from(4, 3)
		).toArray(Cheese[]::new);
		Assert.assertEquals(2, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(48, 0),  //top right
				Vector2d.from(48, 32), //bottom right
				Vector2d.from(16, 32), //bottom left
				Vector2d.from(16, 0)   //top left
		)));

		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(80, 32), //top right
				Vector2d.from(80, 64), //bottom left
				Vector2d.from(48, 64), //bottom right
				Vector2d.from(48, 32)  //top left
		)));
	}

	@Test
	public void cheesePlatterCreeperFace() {
		// ##..##
		// ##..##
		// ..##..
		// .####.
		// .####.
		// .#..#.
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(0, 0),
				Vector2i.from(1, 0),
				Vector2i.from(4, 0),
				Vector2i.from(5, 0),

				Vector2i.from(0, 1),
				Vector2i.from(1, 1),
				Vector2i.from(4, 1),
				Vector2i.from(5, 1),

				Vector2i.from(2, 2),
				Vector2i.from(3, 2),

				Vector2i.from(1, 3),
				Vector2i.from(2, 3),
				Vector2i.from(3, 3),
				Vector2i.from(4, 3),

				Vector2i.from(1, 4),
				Vector2i.from(2, 4),
				Vector2i.from(3, 4),
				Vector2i.from(4, 4),

				Vector2i.from(1, 5),
				Vector2i.from(4, 5)
		).toArray(Cheese[]::new);
		Assert.assertEquals(3, platter.length);

		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(32, 48), //nose bottom left
				Vector2d.from(32, 32), //nose top left
				Vector2d.from(64, 32), //nose top right
				Vector2d.from(64, 48), //nose bottom right
				Vector2d.from(80, 48), //right (cheek)
				Vector2d.from(80, 96), //bottom right most
				Vector2d.from(64, 96), //bottom mouth bottom right
				Vector2d.from(64, 80), //bottom mouth top right
				Vector2d.from(32, 80), //bottom mouth top left
				Vector2d.from(32, 96), //bottom mouth bottom left
				Vector2d.from(16, 96), //bottom left most
				Vector2d.from(16, 48)  //left (cheek)
		)));

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, 0),  //top right
				Vector2d.from(32, 32), //bottom right
				Vector2d.from(0, 32),  //bottom left
				Vector2d.from(0, 0)    //top left
		)));

		compareCheese(platter[2], new Cheese(new Shape(
				Vector2d.from(96, 0),  //top right
				Vector2d.from(96, 32), //bottom right
				Vector2d.from(64, 32), //bottom left
				Vector2d.from(64, 0)   //top left
		)));
	}

	@Test
	public void cheesePlatterWithHole() {
		// ###
		// #.#
		// ###
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(-1, -1), //top left
				Vector2i.from(0, -1),  //top middle
				Vector2i.from(1, -1),  //top right
				Vector2i.from(-1, 0),  //middle left
				Vector2i.from(1, 0),   //middle right
				Vector2i.from(-1, 1),  //bottom left
				Vector2i.from(0, 1),   //bottom middle
				Vector2i.from(1, 1)    //bottom right
		).toArray(Cheese[]::new);
		Assert.assertEquals(1, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, -16), //top right
				Vector2d.from(32, 32),  //bottom right
				Vector2d.from(-16, 32), //bottom left
				Vector2d.from(-16, -16) //top left
		),
				new Shape(
						Vector2d.from(0, 16),  //bottom left
						Vector2d.from(16, 16), //bottom right
						Vector2d.from(16, 0),  //top right
						Vector2d.from(0, 0)    //top left
				)
		));
	}

	@Test
	public void cheesePlatterPairOfSquareBrackets() {
		// ##.##
		// #...#
		// ##.##

		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(0, 0), //[ left top
				Vector2i.from(0, 1), //[ left middle
				Vector2i.from(0, 2), //[ left bottom

				Vector2i.from(1, 0), //[ right top
				Vector2i.from(1, 2), //[ right bottom

				Vector2i.from(3, 0), //] left top
				Vector2i.from(3, 2), //] left bottom

				Vector2i.from(4, 0), //] right top
				Vector2i.from(4, 1), //] right middle
				Vector2i.from(4, 2)  //] right bottom
		).toArray(Cheese[]::new);
		Assert.assertEquals(2, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, 0), //[ right top
				Vector2d.from(32, 16),  //[ right middle top right
				Vector2d.from(16, 16), //[ right middle top left
				Vector2d.from(16, 32), //[ right middle bottom left
				Vector2d.from(32, 32), //[ right middle bottom right
				Vector2d.from(32, 48),  //[ right bottom
				Vector2d.from(0, 48), //[ left bottom
				Vector2d.from(0, 0) //[ left top
		)));

		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(80, 0), //] right top
				Vector2d.from(80, 48), //] right bottom
				Vector2d.from(48, 48), //] left bottom
				Vector2d.from(48, 32), //] left middle bottom left
				Vector2d.from(64, 32), //] left middle bottom right
				Vector2d.from(64, 16), //] left middle top right
				Vector2d.from(48, 16), //] left middle top left
				Vector2d.from(48, 0) //] left top
		)));
	}

	@Test
	public void cheesePlatterTwoSquaresWithHoles() {
		// ###..###
		// #.#..#.#
		// ###..###
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(-1, -1), //L top left
				Vector2i.from(0, -1),  //L top middle
				Vector2i.from(1, -1),  //L top right

				Vector2i.from(-1, 0),  //L middle left
				Vector2i.from(1, 0),   //L middle right

				Vector2i.from(-1, 1),  //L bottom left
				Vector2i.from(0, 1),   //L bottom middle
				Vector2i.from(1, 1),   //L bottom right

				Vector2i.from(4, -1), //R top left
				Vector2i.from(5, -1), //R top middle
				Vector2i.from(6, -1), //R top right

				Vector2i.from(4, 0),  //R middle left
				Vector2i.from(6, 0),  //R middle right

				Vector2i.from(4, 1),  //R bottom left
				Vector2i.from(5, 1),  //R bottom middle
				Vector2i.from(6, 1)   //R bottom right
		).toArray(Cheese[]::new);
		Assert.assertEquals(2, platter.length);

		// Left square
		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(32, -16), //top right
				Vector2d.from(32, 32),  //bottom right
				Vector2d.from(-16, 32), //bottom left
				Vector2d.from(-16, -16) //top left
		),
				new Shape(
						Vector2d.from(0, 16),  //bottom left
						Vector2d.from(16, 16), //bottom right
						Vector2d.from(16, 0),  //top right
						Vector2d.from(0, 0)    //top left
				)
		));

		// Right square
		compareCheese(platter[1], new Cheese(new Shape(
				Vector2d.from(112, -16), //top right
				Vector2d.from(112, 32),  //bottom right
				Vector2d.from(64, 32),   //bottom left
				Vector2d.from(64, -16)   //top left
		),
				new Shape(
						Vector2d.from(80, 16), //bottom left
						Vector2d.from(96, 16), //bottom right
						Vector2d.from(96, 0),  //top right
						Vector2d.from(80, 0)   //top left
				)
		));
	}

	@Test
	public void cheesePlatterBluesWonkyShape() {
		// .###...
		// .###...
		// .#..##.
		// .#####.
		Cheese[] platter = Cheese.createPlatterFromChunks(
				Vector2i.from(1, 0),
				Vector2i.from(2, 0),
				Vector2i.from(3, 0),

				Vector2i.from(1, 1),
				Vector2i.from(2, 1),
				Vector2i.from(3, 1),

				Vector2i.from(1, 2),
				Vector2i.from(4, 2),
				Vector2i.from(5, 2),

				Vector2i.from(1, 3),
				Vector2i.from(2, 3),
				Vector2i.from(3, 3),
				Vector2i.from(4, 3),
				Vector2i.from(5, 3)
		).toArray(Cheese[]::new);
		Assert.assertEquals(1, platter.length);

		compareCheese(platter[0], new Cheese(new Shape(
				Vector2d.from(64, 0),  //top right
				Vector2d.from(64, 32), //top right middle
				Vector2d.from(96, 32), //bottom right top
				Vector2d.from(96, 64), //bottom right bottom
				Vector2d.from(16, 64), //bottom left
				Vector2d.from(16, 0)   //top left
		),
				new Shape(
						Vector2d.from(32, 48), //top left
						Vector2d.from(64, 48), //top right
						Vector2d.from(64, 32), //bottom right
						Vector2d.from(32, 32)   //bottom left
				)
		));
	}

	//endregion Platter
}
