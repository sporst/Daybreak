package tv.porst.daybreak.gui;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.jnhf.helpers.Pair;
import tv.porst.daybreak.model.EditedScreenModel;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class ScreenSelectionDialog extends JDialog
{
	private final EditedScreenModel model;

	private final ListSelectionListener internalSelectionListener = new InternalListSelectionListener();

	private final ScreenSelectionList screenSelectionList;

	private final List<Pair<Level, Screen>> screens;

	public ScreenSelectionDialog(final List<Pair<Level, Screen>> screens, final EditedScreenModel model)
	{
		this.screens = screens;
		this.model = model;

		screenSelectionList = new ScreenSelectionList(screens);
		screenSelectionList.addListSelectionListener(internalSelectionListener);

		add(new JScrollPane(screenSelectionList));

		setSize(screenSelectionList.getPreferredSize().width + 30, 384);
	}

	private class InternalListSelectionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(final ListSelectionEvent event)
		{
			if (event.getValueIsAdjusting())
			{
				return;
			}

			final Pair<Level, Screen> screen = screens.get(screenSelectionList.getSelectedIndex());

			model.setSelectedScreen(screen.first(), screen.second());
		}
	}
}
