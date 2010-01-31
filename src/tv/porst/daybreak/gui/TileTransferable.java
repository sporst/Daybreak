package tv.porst.daybreak.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.model.TileInformation;

public class TileTransferable implements Transferable
{
	private static final DataFlavor[] SUPPORTED_FLAVORS = new DataFlavor[1];

	public static final DataFlavor TILE_FLAVOR;

	private final TileData m_tiles;

	static
	{
		TILE_FLAVOR = new DataFlavor(TileInformation.class, "TileInformation");
		SUPPORTED_FLAVORS[0] = TILE_FLAVOR;
	}

	public TileTransferable(final TileData tileData)
	{
		m_tiles = tileData;
	}

	public TileData getTile()
	{
		return m_tiles;
	}

	@Override
	public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if (flavor.equals(TILE_FLAVOR))
		{
			return m_tiles;
		}

		throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return SUPPORTED_FLAVORS.clone();
	}

	@Override
	public boolean isDataFlavorSupported(final DataFlavor flavor)
	{
		return flavor.equals(TILE_FLAVOR);
	}
}
