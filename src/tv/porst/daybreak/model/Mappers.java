package tv.porst.daybreak.model;

public class Mappers
{
	public static int[] HOUSE_TILE_INDICES = {6,6,6,7,7,7,7,7,8,8};

	public static int chunkToLevel(final int chunk)
	{
		if (chunk == 2)
		{
			return 1;
		}
		else if (chunk == 3)
		{
			return 2;
		}
		else if (chunk == 1)
		{
			return 3;
		}
		else if (chunk == 5)
		{
			return 4;
		}
		else if (chunk == 6)
		{
			return 5;
		}
		else if (chunk == 4)
		{
			return 6;
		}

		return chunk;
	}

	public static int levelToTiles(final int levelIndex)
	{
		switch(levelIndex)
		{
			case 3: return 2;
			case 4: return 1;
			case 1: return 3;
			case 7:
			case 5: return 4;
			case 2: return 5;
			default: return levelIndex;
		}
	}

	public static int lvlToChunk(final int lvlnr)
	{
		if (lvlnr == 1)
		{
			return 2;
		}
		else if (lvlnr == 2)
		{
			return 3;
		}
		else if (lvlnr == 3)
		{
			return 1;
		}
		else if (lvlnr == 4)
		{
			return 5;
		}
		else if (lvlnr == 5)
		{
			return 6;
		}
		else if (lvlnr == 6)
		{
			return 4;
		}

		return lvlnr;
	}
}
