package basicallyiamfox.ani.item

import com.mojang.logging.LogUtils
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object AnimorphsItems {
    private val LOGGER = LogUtils.getLogger()
    private val ITEMS = LinkedHashMap<Identifier, Item>()

    @JvmField
    val GROUP: ItemGroup = FabricItemGroup.builder()
        .displayName(Text.translatable("itemGroup.animorphs.transformations_group"))
        .icon { ItemStack(STINGER_O_POLLEN) }
        .entries { _, entries ->
            entries.add(STINGER_O_POLLEN)
            entries.add(MAGMA_JELLY)
            entries.add(UNFINISHED_SYMPHONY)
            entries.add(DOLPHIN_FIN)
        }
        .build()

    @JvmField
    val STINGER_O_POLLEN: Item = add("stinger_o_pollen", TransformationItem())
    @JvmField
    val MAGMA_JELLY: Item = add("magma_jelly", TransformationItem())
    @JvmField
    val UNFINISHED_SYMPHONY: Item = add("unfinished_symphony", TransformationItem())
    @JvmField
    val DOLPHIN_FIN: Item = add("dolphin_fin", TransformationItem())

    fun init() {
        for ((key, value) in ITEMS) {
            Registry.register(Registries.ITEM, key, value)
        }
        Registry.register(Registries.ITEM_GROUP, Identifier("animorphs", "transformations_group"), GROUP)
    }

    private fun <I : Item> add(name: String, item: I): I {
        if (!ITEMS.containsKey(Identifier("animorphs", name))) {
            ITEMS[Identifier("animorphs", name)] = item
        } else {
            LOGGER.error("Item with same Identifier was registered! ($name)")
        }
        return item
    }
}