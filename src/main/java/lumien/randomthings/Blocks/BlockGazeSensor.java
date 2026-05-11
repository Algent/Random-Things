package lumien.randomthings.Blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import lumien.randomthings.RandomThings;
import lumien.randomthings.TileEntities.TileEntityGazeSensor;

public class BlockGazeSensor extends BlockContainerBase {

    IIcon[] icons;

    public BlockGazeSensor() {
        super("gazeSensor", Material.ice);

        this.setCreativeTab(RandomThings.creativeTab);

        this.blockHardness = 2.0F;
        icons = new IIcon[2];
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        icons[0] = ir.registerIcon("RandomThings:gazeSensor/gazeUnseen");
        icons[1] = ir.registerIcon("RandomThings:gazeSensor/gazeSeen");
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess p_149677_1_, int p_149677_2_, int p_149677_3_, int p_149677_4_) {
        return 15728704;
    }

    @Override
    public IIcon getIcon(IBlockAccess ba, int posX, int posY, int posZ, int side) {
        int metadata = ba.getBlockMetadata(posX, posY, posZ);
        if (metadata == 1) {
            return icons[1];
        } else {
            return icons[0];
        }
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return icons[0];
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int posX, int posY, int posZ, int side) {
        int metadata = blockAccess.getBlockMetadata(posX, posY, posZ);
        return metadata == 1 ? 15 : 0;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int posX, int posY, int posZ, int side) {
        int metadata = blockAccess.getBlockMetadata(posX, posY, posZ);
        return metadata == 1 ? 15 : 0;
    }

    @Override
    protected <T extends TileEntity> Class getTileEntityClass() {
        return TileEntityGazeSensor.class;
    }
}
