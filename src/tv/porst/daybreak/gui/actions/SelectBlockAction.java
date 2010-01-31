package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tv.porst.daybreak.gui.ScreenPanel;
import tv.porst.daybreak.model.Block;

public class SelectBlockAction extends AbstractAction
{
	private final Block block;
	private final ScreenPanel panel;

	public SelectBlockAction(final ScreenPanel panel, final Block block)
	{
		super("Select Block");

		this.panel = panel;
		this.block = block;
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		panel.setSelectedBlock(block);
	}
}
