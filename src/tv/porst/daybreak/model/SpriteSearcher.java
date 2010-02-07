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

		for (final Level level : rom.getLevels())
		{
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
