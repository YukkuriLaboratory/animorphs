package basicallyiamfox.ani.mixin;

import basicallyiamfox.ani.extensions._LivingEntityKt;
import basicallyiamfox.ani.interfaces.IPlayerEntity;
import basicallyiamfox.ani.item.TransformationItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow private @Nullable NbtCompound nbt;

    @Shadow public abstract NbtCompound getOrCreateNbt();
    @Inject(method = "use", at = @At(value = "HEAD"))
    private void animorphs$switchVisualActiveKey(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!_LivingEntityKt.getTransformationManager(user).getTypeByItemId().containsKey(Registries.ITEM.getId(getItem())))
            return;

        user.getStackInHand(hand).getOrCreateNbt().putBoolean(
                TransformationItem.VISUAL_ACTIVE_KEY,
                !user.getStackInHand(hand).getOrCreateNbt().getBoolean(TransformationItem.VISUAL_ACTIVE_KEY)
        );
    }

    @Inject(method = "inventoryTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;inventoryTick(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V"))
    private void animorphs$addVisualActiveKeyAndSetActiveTransformation(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            var manager = _LivingEntityKt.getTransformationManager((PlayerEntity)entity);
            if (manager == null) return;

            var trans = manager.get(getItem());
            if (trans == null) return;

            if (nbt == null || nbt.isEmpty() || !nbt.contains(TransformationItem.VISUAL_ACTIVE_KEY)) {
                getOrCreateNbt().putBoolean(TransformationItem.VISUAL_ACTIVE_KEY, false);
            }

            var duck = (IPlayerEntity)entity;
            var isEnabled = getOrCreateNbt().getBoolean(TransformationItem.VISUAL_ACTIVE_KEY);
            if (isEnabled) {
                duck.setActiveTransformation(trans.getId());
                duck.setTransformationItem((ItemStack)(Object)this);
            } else {
                duck.setActiveTransformation(null);
                duck.setTransformationItem(null);
            }
        }
    }
}
