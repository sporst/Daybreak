package tv.porst.daybreak.gui;

import java.awt.image.BufferedImage;

import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.model.Sprite;

public class SpriteBitmap extends BufferedImage
{
	private static final int TILE_WIDTH = 32;

	public SpriteBitmap(final Sprite sprite)
	{
		super(sprite.width() * TILE_WIDTH, sprite.height() * TILE_WIDTH, TYPE_3BYTE_BGR);

		final TileData[][] tiles = sprite.getTiles();

		for (int i=0;i<tiles.length;i++)
		{
			for (int j=0;j<tiles[i].length;j++)
			{
				final TileBitmap bitmap = new TileBitmap(tiles[i][j], 2, sprite.getPalette());

				getGraphics().drawImage(bitmap, j * TILE_WIDTH, i * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, null);
			}
		}
	}
}
