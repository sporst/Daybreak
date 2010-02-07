package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteLocation;

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
		final List<SpriteLocation> sprites = screen.getSpriteData();

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

				draw(block, bitmap);

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

		for (final SpriteLocation spriteLocation : sprites)
		{
			final int x = spriteLocation.getX();
			final int y = spriteLocation.getY();

			final Sprite sprite = spriteLocation.getSprite();

			if (sprite != null)
			{
				final SpriteBitmap bitmap = new SpriteBitmap(sprite);

				g.drawImage(bitmap, x * TILE_WIDTH, y * TILE_WIDTH, sprite.width() * 8, sprite.height() * 8, null);
			}
		}

		for (int x=0;x<TILES_X;x++)
		{
			for (int y=0;y<TILES_Y;y++)
			{
				final int blockId = squareNumbers[y][x];
				final Block block = blocks[blockId];

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

	private void draw(final Block block, final BlockBitmap bitmap)
	{
		Color color = null;

		final Graphics g = bitmap.getGraphics();

		if (block.getProperty().isSolid())
		{
			color = Color.GREEN;
		}
		else if (block.getProperty().isDoor())
		{
			color = Color.RED;
		}

		if (color != null)
		{
			g.setColor(color);

			for (int i=0;i<2 * bitmap.getWidth();i+=2)
			{
				g.drawLine(0, i, i, 0);
			}
		}
	}
}
