package tv.porst.daybreak.model;


public final class Block
{
	private int t1;
	private int t2;
	private int t3;
	private int t4;
	private final BlockAttribute attribute;
	private final int index;
	private final BlockProperty property;

	public Block(final int index, final BlockProperty property, final BlockAttribute attribute, final int t1, final int t2, final int t3, final int t4)
	{
		this.index = index;
		this.attribute = attribute;
		this.property = property;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
	}

	public BlockAttribute getAttribute()
	{
		return attribute;
	}

	public int getIndex()
	{
		return index;
	}

	public BlockProperty getProperty()
	{
		return property;
	}

	public int getTile1()
	{
		return t1;
	}

	public int getTile2()
	{
		return t2;
	}

	public int getTile3()
	{
		return t3;
	}

	public int getTile4()
	{
		return t4;
	}

	public void setTile1(final int id)
	{
		t1 = id;
	}

	public void setTile2(final int id)
	{
		t2 = id;
	}

	public void setTile3(final int id)
	{
		t3 = id;
	}

	public void setTile4(final int id)
	{
		t4 = id;
	}

	@Override
	public String toString()
	{
		return String.format("%02X %02X\n%02X %02X", t1, t2, t3, t4);
	}
}
