package tv.porst.daybreak.model;

public class BlockProperty
{
	private final byte value;

	public BlockProperty(final byte value)
	{
		System.out.println(value);

		this.value = value;
	}

	public byte getValue()
	{
		return value;
	}

	public boolean isDoor()
	{
		return value == 3;
	}

	public boolean isSolid()
	{
		return value == 1;
	}
}
