package tv.porst.daybreak.rom;

import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ArrayHelpers;
import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.romfile.TileData;
import net.sourceforge.jnhf.romfile.TileDataReader;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.Sprite;

public class SpriteReader
{
	private static int SPRITES = 0x65;

	private static void readAnimations(final byte[] data)
	{
		// 0x00   -
		// 0x01   0
		// 0x02   0 1
		// 0x03   ??
		// 0x04   0 1 2 3
		// 0x05   0 1
		// 0x06   0 1 2
		// 0x07   0 1
		// 0x08   0 1
		// 0x09   0 1
		// 0x0A   0 1
		// 0x0B   0 1
		// 0x0C   0 1
		// 0x0D   0 1 2
		// 0x0E   0 1 2 3
		// 0x0F   0 1 2 3?
		// 0x10

		final int sprites = 102;
		final int phaseIndexTableOffset = 0x38CAF;

		final byte[] phaseIndexTable = ArrayHelpers.copy(data, phaseIndexTableOffset, sprites);

		final int bankOffset = 0x1C010;
		final int offset = 0x1C016;

		final int animationPointer = bankOffset + PointerReader.readPointer(data, offset);

		System.out.printf("%04X\n", animationPointer - bankOffset);

		for (int i=0x2A;i<0x2B;i++)
		{
			final int normalized = phaseIndexTable[i];

			System.out.printf("F072: %04X\n", animationPointer - bankOffset + normalized * 2);

			final int ap = bankOffset + PointerReader.readPointer(data, animationPointer + normalized * 2);

			System.out.printf("F07F: %04X\n", ap - bankOffset);

			System.out.printf("%02X\n", data[ap]);

			final int height = 1 + (data[ap] >>> 4);
			final int width = 1 + (data[ap] & 0xF);

			final int anim1 = ap + 3;

			final byte[] animBytes = ArrayHelpers.copy(data, anim1, 2 * height * width);

			for (int j=1;j<animBytes.length;j+=2)
			{
				System.out.println(animBytes[j]);
			}
		}

		System.exit(0);
	}

	private static byte[] readHorizontalSizes(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3B4E1, 7);
	}

	private static byte[] readRequiredTiles(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3CE2B, SPRITES);
	}

	private static byte[] readSpriteSizes(final byte[] data)
	{
		return ArrayHelpers.copy(data, 0x3B4EF, SPRITES);
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
		readAnimations(data);

		final List<Sprite> sprites = new FilledList<Sprite>();

		for (int i=0;i<spriteTiles.length;i++)
		{
			final int offset = 0x18010 + (i >= 0x37 ? 0x4000 : 0);
			final int spritePointerTableOffset = PointerReader.readPointer(data, offset);
//			final int[] spriteDataPointers = PointerReader.readPointers(data, offset + spritePointerTableOffset, SPRITES);

			final int normalizedIndex = i >= 0x37 ? i - 0x37 : i;

			final int spriteDataPointer = PointerReader.readPointer(data, offset + spritePointerTableOffset + normalizedIndex * 2);
			final byte tileCount = spriteTiles[i];

			System.out.printf("%08X\n", offset + spriteDataPointer);

			final IFilledList<TileData> tiles = TileDataReader.readTiles(data, offset + spriteDataPointer, tileCount);

			final int blockWidth = (spriteSizesH[spriteSizes[normalizedIndex]] + 1) / 8;
			final int blockHeight = (spriteSizesV[spriteSizes[normalizedIndex]] + 1) / 8;

			System.out.println(String.format("%02X", i) + " " + tileCount + ": " + blockHeight + " - " + blockWidth);

			final TileData[][] spriteData = new TileData[blockHeight][blockWidth];

			if (blockWidth * blockHeight > tileCount)
			{
//				sprites.add(null);

//				continue;
			}

//			for (int j=0;j<blockWidth * blockHeight;j++)
			for (int j=0;j<Math.min(tileCount, blockWidth * blockHeight);j++)
			{
				spriteData[j / blockWidth][j % blockWidth] = tiles.get(j);
			}

//			final Palette palette2 = new Palette(new byte[] {
//				    (byte)0x0F, (byte)0x18, (byte)0x26, (byte)0x30, (byte)0x0F, (byte)0x00, (byte)0x25, (byte)0x30, (byte)0x0F, (byte)0x0F,
//				    (byte)0x1C, (byte)0x33, (byte)0x0F, (byte)0x0F, (byte)0x27, (byte)0x30
//				});

			sprites.add(new Sprite(i, spriteData, palette));
		}

		return sprites;
	}
}
