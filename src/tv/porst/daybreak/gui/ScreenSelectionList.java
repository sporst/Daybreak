package tv.porst.daybreak.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import net.sourceforge.jnhf.helpers.ImageHelpers;
import net.sourceforge.jnhf.helpers.Pair;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.IScreenListener;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class ScreenSelectionList extends JList
{
	private static List<Screen> extractScreens(final List<Pair<Level, Screen>> screens)
	{
		final List<Screen> out = new ArrayList<Screen>();

		for (final Pair<Level,Screen> pair : screens)
		{
			out.add(pair.second());
		}

		return out;
	}

	private final Map<Screen, ImageIcon> images = new HashMap<Screen, ImageIcon>();

	private final IScreenListener internalScreenListener = new InternalScreenListener();

	private final List<Pair<Level, Screen>> screens;

	public ScreenSelectionList(final List<Pair<Level, Screen>> screens)
	{
		super(new ScreenSelectionListModel(extractScreens(screens)));

		this.screens = screens;

		setCellRenderer(new ScreenRenderer());

		for (final Screen screen : extractScreens(screens))
		{
			screen.addListener(internalScreenListener);
		}
	}

	private ImageIcon getImage(final int index)
	{
		final Screen screen = getModel().getScreens().get(index);

		if (images.containsKey(screen))
		{
			return images.get(screen);
		}

		final ScreenBitmap bitmap = new ScreenBitmap(screen, screens.get(index).first().getMetaData().getBlocks(), -1, -1, null);

		final ImageIcon image = new ImageIcon(ImageHelpers.resize(bitmap, 16 * 8, 13 * 8));

		images.put(screen, image);

		return image;
	}

	@Override
	public ScreenSelectionListModel getModel()
	{
		return (ScreenSelectionListModel) super.getModel();
	}

	public List<Pair<Level, Screen>> getScreens()
	{
		return new ArrayList<Pair<Level,Screen>>(screens);
	}

	public void setScreens(final List<Pair<Level, Screen>> merge)
	{
		this.screens.clear();
		this.screens.addAll(merge);

		getModel().setScreens(extractScreens(merge));
	}

	public void setSelectedScreen(final Screen screen)
	{
		setSelectedIndex(extractScreens(screens).indexOf(screen));
	}

	private class InternalScreenListener implements IScreenListener
	{
		@Override
		public void changedBlock(final Screen screen, final int x, final int y, final Block block)
		{
			images.remove(screen);
		}
	}

	private class ScreenRenderer extends DefaultListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus)
		{
			final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			((JLabel) c).setIcon(getImage(index));
			((JLabel) c).setText("");

			return c;
		}
	}
}
