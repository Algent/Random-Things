package lumien.randomthings.TileEntities;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityGazeSensor extends TileEntity {

    static final double range = 16D;

    public TileEntityGazeSensor() {

    }

    @Override
    public void updateEntity() {
        if (this.worldObj == null) return;
        if (this.worldObj.isRemote) return;

        if (this.worldObj.getTotalWorldTime() % 5L == 0L) {
            int currMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            boolean gazed = false;

            List players = worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(
                            xCoord - range,
                            yCoord - range,
                            zCoord - range,
                            xCoord + range,
                            yCoord + range,
                            zCoord + range));

            for (Object playerObj : players) {
                if (playerObj == null) continue;
                EntityPlayer player = (EntityPlayer) playerObj;
                if (isGazedAt(player)) {
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

    private boolean isGazedAt(EntityPlayer player) {

        Vec3 lookVec = player.getLook(1.0F).normalize();

        Vec3 toBlock = Vec3.createVectorHelper(
                (this.xCoord + 0.5D) - player.posX,
                (this.yCoord + 0.5D) - (player.posY + player.getEyeHeight()),
                (this.zCoord + 0.5D) - player.posZ);
        double distance = toBlock.lengthVector();
        toBlock = toBlock.normalize();
        double dot = lookVec.dotProduct(toBlock);

        return dot > 1.0D - 0.05D / distance;
    }

    // this is broken
    private boolean canBlockBeSeenByPlayer(EntityPlayer player) {
        return this.worldObj.rayTraceBlocks(
                Vec3.createVectorHelper(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ),
                Vec3.createVectorHelper(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D)) == null;
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
