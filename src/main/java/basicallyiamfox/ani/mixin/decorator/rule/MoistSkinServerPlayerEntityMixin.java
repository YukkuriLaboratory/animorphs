package basicallyiamfox.ani.mixin.decorator.rule;

import basicallyiamfox.ani.decorator.rule.MoistSkinRuleDecorator;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public abstract class MoistSkinServerPlayerEntityMixin implements MoistSkinRuleDecorator.MoistSkinPlayerEntity {
    @Unique
    int animorphs$currentMoisture = 200;

    @Override
    public int getAnimorphs$currentMoisture() {
        return animorphs$currentMoisture;
    }

    @Override
    public void setAnimorphs$currentMoisture(int animorphs$currentMoisture) {
        this.animorphs$currentMoisture = animorphs$currentMoisture;
    }
}
