package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import tv.porst.daybreak.gui.sprites.SpriteBitmap;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteLocation;

public class ScreenBitmap extends BufferedImage
{
	private static int TILES_X = 16;
	private static int TILES_Y = 13;
	private static int TILE_WIDTH = 16;

	public ScreenBitmap(final Screen screen, final ScreenPanelOptions options, final Block[] blocks, final int mouseRow, final int mouseCol, final Block highlightedBlock)
	{
		super(16 * 16, 16 * 13, TYPE_3BYTE_BGR);

		final int[][] squareNumbers = screen.getSquareNumbers();
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

				BlockHighlighter.highlight(options.getHighlightingOptions(), block, bitmap);

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
}
