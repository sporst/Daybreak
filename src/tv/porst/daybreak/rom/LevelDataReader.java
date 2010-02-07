package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.BitReader;
import net.sourceforge.jnhf.helpers.ByteHelpers;
import net.sourceforge.jnhf.helpers.Pair;
import net.sourceforge.jnhf.romfile.INesHeader;
import tv.porst.daybreak.helpers.PointerReader;
import tv.porst.daybreak.model.GameData;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Mappers;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteLocation;
import tv.porst.daybreak.model.TileInformation;

public final class LevelDataReader
{
	public static final Palette DEFAULT_PALETTE = new Palette(new byte[16]);

	private static void assignPalette(final List<Level> levels, final int levelId, final int screenId, final List<Palette> palettes, final GameData gameData)
	{
		final Level level = levels.get(levelId);

		final byte[] doorLocations = level.getMetaData().getDoorLocations();
		final byte[] doorDestinations = level.getMetaData().getDoorDestinations();

		for (int i=0;i<doorLocations.length;i+=4)
		{
			if (screenId == doorLocations[i])
			{
				final Pair<Integer, Integer> result = processDoor(levels, levelId, doorLocations, i / 4, doorDestinations, palettes, gameData);

				if (result != null)
				{
					assignPalette(levels, result.first(), result.second(), palettes, gameData);
				}
			}
		}

		final byte[] scrollData = level.getMetaData().getScrollData();

		final Integer nextScreen1 = assignScreenPalette(level, screenId, scrollData, 0);

		if (nextScreen1 != null)
		{
			assignPalette(levels, levelId, nextScreen1, palettes, gameData);
		}

		final Integer nextScreen2 = assignScreenPalette(level, screenId, scrollData, 1);

		if (nextScreen2 != null)
		{
			assignPalette(levels, levelId, nextScreen2, palettes, gameData);
		}

		final Integer nextScreen3 = assignScreenPalette(level, screenId, scrollData, 2);

		if (nextScreen3 != null)
		{
			assignPalette(levels, levelId, nextScreen3, palettes, gameData);
		}

		final Integer nextScreen4 = assignScreenPalette(level, screenId, scrollData, 3);

		if (nextScreen4 != null)
		{
			assignPalette(levels, levelId, nextScreen4, palettes, gameData);
		}

		final byte[] additionalScrollingData2 = level.getMetaData().getAdditionalScrollingData2();

		for (int i=0;i<additionalScrollingData2.length;i+=4)
		{
			if (additionalScrollingData2[i] == screenId)
			{
				final byte roomId = additionalScrollingData2[i + 1];
				final byte paletteId = additionalScrollingData2[i + 3];

				final Screen room = level.getScreens().get(roomId);

				if (room.getPalette() == null)
				{
					room.setPalette(palettes.get(paletteId));
					assignPalette(levels, levelId, roomId, palettes, gameData);
				}
			}
		}

		final byte[] additionalScrollingData = level.getMetaData().getAdditionalScrollingData();

		for (int i=0;i<additionalScrollingData.length;i+=5)
		{
			if (additionalScrollingData[i] == screenId)
			{
				final int bankId = Mappers.chunkToLevel(additionalScrollingData[i + 1]);
				final int roomId = additionalScrollingData[i + 2];
				final int paletteId = additionalScrollingData[i + 4];

				final Screen room = levels.get(bankId).getScreens().get(roomId);

				if (room.getPalette() == null)
				{
					room.setPalette(palettes.get(paletteId));
					assignPalette(levels, bankId, roomId, palettes, gameData);
				}
			}
		}
	}

	private static void assignPalettes(final List<Level> levels, final List<Palette> palettes, final GameData gameData)
	{
		levels.get(0).getScreens().get(0).setPalette(palettes.get(0));

		assignPalette(levels, 0, 0, palettes, gameData);

		for (final Level level : levels)
		{
			for (final Screen screen : level.getScreens())
			{
				if (screen.getPalette() == null)
				{
					screen.setPalette(DEFAULT_PALETTE );
				}
			}
		}
	}

	private static Integer assignScreenPalette(final Level level, final int screenId, final byte[] scrollData, final int index)
	{
		if (scrollData[4 * screenId + index] != -1)
		{
			final int targetScreenIndex = scrollData[4 * screenId + index];

			final Screen targetScreen = level.getScreens().get(targetScreenIndex);

			if (targetScreen.getPalette() == null)
			{
				final Screen sourceScreen = level.getScreens().get(screenId);
				targetScreen.setPalette(sourceScreen.getPalette());

				return targetScreenIndex;
			}
		}

		return null;
	}

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

	private static Pair<Integer, Integer> processDoor(final List<Level> levels, final int levelId, final byte[] doorLocations, final int doorIndex, final byte[] doorDestinations, final List<Palette> palettes, final GameData gameData)
	{
//		System.out.println("Processing: " + levelId + " - " + doorIndex);

		int destination = doorLocations[4 * doorIndex + 2] & 0xFF;

		if (destination < 0x20)
		{
			final int destinationPointer = 4 * destination;

			final Palette palette = palettes.get(doorDestinations[destinationPointer + 1]);

			final Level level = levels.get(levelId);

			final int destinationScreen = doorDestinations[destinationPointer];

			final Screen screen = level.getScreens().get(destinationScreen);

			if (screen.getPalette() == null)
			{
				screen.setPalette(palette);

				return new Pair<Integer, Integer>(levelId, destinationScreen);
			}
			else
			{
				return null;
			}
		}
		else if (destination < 0xFE)
		{
			if (levelId == 2)
			{
				destination -= 0x20;
			}

			final Level level = levels.get(6);


			final int destinationPointer = 4 * destination;
			final Screen screen = level.getScreens().get(doorDestinations[destinationPointer + 1]);

			screen.setPalette(palettes.get(new int[] { 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A }[doorDestinations[destinationPointer + 1]]));

			return null;
		}
		else if (destination == 0xFE)
		{
			return null;
		}
		else if (destination == 0xFF)
		{
			final byte[] lvlMap = gameData.getNewLevelMap();

			int newLevel = Mappers.chunkToLevel(lvlMap[2 * Mappers.lvlToChunk(levelId) + 1]);

			if (newLevel == 2)
			{
				newLevel = 4;
			}
			else if (newLevel == 4)
			{
				newLevel = 5;
			}
			else if (newLevel == 0)
			{
				newLevel = 7;
			}

			final byte[] nscrTable = {
				    (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x11, (byte)0x28, (byte)0x00, (byte)0x1F, (byte)0x00, (byte)0x27, (byte)0x08,
				    (byte)0x0E, (byte)0x0E, (byte)0x0D
			};

			final int newScreen = nscrTable[2 * levelId + 1];

			final Screen screen = levels.get(newLevel).getScreens().get(newScreen);

			if (screen.getPalette() == null)
			{
				final byte[] palettesTable = {
					    (byte)0x00, (byte)0x06, (byte)0x0A, (byte)0x1B, (byte)0x1B, (byte)0x08, (byte)0x0C, (byte)0x0F, (byte)0x00, (byte)0x00,
					    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
				};

				screen.setPalette(palettes.get(palettesTable[Mappers.lvlToChunk(newLevel)]));

				return new Pair<Integer, Integer>(newLevel, newScreen);
			}
			else
			{
				return null;
			}
		}

		return null;
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

	private static List<Level> readScreenData(final byte[] data, final int bank, final int levelId, final List<TileInformation> tileInformation, final List<Sprite> sprites)
	{
		final List<Level> levels = new ArrayList<Level>();

		PointerReader.readTable(data, 0, INesHeader.NES_HEADER_SIZE + bank * 0x4000);

		for (final int pointer : PointerReader.readTable(data, 0, INesHeader.NES_HEADER_SIZE + bank * 0x4000))
		{
			final List<Screen> screens = new ArrayList<Screen>();

			final int[] screenPointers = PointerReader.readTable(data, pointer, INesHeader.NES_HEADER_SIZE + bank * 0x4000);

			for (final int pointer2 : screenPointers)
			{
				final TileInformation tiles = getTileInformation(levelId + levels.size(), screens.size(), tileInformation);

				final List<SpriteLocation> spriteData = readSpriteData(data, levelId + levels.size(), screens.size(), sprites);

				final int[][] squareNumbers = readScreen(data, INesHeader.NES_HEADER_SIZE + bank * 0x4000 + pointer2);

				final Screen screen = new Screen(squareNumbers, spriteData, tiles);

				screens.add(screen);
			}

			final MetaData metaData = MetaDataReader.read(data, levelId + levels.size(), screens.size(), screenPointers.length);

			levels.add(new Level(metaData, screens));
		}

		return levels;
	}

	private static List<SpriteLocation> readSpriteData(final byte[] data, final int levelId, final int screenId, final List<Sprite> spritess)
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
			final int spriteId = spriteIds.get(i) & 0xFF;

			final Sprite sprite = spriteId < spritess.size() ? spritess.get(spriteId) : null;
			final byte spriteLocation = spriteLocations.get(i);
			final byte spriteMessage = i < messageIds.size() ? messageIds.get(0) : -1;

			sprites.add(new SpriteLocation(sprite, spriteLocation, spriteMessage));
		}

		return sprites;
	}

	public static List<Level> readLevelData(final byte[] data, final List<TileInformation> tileInformation, final List<Palette> palettes, final GameData gameData, final List<Sprite> sprites)
	{
		final List<Level> screens = new ArrayList<Level>();

		screens.addAll(readScreenData(data, 0, 0, tileInformation, sprites));
		screens.addAll(readScreenData(data, 1, screens.size(), tileInformation, sprites));
		screens.addAll(readScreenData(data, 2, screens.size(), tileInformation, sprites));

		assignPalettes(screens, palettes, gameData);

		return screens;
	}

}
