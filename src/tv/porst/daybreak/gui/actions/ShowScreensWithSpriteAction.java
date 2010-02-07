package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tv.porst.daybreak.gui.implementations.SpriteListActions;
import tv.porst.daybreak.model.EditedScreenModel;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.Sprite;

public class ShowScreensWithSpriteAction extends AbstractAction
{
	private final Sprite sprite;
	private final FaxanaduRom rom;
	private final EditedScreenModel model;

	public ShowScreensWithSpriteAction(final FaxanaduRom rom, final Sprite sprite, final EditedScreenModel model)
	{
		super("Show screens");

		this.rom = rom;
		this.sprite = sprite;
		this.model = model;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		SpriteListActions.show(rom, sprite, model);
	}
}
