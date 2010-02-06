package tv.porst.daybreak.model;


public final class MetaData
{
	private final byte[] properties;
	private final Block[] blocks;
	private final byte[] doorLocations;
	private final byte[] scrollData;
	private final byte[] doorDestinations;
	private final byte[] additionalScrollingData;
	private final byte[] additionalScrollingData2;

	public MetaData(final byte[] properties, final Block[] blocks, final byte[] scrollData, final byte[] doorLocations, final byte[] doorDestinations, final byte[] additionalScrollingData, final byte[] additionalScrollingData2)
	{
		this.properties = properties.clone();
		this.blocks = blocks.clone();
		this.doorLocations = doorLocations.clone();
		this.doorDestinations = doorDestinations.clone();
		this.scrollData = scrollData;
		this.additionalScrollingData = additionalScrollingData;
		this.additionalScrollingData2 = additionalScrollingData2;
	}

	public byte[] getAdditionalScrollingData()
	{
		return additionalScrollingData;
	}

	public byte[] getAdditionalScrollingData2()
	{
		return additionalScrollingData2;
	}

	public Block[] getBlocks()
	{
		return blocks;
	}

	public byte[] getDoorDestinations()
	{
		return doorDestinations;
	}

	public byte[] getDoorLocations()
	{
		return doorLocations;
	}

	public byte[] getScrollData()
	{
		return scrollData;
	}
}
