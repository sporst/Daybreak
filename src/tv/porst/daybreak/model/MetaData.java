package tv.porst.daybreak.model;


public final class MetaData
{
	private final byte[] properties;
	private final Block[] blocks;

	public MetaData(final byte[] properties, final Block[] blocks)
	{
		this.properties = properties.clone();
		this.blocks = blocks.clone();
	}

	public Block[] getBlocks()
	{
		return blocks;
	}
}
