package tv.porst.daybreak.model;

import net.sourceforge.jnhf.helpers.FilledList;
import net.sourceforge.jnhf.helpers.IFilledList;
import net.sourceforge.jnhf.romfile.TileData;

public class TileInformation
{
	private final int startLocation;
	private final IFilledList<TileData> tileData;

	public TileInformation(final int startLocation, final IFilledList<TileData> tileData)
	{
		this.startLocation = startLocation;
		this.tileData = new FilledList<TileData>(tileData);
	}

	public int getStartLocation()
	{
		return startLocation;
	}

	public IFilledList<TileData> getTileData()
	{
		return tileData;
	}
}
