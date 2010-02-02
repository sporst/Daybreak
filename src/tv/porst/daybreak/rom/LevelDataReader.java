package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.BitReader;
import net.sourceforge.jnhf.helpers.ByteHelpers;
import net.sourceforge.jnhf.romfile.INesHeader;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Mappers;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.SpriteLocation;
import tv.porst.daybreak.model.TileInformation;

public final class LevelDataReader
{
	private static TileInformation getTileInformation(final int levelIndex, final int screenIndex, final List<TileInformation> tileInformation)
	{
		if (levelIndex == 6)
		{
			return tileInformation.get(Mappers.HOUSE_TILE_INDICES[screenIndex]);
		}
		else
		{
			return tileInformation.get(Mappers.levelToTiles(levelIndex));
		}
	}

	private static int[][] readScreen(final byte[] data, final int levelDataPointer)
	{
		final int[][] squareNumbers = new int[13][16];

		final BitReader reader = new BitReader(data, levelDataPointer);

		for (int y=0;y<squareNumbers.length;y++)
		{
			for (int x=0;x<squareNumbers[y].length;x++)
			{
				final int next = reader.readBits(2);

				switch(next)
				{
				case 0: squareNumbers[y][x] = x == 0 ? squareNumbers[y-1][15] : squareNumbers[y][x-1]; break;
				case 1: squareNumbers[y][x] = squareNumbers[y-1][x]; break;
				case 2: squareNumbers[y][x] = x == 0 ? squareNumbers[y-2][15] : squareNumbers[y-1][x-1]; break;
				case 3: squareNumbers[y][x] = reader.readBits(8); break;
				}
			}
		}

		return squareNumbers;
	}

	private static List<Level> readScreenData(final byte[] data, final int bank, final int levelId, final List<TileInformation> tileInformation, final List<Palette> palettes)
	{
		final List<Level> levels = new ArrayList<Level>();

		PointerReader.readTable(data, 0, INesHeader.NES_HEADER_SIZE + bank * 0x4000);

		for (final int pointer : PointerReader.readTable(data, 0, INesHeader.NES_HEADER_SIZE + bank * 0x4000))
		{
			final List<Screen> screens = new ArrayList<Screen>();

			final int[] screenPointers = PointerReader.readTable(data, pointer, INesHeader.NES_HEADER_SIZE + bank * 0x4000);

			for (final int pointer2 : screenPointers)
			{
				final TileInformation tiles = getTileInformation(levelId, screens.size(), tileInformation);
				final Palette palette = PaletteAssigner.find(palettes, levelId, screens.size());

				final List<SpriteLocation> spriteData = readSpriteData(data, levelId, screens.size());

				final int[][] squareNumbers = readScreen(data, INesHeader.NES_HEADER_SIZE + bank * 0x4000 + pointer2);

				final Screen screen = new Screen(squareNumbers, spriteData, tiles, palette);

				screens.add(screen);
			}

			final MetaData metaData = MetaDataReader.read(data, levelId + levels.size(), screens.size(), screenPointers.length);

			levels.add(new Level(metaData, screens));
		}

		return levels;
	}

	private static List<SpriteLocation> readSpriteData(final byte[] data, final int levelId, final int screenId)
	{
		final int bankOffset = 0x2C010;
		final int startOffset = 0x2C220;
		final int chunkId = Mappers.lvlToChunk(levelId);

		final int spriteDefinitionPointer = ByteHelpers.readWordLittleEndian(data, startOffset + 2 * chunkId);

		final int screenPointer = ByteHelpers.readWordLittleEndian(data, bankOffset + spriteDefinitionPointer - 0x8000 + 2 * screenId);

		final int spriteDataPointer = bankOffset + screenPointer - 0x8000;

		final List<Byte> spriteIds = new ArrayList<Byte>();
		final List<Byte> spriteLocations = new ArrayList<Byte>();
		final List<Byte> messageIds = new ArrayList<Byte>();

		boolean done = false;

		for (int i=0;;i+=2)
		{
			final byte spriteId = data[spriteDataPointer + i];

			if (spriteId == -1)
			{
				for (int j=i;;j++)
				{
					final byte messageId = data[spriteDataPointer + j];

					if (messageId == -1)
					{
						done = true;

						break;
					}
					else
					{
						messageIds.add(messageId);
					}
				}

				if (done)
				{
					break;
				}
			}
			else
			{
				final byte spriteLocation= data[spriteDataPointer + i + 1];

				spriteIds.add(spriteId);
				spriteLocations.add(spriteLocation);
			}
		}

		final List<SpriteLocation> sprites = new ArrayList<SpriteLocation>();

		for (int i=0;i<spriteIds.size();i++)
		{
			final byte spriteId = spriteIds.get(i);
			final byte spriteLocation = spriteLocations.get(i);
			final byte spriteMessage = i < messageIds.size() ? messageIds.get(0) : -1;

			sprites.add(new SpriteLocation(spriteId, spriteLocation, spriteMessage));
		}

		return sprites;
	}

	public static List<Level> readLevelData(final byte[] data, final List<TileInformation> tileInformation, final List<Palette> palettes)
	{
		final List<Level> screens = new ArrayList<Level>();

		screens.addAll(readScreenData(data, 0, 0, tileInformation, palettes));
		screens.addAll(readScreenData(data, 1, screens.size(), tileInformation, palettes));
		screens.addAll(readScreenData(data, 2, screens.size(), tileInformation, palettes));

		return screens;
	}

}
