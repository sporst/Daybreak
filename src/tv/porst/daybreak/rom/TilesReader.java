package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.helpers.ArrayHelpers;
import net.sourceforge.jnhf.romfile.INesHeader;
import net.sourceforge.jnhf.romfile.TileDataReader;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.TileInformation;

public class TilesReader
{
	public static List<TileInformation> read(final byte[] data)
	{
		final int offset = 0x3CF17;

		final int[] tilePointers = PointerReader.readPointers(data, offset, 9);
		final byte[] tables = ArrayHelpers.copy(data, 0x3CF29, 9);
		final byte[] tileNumbers = ArrayHelpers.copy(data, 0x3CF32, 9);

		final List<TileInformation> tileInformation = new ArrayList<TileInformation>();

		final int bankOffset = 4 * 0x4000 + INesHeader.NES_HEADER_SIZE;

		for (int i=0;i<tilePointers.length;i++)
		{
			final int tileCount = (tileNumbers[i] & 0xFF) * 0x10;
			final int startLocation = (tables[i] & 0x0F) << 4;

			tileInformation.add(new TileInformation(startLocation, TileDataReader.readTiles(data, bankOffset + tilePointers[i] - 0x8000, tileCount)));
		}

		return tileInformation;
	}
}
