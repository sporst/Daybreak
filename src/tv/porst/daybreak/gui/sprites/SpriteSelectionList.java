package tv.porst.daybreak.gui.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.TransferHandler;

import net.sourceforge.jnhf.helpers.ImageHelpers;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteSearcher;

public class SpriteSelectionList extends JList
{
	private final Map<Sprite, ImageIcon> images = new HashMap<Sprite, ImageIcon>();

	private final FaxanaduRom rom;

	private final SpriteDragProvider spriteDragProvider = new SpriteDragProvider();

	private final TransferHandler spriteTransferHandler = new SpriteTransferHandler(spriteDragProvider);

	public SpriteSelectionList(final FaxanaduRom rom)
	{
		super(new SpriteSelectionListModel(rom));

		this.rom = rom;

		setDragEnabled(true);
		setTransferHandler(spriteTransferHandler);

		setVisibleRowCount(7);
		setCellRenderer(new SpriteRenderer());
	}

	private ImageIcon getImage(final int index)
	{
		final Sprite sprite = rom.getSprites().get(index);

		if (images.containsKey(sprite))
		{
			return images.get(sprite);
		}

		final ImageIcon image = new ImageIcon(getNormalizedImage(sprite));

		images.put(sprite, image);

		return image;
	}

	private Image getNormalizedImage(final Sprite sprite)
	{
		if (sprite.height() == 0 && sprite.width() == 0)
		{
			return new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
		}

		final SpriteBitmap bitmap = new SpriteBitmap(sprite);

		final int count = SpriteSearcher.getScreensWithSprite(rom, sprite).size();

		if (sprite.height() >= sprite.width())
		{
			final int NEW_HEIGHT = 64;

			final int newWidth = NEW_HEIGHT / (sprite.height() * 8) * sprite.width() * 8;

			final BufferedImage resizedImage = ImageHelpers.resize(bitmap, newWidth, NEW_HEIGHT);

			final int normalizedSize = 104;

			final BufferedImage image = new BufferedImage(normalizedSize, normalizedSize, BufferedImage.TYPE_3BYTE_BGR);

			final int paddingLeft = (image.getWidth() - newWidth) / 2;
			final int paddingTop = (image.getHeight() - NEW_HEIGHT) / 2;

			final Graphics g = image.getGraphics();

			g.drawImage(resizedImage, paddingLeft, paddingTop, null);

			if (count == 0)
			{
				g.setColor(Color.RED);
				g.drawString("          Unused", 0, image.getHeight() - 6);
			}
			else
			{
				g.setColor(Color.WHITE);

				if (count == 1)
				{
					g.drawString("     Used 1 time", 0, image.getHeight() - 6);
				}
				else
				{
					g.drawString(String.format("     Used %d times", count), 0, image.getHeight() - 6);
				}
			}

			return image;
		}
		else
		{
			return new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
		}
	}

	@Override
	public SpriteSelectionListModel getModel()
	{
		return (SpriteSelectionListModel) super.getModel();
	}

	private class SpriteDragProvider implements IDragSpriteProvider
	{
		@Override
		public Sprite getSprite()
		{
			return rom.getSprites().get(getSelectedIndex());
		}
	}

	private class SpriteRenderer extends DefaultListCellRenderer
	{
		@Override
		public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus)
		{
			final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			final ImageIcon icon = getImage(index);

			if (isSelected)
			{
				final ImageIcon clonedIcon = new ImageIcon(ImageHelpers.copy((BufferedImage) icon.getImage()));

				final Graphics2D g = (Graphics2D) clonedIcon.getImage().getGraphics();

				g.setColor(Color.RED);
				g.setStroke(new BasicStroke(5));
				g.drawRect(0, 0, clonedIcon.getIconWidth() - 1, clonedIcon.getIconHeight() - 1);

				((JLabel) c).setIcon(clonedIcon);
			}
			else
			{
				((JLabel) c).setIcon(icon);
			}

			((JLabel) c).setText("");

			return c;
		}
	}
}
