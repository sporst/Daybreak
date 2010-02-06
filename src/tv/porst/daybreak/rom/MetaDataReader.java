package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.helpers.ArrayHelpers;
import net.sourceforge.jnhf.helpers.ByteHelpers;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.BlockAttribute;
import tv.porst.daybreak.model.Mappers;
import tv.porst.daybreak.model.MetaData;

public class MetaDataReader
{
	private static byte[] readAdditionalScrollingData(final byte[] data, final int offset, final int levelId)
	{
		final int[] pointers = PointerReader.readPointers(data, offset, 8);

		final int pointer = pointers[Mappers.lvlToChunk(levelId)];

		final List<byte[]> scrollingData = new ArrayList<byte[]>();

		final int baseOffset = 0x3C010 - 0xC000 + pointer;

		for (int i=0;;i+=5)
		{
			if (data[baseOffset + i] == -1)
			{
				break;
			}

			scrollingData.add(ArrayHelpers.copy(data, baseOffset+ i, 5));
		}

		final byte[] array = new byte[5 * scrollingData.size()];

		for (int i=0;i<scrollingData.size();i++)
		{
			System.arraycopy(scrollingData.get(i), 0, array, 5 * i, 5);
		}

		return array;
	}

	private static byte[] readAdditionalScrollingData2(final byte[] data, final int offset, final int levelId)
	{
		final int[] pointers = PointerReader.readPointers(data, offset, 8);

		final int pointer = pointers[Mappers.lvlToChunk(levelId)];

		final List<byte[]> scrollingData = new ArrayList<byte[]>();

		final int baseOffset = 0x3C010 - 0xC000 + pointer;

		for (int i=0;;i+=4)
		{
			if (data[baseOffset + i] == -1)
			{
				break;
			}

			scrollingData.add(ArrayHelpers.copy(data, baseOffset+ i, 4));
		}

		final byte[] array = new byte[4 * scrollingData.size()];

		for (int i=0;i<scrollingData.size();i++)
		{
			System.arraycopy(scrollingData.get(i), 0, array, 4 * i, 4);
		}

		return array;
	}

	private static BlockAttribute[] readAttributes(final byte[] data, final int offset, final int tiles)
	{
		final BlockAttribute[] attributes = new BlockAttribute[tiles];

		for (int i=0;i<tiles;i++)
		{
			attributes[i] = new BlockAttribute(data[offset + i]);
		}

		return attributes;
	}

	private static byte[] readDoorDestinations(final byte[] data, final int offset, final int doors)
	{
		return ArrayHelpers.copy(data, offset, 4 * doors);
	}

	private static byte[] readDoorLocations(final byte[] data, final int offset, final int doors)
	{
		return ArrayHelpers.copy(data, offset, doors);
	}

	private static byte[] readProperties(final byte[] data, final int offset, final int tiles)
	{
		return ArrayHelpers.copy(data, offset, tiles);
	}

	private static byte[] readScrollData(final byte[] data, final int offset, final int screens)
	{
		return ArrayHelpers.copy(data, offset, 4 * screens);
	}

	private static Block[] readTsaData(final byte[] data, final int baseOffset, final int tsa1, final int tsa2, final int tsa3, final int tsa4, final BlockAttribute[] attributes)
	{
		final int tiles = tsa2 - tsa1;

		final Block[] blocks = new Block[tiles];

		for (int i=0;i<tiles;i++)
		{
			final int tile1 = data[baseOffset + tsa1 + i] & 0xFF;
			final int tile2 = data[baseOffset + tsa2 + i] & 0xFF;
			final int tile3 = data[baseOffset + tsa3 + i] & 0xFF;
			final int tile4 = data[baseOffset + tsa4 + i] & 0xFF;

			blocks[i] = new Block(i, attributes[i], tile1, tile2, tile3, tile4);
		}

		return blocks;
	}

	public static MetaData read(final byte[] data, final int levelId, final int screenCount, final int screensInLevel)
	{
		final int baseOffset = 0xC010;

		final int metaTablePointer = ByteHelpers.readWordLittleEndian(data, baseOffset);

		final int metaPointer = ByteHelpers.readWordLittleEndian(data, baseOffset + metaTablePointer + 2 * Mappers.lvlToChunk(levelId));

		final int[] metaPointers = PointerReader.readPointers(data, baseOffset + metaPointer, 10);

		final int tiles = metaPointers[7] - metaPointers[6];
		final int doors = -1 + metaPointers[4] - metaPointers[3];

		final int attributePointer = ByteHelpers.readWordLittleEndian(data, baseOffset + metaPointers[0]);

		final BlockAttribute[] attributes = readAttributes(data, baseOffset + attributePointer, tiles);
		final byte[] properties = readProperties(data, baseOffset + metaPointers[1], tiles);
		final byte[] scrollData = readScrollData(data, baseOffset + metaPointers[2], screensInLevel);
		final byte[] doorLocations = readDoorLocations(data, baseOffset + metaPointers[3], doors);
		final byte[] doorDestinations = readDoorDestinations(data, baseOffset + metaPointers[4], doors);

		final Block[] blocks = readTsaData(data, baseOffset, metaPointers[6], metaPointers[7], metaPointers[8], metaPointers[9], attributes);

		final byte[] scrollingData = readAdditionalScrollingData(data, 0x3EAAC, levelId);
		final byte[] scrollingData2 = readAdditionalScrollingData2(data, 0x3EA47, levelId);

		return new MetaData(properties, blocks, scrollData, doorLocations, doorDestinations, scrollingData, scrollingData2);
	}
}
