package squeek.veganoption.content.modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import squeek.veganoption.ModInfo;
import squeek.veganoption.VeganOption;
import squeek.veganoption.blocks.BlockFluidGeneric;
import squeek.veganoption.content.ContentHelper;
import squeek.veganoption.content.IContentModule;
import squeek.veganoption.content.Modifiers;
import squeek.veganoption.content.recipes.InputItemStack;
import squeek.veganoption.content.recipes.PistonCraftingRecipe;
import squeek.veganoption.content.recipes.ShapelessMatchingOreRecipe;
import squeek.veganoption.content.registry.PistonCraftingRegistry;
import squeek.veganoption.content.registry.RelationshipRegistry;
import squeek.veganoption.integration.IntegrationBase;
import squeek.veganoption.integration.IntegrationHandler;
import squeek.veganoption.items.ItemBucketGeneric;
import cpw.mods.fml.common.registry.GameRegistry;

public class PlantMilk implements IContentModule
{
	public static Fluid fluidPlantMilk;
	public static Block plantMilk;
	public static Item bucketPlantMilk;

	@Override
	public void create()
	{
		fluidPlantMilk = new Fluid(ModInfo.MODID + ".plantMilk");
		FluidRegistry.registerFluid(fluidPlantMilk);
		plantMilk = new BlockFluidGeneric(fluidPlantMilk, Material.water, "plant_milk")
				.setBlockName(ModInfo.MODID + ".plantMilk");
		fluidPlantMilk.setBlock(plantMilk);
		fluidPlantMilk.setUnlocalizedName(plantMilk.getUnlocalizedName());
		GameRegistry.registerBlock(plantMilk, "plantMilk");

		bucketPlantMilk = new ItemBucketGeneric(plantMilk)
				.setUnlocalizedName(ModInfo.MODID + ".bucketPlantMilk")
				.setCreativeTab(VeganOption.creativeTab)
				.setTextureName("bucket_milk")
				.setContainerItem(Items.bucket);
		GameRegistry.registerItem(bucketPlantMilk, "bucketPlantMilk");
		FluidContainerRegistry.registerFluidContainer(new FluidStack(fluidPlantMilk, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketPlantMilk), new ItemStack(Items.bucket));
	}

	@Override
	public void oredict()
	{
		if (!IntegrationHandler.modExists(IntegrationBase.MODID_MINEFACTORY_RELOADED))
			OreDictionary.registerOre(ContentHelper.milkOreDict, new ItemStack(Items.milk_bucket));
		OreDictionary.registerOre(ContentHelper.milkOreDict, new ItemStack(bucketPlantMilk));

		OreDictionary.registerOre(ContentHelper.plantMilkSourceOreDict, new ItemStack(Items.pumpkin_seeds));
	}

	@Override
	public void recipes()
	{
		ContentHelper.remapOre(ContentHelper.soybeanOreDict, ContentHelper.plantMilkSourceOreDict);
		ContentHelper.remapOre(ContentHelper.coconutOreDict, ContentHelper.plantMilkSourceOreDict);
		ContentHelper.remapOre(ContentHelper.almondOreDict, ContentHelper.plantMilkSourceOreDict);
		ContentHelper.remapOre(ContentHelper.riceOreDict, ContentHelper.plantMilkSourceOreDict);
		ContentHelper.remapOre(ContentHelper.oatOreDict, ContentHelper.plantMilkSourceOreDict);

		Modifiers.recipes.convertInput(new ItemStack(Items.milk_bucket), ContentHelper.milkOreDict);

		GameRegistry.addRecipe(new ShapelessMatchingOreRecipe(new ItemStack(bucketPlantMilk),
				new ItemStack(Items.water_bucket),
				ContentHelper.plantMilkSourceOreDict,
				ContentHelper.plantMilkSourceOreDict,
				new ItemStack(Items.sugar)));
		Modifiers.crafting.addInputsToRemoveForOutput(new ItemStack(bucketPlantMilk), // output
														new ItemStack(Items.water_bucket));

		PistonCraftingRegistry.register(new PistonCraftingRecipe(fluidPlantMilk, FluidRegistry.WATER, Items.sugar, new InputItemStack(ContentHelper.plantMilkSourceOreDict, 2)));
	}

	@Override
	public void finish()
	{
		RelationshipRegistry.addRelationship(new ItemStack(bucketPlantMilk), new ItemStack(plantMilk));
		RelationshipRegistry.addRelationship(new ItemStack(plantMilk), new ItemStack(bucketPlantMilk));
	}
}
