package squeek.veganoption.content.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;
import squeek.veganoption.helpers.RandomHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class CraftingModifier
{
	public HashMap<ItemStack, ItemStack[]> inputsToRemoveForOutput = new HashMap<ItemStack, ItemStack[]>();
	public HashMap<ItemStack, ItemStack[]> inputsToKeepForOutput = new HashMap<ItemStack, ItemStack[]>();

	public CraftingModifier()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void addInputsToRemoveForOutput(ItemStack output, ItemStack... inputs)
	{
		inputsToRemoveForOutput.put(output, inputs);
	}

	public void addInputsToRemoveForOutput(ItemStack output, String... inputOreDicts)
	{
		for (String inputOreDict : inputOreDicts)
		{
			List<ItemStack> oreStacks = OreDictionary.getOres(inputOreDict);
			if (oreStacks.size() > 0)
				addInputsToRemoveForOutput(output.copy(), oreStacks.toArray(new ItemStack[oreStacks.size()]));
		}
	}

	public void addInputsToKeepForOutput(ItemStack output, ItemStack... inputs)
	{
		inputsToKeepForOutput.put(output, inputs);
	}

	public void addInputsToKeepForOutput(ItemStack output, String... inputOreDicts)
	{
		for (String inputOreDict : inputOreDicts)
		{
			List<ItemStack> oreStacks = OreDictionary.getOres(inputOreDict);
			if (oreStacks.size() > 0)
				addInputsToKeepForOutput(output.copy(), oreStacks.toArray(new ItemStack[oreStacks.size()]));
		}
	}

	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
	{
		List<ItemStack> inputsToRemove = getInputsToRemoveForOutput(event.crafting);
		List<ItemStack> inputsToKeep = getInputsToKeepForOutput(event.crafting);

		if (inputsToRemove.isEmpty() && inputsToKeep.isEmpty())
			return;

		for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = event.craftMatrix.getStackInSlot(i);
			if (!stackInSlot.isEmpty())
			{
				for (ItemStack inputToRemove : inputsToRemove)
				{
					if (OreDictionary.itemMatches(inputToRemove, stackInSlot, false))
					{
						stackInSlot.shrink(inputToRemove.getCount());
						if (stackInSlot.getCount() <= 0)
							event.craftMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
						break;
					}
				}
				for (ItemStack inputToKeep : inputsToKeep)
				{
					if (OreDictionary.itemMatches(inputToKeep, stackInSlot, false))
					{
						stackInSlot.grow(inputToKeep.getCount());
						if (stackInSlot.isItemStackDamageable() && stackInSlot.attemptDamageItem(inputToKeep.getCount(), RandomHelper.random))
						{
							stackInSlot.shrink(1);
						}
						break;
					}
				}
			}
		}
	}

	public List<ItemStack> getInputsToRemoveForOutput(ItemStack output)
	{
		List<ItemStack> inputsToRemove = new ArrayList<ItemStack>();
		for (Entry<ItemStack, ItemStack[]> entry : inputsToRemoveForOutput.entrySet())
		{
			if (OreDictionary.itemMatches(entry.getKey(), output, false))
			{
				inputsToRemove.addAll(Arrays.asList(entry.getValue()));
			}
		}
		return inputsToRemove;
	}

	public List<ItemStack> getInputsToKeepForOutput(ItemStack output)
	{
		List<ItemStack> inputsToKeep = new ArrayList<ItemStack>();
		for (Entry<ItemStack, ItemStack[]> entry : inputsToKeepForOutput.entrySet())
		{
			if (OreDictionary.itemMatches(entry.getKey(), output, false))
			{
				inputsToKeep.addAll(Arrays.asList(entry.getValue()));
			}
		}
		return inputsToKeep;
	}
}
