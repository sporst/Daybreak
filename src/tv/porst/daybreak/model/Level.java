package tv.porst.daybreak.model;

import java.util.ArrayList;
import java.util.List;


public final class Level
{
	private final ArrayList<Screen> screens;
	private final MetaData metaData;

	public Level(final MetaData metaData, final List<Screen> screens)
	{
		this.metaData = metaData;
		this.screens = new ArrayList<Screen>(screens);
	}

	public MetaData getMetaData()
	{
		return metaData;
	}

	public List<Screen> getScreens()
	{
		return new ArrayList<Screen>(screens);
	}
}
