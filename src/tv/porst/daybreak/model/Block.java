package tv.porst.daybreak.model;

public final class Block
{
	private final int t1;
	private final int t2;
	private final int t3;
	private final int t4;
	private final BlockAttribute attribute;

	public Block(final BlockAttribute attribute, final int t1, final int t2, final int t3, final int t4)
	{
		this.attribute = attribute;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
	}

	public BlockAttribute getAttribute()
	{
		return attribute;
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

	@Override
	public String toString()
	{
		return String.format("%02X %02X\n%02X %02X", t1, t2, t3, t4);
	}
}
