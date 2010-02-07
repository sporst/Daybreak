package tv.porst.daybreak.model;

public class SpriteLocation
{
	private final int x;
	private final int y;
	private final Sprite sprite;

	public SpriteLocation(final Sprite sprite, final byte location, final byte message)
	{
		this.sprite = sprite;
		x = location & 0xF;
		y = (location >>> 4) & 0xF;
	}

	public SpriteLocation(final Sprite sprite, final int col, final int row, final byte message)
	{
		this.sprite = sprite;
		x = col;
		y = row;
	}

	public Sprite getSprite()
	{
		return sprite;
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
