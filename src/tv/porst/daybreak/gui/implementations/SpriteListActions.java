package tv.porst.daybreak.gui.implementations;

import java.util.List;

import javax.swing.JDialog;

import net.sourceforge.jnhf.helpers.Pair;
import tv.porst.daybreak.gui.ScreenSelectionDialog;
import tv.porst.daybreak.model.EditedScreenModel;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteSearcher;

public class SpriteListActions
{
	public static void show(final FaxanaduRom rom, final Sprite sprite, final EditedScreenModel model)
	{
		final List<Pair<Level, Screen>> screens = SpriteSearcher.getScreensWithSprite(rom, sprite);

		final JDialog dialog = new ScreenSelectionDialog(screens, model);

		dialog.setVisible(true);
	}
}
