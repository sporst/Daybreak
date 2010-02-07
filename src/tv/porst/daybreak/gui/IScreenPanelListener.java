package tv.porst.daybreak.gui;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public interface IScreenPanelListener
{
	void changedScreen(ScreenPanel screenPanel, Level level, Screen screen);

	void hoveredBlock(Block block);
}
