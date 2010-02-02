package tv.porst.daybreak.model;

public class SpriteLocation
{
	private final int x;
	private final int y;

	public SpriteLocation(final byte spriteId, final byte location, final byte message)
	{
		x = location & 0xF;
		y = (location >>> 4) & 0xF;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
