package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.BitReader;
import net.sourceforge.jnhf.romfile.INesHeader;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Mappers;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;
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

	private static Screen readScreen(final byte[] data, final int levelDataPointer, final TileInformation tiles, final Palette palette)
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

		return new Screen(squareNumbers, tiles, palette);
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

				screens.add(readScreen(data, INesHeader.NES_HEADER_SIZE + bank * 0x4000 + pointer2, tiles, palette));
			}

			final MetaData metaData = MetaDataReader.read(data, levelId + levels.size(), screens.size(), screenPointers.length);

			levels.add(new Level(metaData, screens));
		}

		return levels;
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
