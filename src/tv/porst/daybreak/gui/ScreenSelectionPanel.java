package tv.porst.daybreak.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.jnhf.helpers.ListenerProvider;
import net.sourceforge.jnhf.helpers.Pair;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class ScreenSelectionPanel extends JPanel
{
	private final LevelBox levelBox;

	private final List<Level> levels;

	private final ScreenSelectionList screenSelectionList;

	private final ListenerProvider<IScreenSelectionListener> listeners = new ListenerProvider<IScreenSelectionListener>();

	public ScreenSelectionPanel(final List<Level> levels)
	{
		super(new BorderLayout());

		this.levels = new ArrayList<Level>(levels);

		setBorder(new TitledBorder("Level"));

		levelBox = new LevelBox(levels);

		levelBox.addItemListener(new InternalItemListener());

		add(levelBox, BorderLayout.NORTH);

		final JPanel innerPanel = new JPanel(new BorderLayout());

		screenSelectionList = new ScreenSelectionList(merge(levels.get(0)));
		screenSelectionList.addListSelectionListener(new InternalListSelectionListener());

		innerPanel.add(new JScrollPane(screenSelectionList));

		innerPanel.setPreferredSize(new Dimension(20 + 16 * 8, 13 * 8 * 5));

		add(innerPanel);
	}

	private List<Pair<Level, Screen>> merge(final Level level)
	{
		final List<Pair<Level, Screen>> screens = new ArrayList<Pair<Level,Screen>>();

		for (final Screen screen : level.getScreens())
		{
			screens.add(new Pair<Level, Screen>(level, screen));
		}

		return screens;
	}

	private void notifyListeners()
	{
		final Pair<Level, Screen> screen = screenSelectionList.getScreens().get(screenSelectionList.getSelectedIndex());

		for (final IScreenSelectionListener listener : listeners)
		{
			try
			{
				listener.selectedScreen(screen.first(), screen.second());
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}

	public void addListener(final IScreenSelectionListener listener)
	{
		listeners.addListener(listener);
	}

	public void removeListener(final IScreenSelectionListener listener)
	{
		listeners.removeListener(listener);
	}

	public void setScreen(final Level level, final Screen screen)
	{
		levelBox.setLevel(level);
		screenSelectionList.setSelectedScreen(screen);
	}

	private class InternalItemListener implements ItemListener
	{
		@Override
		public void itemStateChanged(final ItemEvent e)
		{
			final Level level = ((LevelItem) levelBox.getSelectedItem()).getLevel();

			screenSelectionList.setScreens(merge(level));

			if (screenSelectionList.getSelectedIndex() == 0)
			{
				notifyListeners();
			}
			else
			{
				screenSelectionList.setSelectedIndex(0);
			}
		}
	}

	private class InternalListSelectionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(final ListSelectionEvent e)
		{
			if (e.getValueIsAdjusting())
			{
				return;
			}

			notifyListeners();
		}
	}
}
