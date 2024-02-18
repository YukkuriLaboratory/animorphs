package basicallyiamfox.ani.mixin.decorator.rule;

import basicallyiamfox.ani.core.Transformations;
import basicallyiamfox.ani.decorator.rule.BeeflyRuleDecorator;
import basicallyiamfox.ani.interfaces.IPlayerEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class BeeflyPlayerEntityMixin extends LivingEntity implements BeeflyRuleDecorator.BeeflyPlayerEntity {
    @Unique
    private int stingerFly;
    @Unique
    private int stingerTick;

    @Unique
    private boolean hasStung = false;
    @Unique
    private int ticksSinceSting;

    protected BeeflyPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void animorphs$updateStingerTick(CallbackInfo ci) {
        stingerTick++;
    }

    @Unique
    @Override
    public int getStingerFly() {
        return stingerFly;
    }

    @Unique
    @Override
    public void setStingerFly(int i) {
        stingerFly = i;
    }

    @Unique
    @Override
    public int getStingerTick() {
        return stingerTick;
    }

    @Unique
    @Override
    public void setStingerTick(int i) {
        stingerTick = i;
    }

    @Override
    public void onAttacking(Entity target) {
        if (this instanceof IPlayerEntity player) {
            if (player.getActiveTransformation().equals(Transformations.BEE)) {
                target.damage(this.getDamageSources().sting(this), (float) ((int) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
                this.applyDamageEffects(this, target);
                if (target instanceof LivingEntity) {
                    int i = 0;
                    if (this.getWorld().getDifficulty() == Difficulty.NORMAL) {
                        i = 10;
                    } else if (this.getWorld().getDifficulty() == Difficulty.HARD) {
                        i = 18;
                    }

                    if (i > 0) {
                        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, i * 20, 0), this);
                    }
                }
                hasStung = true;
                this.playSound(SoundEvents.ENTITY_BEE_STING, 1.0F, 1.0F);
            }
        }
        super.onAttacking(target);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (hasStung) {
            ++this.ticksSinceSting;
            if (this.ticksSinceSting % 5 == 0 && this.random.nextInt(MathHelper.clamp(120 - this.ticksSinceSting, 1, 120)) == 0) {
                this.damage(this.getDamageSources().generic(), this.getHealth());
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void animorphs$writeStingerTickToNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound element = nbt.contains("animorphs_data") ? nbt.getCompound("animorphs_data") : new NbtCompound();
        element.putInt("stinger_tick", stingerTick);
        nbt.put("animorphs_data", element);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void animorphs$readStingerTickFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("animorphs_data") && nbt.getCompound("animorphs_data").contains("stinger_tick")) {
            stingerTick = nbt.getCompound("animorphs_data").getInt("stinger_tick");
        }
    }
}
