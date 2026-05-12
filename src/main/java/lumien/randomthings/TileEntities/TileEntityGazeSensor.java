package lumien.randomthings.TileEntities;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import lumien.randomthings.Configuration.Settings;

public class TileEntityGazeSensor extends TileEntity {

    private static double range = Settings.GAZE_SENSOR_RANGE;
    private AxisAlignedBB detectionBox;

    @Override
    public void validate() {
        super.validate();
        detectionBox = AxisAlignedBB.getBoundingBox(
                xCoord - range,
                yCoord - range,
                zCoord - range,
                xCoord + range,
                yCoord + range,
                zCoord + range);
    }

    @Override
    public void updateEntity() {
        if (worldObj == null || worldObj.isRemote) return;
        if (detectionBox == null) return;

        if (this.worldObj.getTotalWorldTime() % 5L == 0L) {
            int currMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            boolean gazed = false;

            List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, detectionBox);

            for (EntityPlayer player : players) {
                if (isPlayerLookingAtBlock(player)) {
                    gazed = true;
                    break;
                }
            }

            int nextMeta = gazed ? 1 : 0;

            if (currMeta != nextMeta) {
                this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, nextMeta, 3);
            }
        }
    }

    private boolean isPlayerLookingAtBlock(EntityPlayer player) {
        Vec3 eye = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        Vec3 look = player.getLookVec();

        Vec3 end = Vec3.createVectorHelper(
                eye.xCoord + look.xCoord * range,
                eye.yCoord + look.yCoord * range,
                eye.zCoord + look.zCoord * range);

        MovingObjectPosition hit = worldObj.rayTraceBlocks(eye, end, false);

        return hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                && hit.blockX == xCoord
                && hit.blockY == yCoord
                && hit.blockZ == zCoord;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

}
