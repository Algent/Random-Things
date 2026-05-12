package lumien.randomthings.Blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lumien.randomthings.TileEntities.TileEntityGazeSensor;

public class BlockGazeSensor extends BlockContainerBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconUnseen;
    @SideOnly(Side.CLIENT)
    private IIcon iconSeen;

    public BlockGazeSensor() {
        super("gazeSensor", Material.rock);

        this.blockHardness = 2F;
        this.blockResistance = 10.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        iconUnseen = ir.registerIcon("RandomThings:gazeSensor/gazeUnseen");
        iconSeen = ir.registerIcon("RandomThings:gazeSensor/gazeSeen");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess ba, int posX, int posY, int posZ, int side) {
        return ba.getBlockMetadata(posX, posY, posZ) == 1 ? iconSeen : iconUnseen;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return iconUnseen;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int posX, int posY, int posZ, int side) {
        int metadata = blockAccess.getBlockMetadata(posX, posY, posZ);
        return metadata == 1 ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int posX, int posY, int posZ, int side) {
        int metadata = blockAccess.getBlockMetadata(posX, posY, posZ);
        return metadata == 1 ? 15 : 0;
    }

    @Override
    protected <T extends TileEntity> Class getTileEntityClass() {
        return TileEntityGazeSensor.class;
    }
}
