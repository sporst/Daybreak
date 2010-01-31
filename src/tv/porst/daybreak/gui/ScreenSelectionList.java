package tv.porst.daybreak.gui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import net.sourceforge.jnhf.helpers.ImageHelpers;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.IScreenListener;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class ScreenSelectionList extends JList
{
	private final Map<Screen, ImageIcon> images = new HashMap<Screen, ImageIcon>();

	private final IScreenListener internalScreenListener = new InternalScreenListener();

	public ScreenSelectionList(final Level level)
	{
		super(new ScreenSelectionListModel(level));

		setCellRenderer(new ScreenRenderer());

		for (final Screen screen : level.getScreens())
		{
			screen.addListener(internalScreenListener);
		}

	}

	private ImageIcon getImage(final int index)
	{
		final Screen screen = getModel().getLevel().getScreens().get(index);

		if (images.containsKey(screen))
		{
			return images.get(screen);
		}

		final ScreenBitmap bitmap = new ScreenBitmap(screen, getModel().getLevel().getMetaData(), -1, -1, null);

		final ImageIcon image = new ImageIcon(ImageHelpers.resize(bitmap, 16 * 8, 13 * 8));

		images.put(screen, image);

		return image;
	}

	@Override
	public ScreenSelectionListModel getModel()
	{
		return (ScreenSelectionListModel) super.getModel();
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
