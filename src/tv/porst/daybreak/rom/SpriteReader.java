package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ArrayHelpers;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.romfile.TileData;
import net.sourceforge.jnhf.romfile.TileDataReader;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.Sprite;

public class SpriteReader
{
	private static byte[] readRequiredTiles(final byte[] data)
	{
		final int offset = 0x3CE2B;

		return ArrayHelpers.copy(data, offset, 0x30);
	}

	public static List<Sprite> read(final byte[] data, final Palette palette)
	{
		final byte[] spriteTiles = readRequiredTiles(data);

		final List<Sprite> sprites = new ArrayList<Sprite>();

		final int offset = 0x18010;
		final int spritePointerTableOffset = PointerReader.readPointer(data, offset);
		final int[] spriteDataPointers = PointerReader.readPointers(data, offset + spritePointerTableOffset, spriteTiles.length);

		for (int i=0;i<spriteTiles.length;i++)
		{
			final int spriteDataPointer = spriteDataPointers[i];
			final byte tileCount = spriteTiles[i];

			final IFilledList<TileData> tiles = TileDataReader.readTiles(data, offset + spriteDataPointer, tileCount);

			if (tiles.size() >= 8)
			{
				final TileData[][] spriteData = new TileData[4][2];

				for (int j=0;j<8;j++)
				{
					spriteData[j / 2][j % 2] = tiles.get(j);
				}

				sprites.add(new Sprite(spriteData, palette));

			}
		}

		return sprites;
	}
}
