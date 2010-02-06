package tv.porst.daybreak.gui;

import tv.porst.daybreak.model.Level;

public class LevelItem
{
	private final String name;
	private final Level level;

	public LevelItem(final String name, final Level level)
	{
		this.name = name;
		this.level = level;
	}

	public Level getLevel()
	{
		return level;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
