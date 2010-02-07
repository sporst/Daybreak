package tv.porst.daybreak.gui.blocks;

import java.awt.event.MouseEvent;

import tv.porst.daybreak.model.Block;

public interface IBlockPanelListener
{
	void clickedBlock(MouseEvent event, Block block);

	void hoveredBlock(Block block);
}
