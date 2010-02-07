package tv.porst.daybreak.model;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.helpers.Pair;

public class SpriteSearcher
{
	private static boolean hasSprite(final Screen screen, final Sprite sprite)
	{
		for (final SpriteLocation spriteData : screen.getSpriteData())
		{
			if (spriteData.getSprite() == sprite)
			{
				return true;
			}
		}

		return false;
	}

	public static List<Pair<Level, Screen>> getScreensWithSprite(final FaxanaduRom rom, final Sprite sprite)
	{
		final List<Pair<Level, Screen>> screens = new ArrayList<Pair<Level, Screen>>();

		final List<Level> levels = rom.getLevels();

		for (int i=0;i<levels.size();i++)
		{
			final Level level = levels.get(Mappers.chunkToLevel(i));

			for (final Screen screen : level.getScreens())
			{
				if (hasSprite(screen, sprite))
				{
					screens.add(new Pair<Level, Screen>(level, screen));
					continue;
				}
			}
		}

		return screens;
	}
}
