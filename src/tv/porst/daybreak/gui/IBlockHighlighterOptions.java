package tv.porst.daybreak.gui;

public interface IBlockHighlighterOptions
{
	boolean getHighlightAir();

	boolean getHighlightDoors();

	boolean getHighlightSolidBlocks();

	void setHighlightAir(boolean highlight);

	void setHighlightDoors(boolean highlight);

	void setHighlightSolidBlocks(boolean highlight);
}
