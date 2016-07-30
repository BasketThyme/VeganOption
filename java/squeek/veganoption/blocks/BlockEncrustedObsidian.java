package squeek.veganoption.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import squeek.veganoption.content.modules.Ender;
import squeek.veganoption.helpers.BlockHelper;

public class BlockEncrustedObsidian extends BlockObsidian
{
	public BlockEncrustedObsidian()
	{
		super();
		setSoundType(SoundType.STONE);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	public static void tryPlacePortalAdjacentTo(World world, int x, int y, int z)
	{
		BlockHelper.BlockPos blockPos = BlockHelper.blockPos(world, x, y, z);
		for (BlockHelper.BlockPos blockToCheck : BlockHelper.getBlocksAdjacentTo(blockPos))
		{
			if (blockToCheck.getBlock() != Ender.enderRift
					&& BlockEnderRift.isValidPortalLocation(blockToCheck.world, blockToCheck.x, blockToCheck.y, blockToCheck.z)
					&& blockToCheck.getBlock().isReplaceable(blockToCheck.world, blockToCheck.x, blockToCheck.y, blockToCheck.z))
			{
				world.setBlock(blockToCheck.x, blockToCheck.y, blockToCheck.z, Ender.enderRift);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block changedBlock)
	{
		super.onNeighborBlockChange(world, x, y, z, changedBlock);

		tryPlacePortalAdjacentTo(world, x, y, z);
	}

	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int meta)
	{
		super.onPostBlockPlaced(world, x, y, z, meta);

		tryPlacePortalAdjacentTo(world, x, y, z);
	}
}
