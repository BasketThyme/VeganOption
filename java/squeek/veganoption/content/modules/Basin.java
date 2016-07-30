package squeek.veganoption.content.modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import squeek.veganoption.ModInfo;
import squeek.veganoption.VeganOption;
import squeek.veganoption.blocks.BlockBasin;
import squeek.veganoption.blocks.renderers.RenderBasin;
import squeek.veganoption.blocks.tiles.TileEntityBasin;
import squeek.veganoption.content.IContentModule;

public class Basin implements IContentModule
{
	public static Block basin;

	@Override
	public void create()
	{
		basin = new BlockBasin(Material.IRON)
				.setHardness(2.5F)
				.setUnlocalizedName(ModInfo.MODID + ".basin")
				.setCreativeTab(VeganOption.creativeTab)
				.setRegistryName(ModInfo.MODID_LOWER, "basin");
		GameRegistry.register(basin);
		GameRegistry.registerTileEntity(TileEntityBasin.class, ModInfo.MODID + ".basin");
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			createBasinRenderer();
		}
	}

	@SideOnly(Side.CLIENT)
	public void createBasinRenderer()
	{
		RenderBasin basinRenderer = new RenderBasin();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBasin.class, basinRenderer);
	}

	@Override
	public void oredict()
	{
	}

	@Override
	public void recipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(basin), " g ", "gcg", " g ", 'g', "blockGlassColorless", 'c', Items.CAULDRON));
	}

	@Override
	public void finish()
	{
	}

}
