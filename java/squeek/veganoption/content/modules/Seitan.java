package squeek.veganoption.content.modules;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import squeek.veganoption.ModInfo;
import squeek.veganoption.VeganOption;
import squeek.veganoption.content.ContentHelper;
import squeek.veganoption.content.IContentModule;
import squeek.veganoption.content.Modifiers;
import squeek.veganoption.content.recipes.PistonCraftingRecipe;
import squeek.veganoption.content.registry.PistonCraftingRegistry;
import squeek.veganoption.items.ItemWashableWheat;
import cpw.mods.fml.common.registry.GameRegistry;

// TODO: Tooltips, usage, and recipe text
public class Seitan implements IContentModule
{
	public static Item washableWheat;
	public static Item seitanCooked;
	public static ItemStack seitanRawStack;
	public static ItemStack seitanUnwashedStack;
	public static ItemStack wheatFlourStack;
	public static ItemStack wheatDoughStack;

	public static final ItemStack wheatCrusher = new ItemStack(Blocks.piston);

	@Override
	public void create()
	{
		washableWheat = new ItemWashableWheat()
				.setUnlocalizedName(ModInfo.MODID)
				.setCreativeTab(VeganOption.creativeTab);
		GameRegistry.registerItem(washableWheat, "washableWheat");
		wheatFlourStack = new ItemStack(washableWheat, 1, ItemWashableWheat.META_FLOUR);
		wheatDoughStack = new ItemStack(washableWheat, 1, ItemWashableWheat.META_DOUGH);
		seitanUnwashedStack = new ItemStack(washableWheat, 1, ItemWashableWheat.META_UNWASHED_START);
		seitanRawStack = new ItemStack(washableWheat, 1, ItemWashableWheat.META_RAW);

		seitanCooked = new ItemFood(8, 0.8f, false)
				.setUnlocalizedName(ModInfo.MODID + ".seitanCooked")
				.setCreativeTab(VeganOption.creativeTab)
				.setTextureName(ModInfo.MODID_LOWER + ":seitan_cooked");
		GameRegistry.registerItem(seitanCooked, "seitanCooked");
	}

	@Override
	public void oredict()
	{
		OreDictionary.registerOre(ContentHelper.wheatFlourOreDict, wheatFlourStack.copy());
		OreDictionary.registerOre(ContentHelper.wheatDoughOreDict, wheatDoughStack.copy());
		OreDictionary.registerOre(ContentHelper.rawSeitanOreDict, seitanRawStack.copy());

		// cooked seitan works as a raw/cooked meat substitute, a la HarvestCraft tofu
		for (String oreDict : ContentHelper.harvestCraftRawMeatOreDicts)
			OreDictionary.registerOre(oreDict, new ItemStack(seitanCooked));
		for (String oreDict : ContentHelper.harvestCraftCookedMeatOreDicts)
			OreDictionary.registerOre(oreDict, new ItemStack(seitanCooked));
	}

	@Override
	public void recipes()
	{
		ContentHelper.addOreSmelting(ContentHelper.rawSeitanOreDict, new ItemStack(seitanCooked), 0.35f);

		GameRegistry.addShapelessRecipe(wheatFlourStack.copy(), wheatCrusher, new ItemStack(Items.wheat));
		Modifiers.crafting.addInputsToKeepForOutput(wheatFlourStack.copy(), wheatCrusher);

		PistonCraftingRegistry.register(new PistonCraftingRecipe(wheatFlourStack.copy(), Items.wheat));

		GameRegistry.addRecipe(new ShapelessOreRecipe(wheatDoughStack.copy(), new ItemStack(Items.water_bucket), ContentHelper.wheatFlourOreDict));
		GameRegistry.addRecipe(new ShapelessOreRecipe(seitanUnwashedStack.copy(), new ItemStack(Items.water_bucket), ContentHelper.wheatDoughOreDict));
		for (int outputMeta = ItemWashableWheat.META_UNWASHED_START + 1; outputMeta < ItemWashableWheat.META_UNWASHED_END; outputMeta++)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(washableWheat, 1, outputMeta), new ItemStack(Items.water_bucket), new ItemStack(washableWheat, 1, outputMeta - 1)));
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(seitanRawStack.copy(), new ItemStack(Items.water_bucket), new ItemStack(washableWheat, 1, ItemWashableWheat.META_RAW - 1)));
	}

	@Override
	public void finish()
	{
	}
}
