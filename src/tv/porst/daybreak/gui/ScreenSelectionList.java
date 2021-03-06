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

import net.sourceforge.jnhf.gui.IPaletteListener;
import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ImageHelpers;
import net.sourceforge.jnhf.helpers.Pair;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.IScreenListener;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.SpriteLocation;

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

	private final ScreenPanelOptions options = new ScreenPanelOptions();

	private final IPaletteListener internalPaletteListener = new InternalPaletteListener();

	public ScreenSelectionList(final List<Pair<Level, Screen>> screens)
	{
		super(new ScreenSelectionListModel(extractScreens(screens)));

		this.screens = screens;

		setCellRenderer(new ScreenRenderer());

		for (final Screen screen : extractScreens(screens))
		{
			screen.addListener(internalScreenListener);
			screen.getPalette().addListener(internalPaletteListener );
		}
	}

	private ImageIcon getImage(final int index)
	{
		final Screen screen = getModel().getScreens().get(index);

		if (images.containsKey(screen))
		{
			return images.get(screen);
		}

		final ScreenBitmap bitmap = new ScreenBitmap(screen, options , screens.get(index).first().getMetaData().getBlocks(), -1, -1, null);

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
		for (final Screen screen : extractScreens(screens))
		{
			screen.removeListener(internalScreenListener);
			screen.getPalette().removeListener(internalPaletteListener);
		}

		this.screens.clear();
		this.screens.addAll(merge);

		getModel().setScreens(extractScreens(merge));

		for (final Screen screen : extractScreens(screens))
		{
			screen.addListener(internalScreenListener);
			screen.getPalette().addListener(internalPaletteListener);
		}
	}

	public void setSelectedScreen(final Screen screen)
	{
		setSelectedIndex(extractScreens(screens).indexOf(screen));
	}

	private class InternalPaletteListener implements IPaletteListener
	{
		@Override
		public void paletteChanged(final Palette palette, final int index, final byte colorIndex)
		{
			for (final Screen screen : extractScreens(screens))
			{
				if (screen.getPalette() == palette)
				{
					images.remove(screen);
				}
			}

			repaint();
		}
	}

	private class InternalScreenListener implements IScreenListener
	{
		@Override
		public void addedSprite(final Screen screen, final SpriteLocation spriteLocation)
		{
			images.remove(screen);

			repaint();
		}

		@Override
		public void changedBlock(final Screen screen, final int x, final int y, final Block block)
		{
			images.remove(screen);

			repaint();
		}

		@Override
		public void removeSprite(final Screen screen, final SpriteLocation sprite)
		{
			images.remove(screen);

			repaint();
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
