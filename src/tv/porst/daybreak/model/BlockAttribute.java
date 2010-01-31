package tv.porst.daybreak.model;


public class BlockAttribute
{
	private final int topLeft;
	private final int topRight;
	private final int bottomLeft;
	private final int bottomRight;

	public BlockAttribute(final byte attribute)
	{
		topLeft = (attribute >>> 6) & 3;
		topRight = (attribute >>> 4) & 3;
		bottomLeft = (attribute >>> 2) & 3;
		bottomRight = (attribute >>> 0) & 3;
	}

	public int bottomLeft()
	{
		return bottomLeft;
	}

	public int bottomRight()
	{
		return bottomRight;
	}

	public int topLeft()
	{
		return topLeft;
	}

	public int topRight()
	{
		return topRight;
	}
}
