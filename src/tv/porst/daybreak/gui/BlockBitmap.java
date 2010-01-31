package tv.porst.daybreak.gui;

import java.awt.image.BufferedImage;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.BlockAttribute;
import tv.porst.daybreak.model.TileInformation;

public class BlockBitmap extends BufferedImage
{
	private final TileBitmap DEFAULT_TILE;

	public BlockBitmap(final Block block, final TileInformation tileInformation, final Palette palette)
	{
		super(16, 16, TYPE_3BYTE_BGR);

		DEFAULT_TILE = new TileBitmap(new TileData(new byte[64]), 0, palette);

		final BlockAttribute attribute = block.getAttribute();

		final IFilledList<TileData> tileData = tileInformation.getTileData();

		final int start = tileInformation.getStartLocation();

		final int tileIndex1 = block.getTile1() - start;
		final int tileIndex2 = block.getTile2() - start;
		final int tileIndex3 = block.getTile3() - start;
		final int tileIndex4 = block.getTile4() - start;

		final TileBitmap upperLeft = block.getTile1() == 0 ? DEFAULT_TILE : new TileBitmap(tileData.get(tileIndex1), attribute.topLeft(), palette);
		final TileBitmap upperRight = block.getTile2() == 0 ? DEFAULT_TILE : new TileBitmap(tileData.get(tileIndex2), attribute.topRight(), palette);
		final TileBitmap lowerLeft = block.getTile3() == 0 ? DEFAULT_TILE : new TileBitmap(tileData.get(tileIndex3), attribute.bottomLeft(), palette);
		final TileBitmap lowerRight = block.getTile4() == 0 ? DEFAULT_TILE : new TileBitmap(tileData.get(tileIndex4), attribute.bottomRight(), palette);

		getGraphics().drawImage(upperLeft, 0, 0, null);
		getGraphics().drawImage(upperRight, 8, 0, null);
		getGraphics().drawImage(lowerLeft, 0, 8, null);
		getGraphics().drawImage(lowerRight, 8, 8, null);
	}
}
