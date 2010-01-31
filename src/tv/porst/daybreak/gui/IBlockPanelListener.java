package tv.porst.daybreak.gui;

import java.awt.event.MouseEvent;

import tv.porst.daybreak.model.Block;

public interface IBlockPanelListener
{
	void clickedBlock(MouseEvent event, Block block);

	void hoveredBlock(Block block);
}
