package tv.porst.daybreak.model;

public interface IScreenListener
{
	void addedSprite(Screen screen, SpriteLocation spriteLocation);

	void changedBlock(Screen screen, int x, int y, Block block);

	void removeSprite(Screen screen, SpriteLocation sprite);
}
