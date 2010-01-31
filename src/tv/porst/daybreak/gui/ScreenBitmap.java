package tv.porst.daybreak.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;

public class ScreenBitmap extends BufferedImage
{
	private static int TILES_X = 16;
	private static int TILES_Y = 13;
	private static int TILE_WIDTH = 16;

	public ScreenBitmap(final Screen screen, final MetaData metaData)
	{
		super(16 * 16, 16 * 13, TYPE_3BYTE_BGR);

		final int[][] squareNumbers = screen.getSquareNumbers();
		final Block[] blocks = metaData.getBlocks();

		final Graphics g = getGraphics();

		for (int x=0;x<TILES_X;x++)
		{
			for (int y=0;y<TILES_Y;y++)
			{
				final int blockId = squareNumbers[y][x];
				final Block block = blocks[blockId];

				final BlockBitmap bitmap = new BlockBitmap(block, screen.getTiles(), screen.getPalette());

				g.drawImage(bitmap, x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, null);
			}
		}
	}
}
