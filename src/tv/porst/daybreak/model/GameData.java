package tv.porst.daybreak.model;

public class GameData
{
	private final byte[] nlvlTable;

	public GameData(final byte[] nlvlTable)
	{
		this.nlvlTable = nlvlTable;
	}

	public byte[] getNewLevelMap()
	{
		return nlvlTable;
	}
}
