package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.BlockAttribute;
import tv.porst.daybreak.model.TileInformation;

public class BlockBitmap extends BufferedImage
{
	private static Image DEFAULT_TILE = new BufferedImage(8, 8, TYPE_3BYTE_BGR);

	private static Image ERROR_TILE;

	static
	{
		ERROR_TILE = new BufferedImage(8, 8, TYPE_3BYTE_BGR);

		final Graphics g = ERROR_TILE.getGraphics();

		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 4, 4);
		g.fillRect(4, 4, 4, 4);

		g.setColor(Color.RED);
		g.fillRect(4, 0, 4, 4);
		g.fillRect(0, 4, 4, 4);
	}

	public BlockBitmap(final Block block, final TileInformation tileInformation, final Palette palette)
	{
		super(16, 16, TYPE_3BYTE_BGR);

		final BlockAttribute attribute = block.getAttribute();

		final IFilledList<TileData> tileData = tileInformation.getTileData();

		final int start = tileInformation.getStartLocation();

		final Image upperLeft = getTileBitmap(block.getTile1(), start, tileData, attribute.topLeft(), palette);
		final Image upperRight = getTileBitmap(block.getTile2(), start, tileData, attribute.topRight(), palette);
		final Image lowerLeft = getTileBitmap(block.getTile3(), start, tileData, attribute.bottomLeft(), palette);
		final Image lowerRight = getTileBitmap(block.getTile4(), start, tileData, attribute.bottomRight(), palette);

		getGraphics().drawImage(upperLeft, 0, 0, null);
		getGraphics().drawImage(upperRight, 8, 0, null);
		getGraphics().drawImage(lowerLeft, 0, 8, null);
		getGraphics().drawImage(lowerRight, 8, 8, null);
	}

	private Image getTileBitmap(final int tile, final int start, final IFilledList<TileData> tileData, final int attribute, final Palette palette)
	{
		if (tile == 0)
		{
			return DEFAULT_TILE;
		}

		final int tileIndex1 = tile - start;

		if (tileIndex1 < 0 || tileIndex1 >= tileData.size())
		{
			return ERROR_TILE;
		}

		return new TileBitmap(tileData.get(tileIndex1), attribute, palette);
	}
}
