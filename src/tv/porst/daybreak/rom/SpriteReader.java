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
	private static byte[] readHorizontalSizes(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3B4E1, 7);
	}

	private static byte[] readRequiredTiles(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3CE2B, 0x37);
	}

	private static byte[] readSpriteSizes(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3B4EF, 0x37);
	}

	private static byte[] readVerticalSizes(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3B4E8, 7);
	}

	public static List<Sprite> read(final byte[] data, final Palette palette)
	{
		final byte[] spriteSizesH = readHorizontalSizes(data);
		final byte[] spriteSizesV = readVerticalSizes(data);
		final byte[] spriteSizes = readSpriteSizes(data);
		final byte[] spriteTiles = readRequiredTiles(data);

		final List<Sprite> sprites = new ArrayList<Sprite>();

		final int offset = 0x18010;
		final int spritePointerTableOffset = PointerReader.readPointer(data, offset);
		final int[] spriteDataPointers = PointerReader.readPointers(data, offset + spritePointerTableOffset, spriteTiles.length);

		for (int i=0;i<spriteTiles.length;i++)
		{
			final int spriteDataPointer = spriteDataPointers[i];
			final byte tileCount = spriteTiles[i];

			System.out.printf("%08X\n", offset + spriteDataPointer);

			final IFilledList<TileData> tiles = TileDataReader.readTiles(data, offset + spriteDataPointer, tileCount);

			final int blockWidth = (spriteSizesH[spriteSizes[i]] + 1) / 8;
			final int blockHeight = (spriteSizesV[spriteSizes[i]] + 1) / 8;

			System.out.println(String.format("%02X", i) + " " + tileCount + ": " + blockHeight + " - " + blockWidth);

			if (blockWidth * blockHeight > tileCount)
			{
				continue;
			}

			final TileData[][] spriteData = new TileData[blockHeight][blockWidth];

			for (int j=0;j<blockWidth * blockHeight;j++)
			{
				spriteData[j / blockWidth][j % blockWidth] = tiles.get(j);
			}

			final Palette palette2 = new Palette(new byte[] {
				    (byte)0x0F, (byte)0x18, (byte)0x26, (byte)0x30, (byte)0x0F, (byte)0x00, (byte)0x25, (byte)0x30, (byte)0x0F, (byte)0x0F,
				    (byte)0x1C, (byte)0x33, (byte)0x0F, (byte)0x0F, (byte)0x27, (byte)0x30
				});

			sprites.add(new Sprite(spriteData, palette2));
		}

		return sprites;
	}
}
