package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;

public class ScreenBitmap extends BufferedImage
{
	private static int TILES_X = 16;
	private static int TILES_Y = 13;
	private static int TILE_WIDTH = 16;

	public ScreenBitmap(final Screen screen, final MetaData metaData, final int mouseRow, final int mouseCol, final Block highlightedBlock)
	{
		super(16 * 16, 16 * 13, TYPE_3BYTE_BGR);

		final int[][] squareNumbers = screen.getSquareNumbers();
		final Block[] blocks = metaData.getBlocks();

		final Graphics g = getGraphics();

		final Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int x=0;x<TILES_X;x++)
		{
			for (int y=0;y<TILES_Y;y++)
			{
				final int blockId = squareNumbers[y][x];
				final Block block = blocks[blockId];

				final BlockBitmap bitmap = new BlockBitmap(block, screen.getTiles(), screen.getPalette());

				if (highlightedBlock == block || highlightedBlock == null)
				{
					g2d.setComposite(Transparency.makeComposite(1.0f));
				}
				else
				{
					g2d.setComposite(Transparency.makeComposite(0.3f));
				}

				g.drawImage(bitmap, x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, null);

				if (x == mouseCol && y == mouseRow)
				{
					g.setColor(Color.YELLOW);
					g.drawRect(x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH - 1, TILE_WIDTH - 1);
				}

				if (highlightedBlock == block)
				{
					g.setColor(Color.RED);
					g.drawRect(x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH - 1, TILE_WIDTH - 1);
				}
			}
		}
	}
}
