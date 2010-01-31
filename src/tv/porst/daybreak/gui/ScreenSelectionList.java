package tv.porst.daybreak.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import net.sourceforge.jnhf.helpers.ImageHelpers;
import tv.porst.daybreak.model.Level;

public class ScreenSelectionList extends JList
{
	public ScreenSelectionList(final Level level)
	{
		super(new ScreenSelectionListModel(level));

		setCellRenderer(new ScreenRenderer());
	}

	private ImageIcon getImage(final int index)
	{
		final ScreenBitmap bitmap = new ScreenBitmap(getModel().getLevel().getScreens().get(index), getModel().getLevel().getMetaData());

		return new ImageIcon(ImageHelpers.resize(bitmap, 16 * 8, 13 * 8));
	}

	@Override
	public ScreenSelectionListModel getModel()
	{
		return (ScreenSelectionListModel) super.getModel();
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
