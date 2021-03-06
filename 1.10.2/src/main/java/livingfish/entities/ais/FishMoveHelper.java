package livingfish.entities.ais;

import livingfish.entities.EntityFish;
import livingfish.utils.BlockUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class FishMoveHelper extends EntityMoveHelper {
    private final EntityFish fish;

    public FishMoveHelper(EntityFish fish) {
        super(fish);
        this.fish = fish;
    }

    public void onUpdateMoveHelper() {
        if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.fish.getNavigator().noPath()) {
            double xDisplacement = this.posX - this.fish.posX;
            double yDisplacement = this.posY + 0.2D - (this.fish.posY + this.fish.getEyeHeight());
            double zDisplacement = this.posZ - this.fish.posZ;
            double squareDisplacement = MathHelper.sqrt_double(xDisplacement * xDisplacement + yDisplacement * yDisplacement + zDisplacement * zDisplacement);

            yDisplacement = yDisplacement / squareDisplacement;
            
            float f = (float)(MathHelper.atan2(zDisplacement, xDisplacement) * (180D / Math.PI)) - 90.0F;
            this.fish.rotationYaw = this.limitAngle(this.fish.rotationYaw, f, 90.0F);
            this.fish.renderYawOffset = this.fish.rotationYaw;
            
            float speed = (float)(this.speed * this.fish.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            float speedModifier = BlockUtils.isTank_Water(this.fish.worldObj, this.fish.getPosition()) ? 0.7F : 1.0F;
            this.fish.setAIMoveSpeed(speed * speedModifier);
            
            double d4 = Math.sin((double)(this.fish.ticksExisted)) * 0.05D;
            double d5 = Math.cos((double)(this.fish.rotationYaw * 0.017453292F));
            double d6 = Math.sin((double)(this.fish.rotationYaw * 0.017453292F));
            this.fish.motionX += d4 * d5;
            this.fish.motionZ += d4 * d6;

            d4 = Math.sin((double)(this.fish.ticksExisted)) * 0.05D;
            this.fish.motionY += d4 * (d6 + d5) * 0.25D;
            this.fish.motionY += (double)this.fish.getAIMoveSpeed() * yDisplacement * 0.1D;
            float speedModifierY = BlockUtils.isTank_Water(this.fish.worldObj, this.fish.getPosition()) ? 0.5F : 0.7F;
            this.fish.motionY *= speedModifierY;
        } else {
            this.fish.setAIMoveSpeed(0.0F);
        }
    }
}
