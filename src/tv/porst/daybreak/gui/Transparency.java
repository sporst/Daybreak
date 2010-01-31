package tv.porst.daybreak.gui;

import java.awt.AlphaComposite;

public class Transparency
{
	public static AlphaComposite makeComposite(final float alpha)
	{
		return (AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}

}
