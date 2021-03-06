package squeek.veganoption.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFoodContainered extends ItemFood
{

	public ItemFoodContainered(int hunger, boolean isWolfsFavoriteFood)
	{
		super(hunger, isWolfsFavoriteFood);
	}

	public ItemFoodContainered(int hunger, float saturation, boolean isWolfsFavoriteFood)
	{
		super(hunger, saturation, isWolfsFavoriteFood);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entity)
	{
		ItemStack itemStackRemaining = super.onItemUseFinish(itemStack, world, entity);

		if (getContainerItem() != null)
		{
			ItemStack container = getContainerItem(itemStack);
			if (itemStackRemaining == null || itemStackRemaining.stackSize <= 0)
				return container;
			else if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).inventory.addItemStackToInventory(container))
				entity.entityDropItem(container, 0);
		}

		return itemStackRemaining;
	}

}
