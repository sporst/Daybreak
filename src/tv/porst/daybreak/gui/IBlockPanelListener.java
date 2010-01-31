package tv.porst.daybreak.gui;

import tv.porst.daybreak.model.Block;

public interface IBlockPanelListener
{
	void clickedBlock(Block block);

	void hoveredBlock(Block block);
}
