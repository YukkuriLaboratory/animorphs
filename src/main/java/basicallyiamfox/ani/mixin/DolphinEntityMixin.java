package basicallyiamfox.ani.mixin;

import basicallyiamfox.ani.item.AnimorphsItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DolphinEntity.class)
public class DolphinEntityMixin extends MobEntity {
    public DolphinEntityMixin(EntityType<? extends DolphinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        dropItem(AnimorphsItems.DOLPHIN_FIN);
    }
}
