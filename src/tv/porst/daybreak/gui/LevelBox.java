package tv.porst.daybreak.gui;

import java.util.List;

import javax.swing.JComboBox;

import tv.porst.daybreak.model.Level;

public class LevelBox extends JComboBox
{
	public LevelBox(final List<Level> levels)
	{
		addItem(new LevelItem("Eolis", levels.get(0)));
		addItem(new LevelItem("Trunk", levels.get(3)));
		addItem(new LevelItem("Mist", levels.get(1)));
		addItem(new LevelItem("Branch", levels.get(4)));
		addItem(new LevelItem("The Evil Place", levels.get(5)));
		addItem(new LevelItem("Dartmoor", levels.get(7)));
		addItem(new LevelItem("Towns", levels.get(2)));
		addItem(new LevelItem("Buildings", levels.get(6)));
	}
}
