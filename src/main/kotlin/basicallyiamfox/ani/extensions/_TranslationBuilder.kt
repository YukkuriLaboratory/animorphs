package basicallyiamfox.ani.extensions

import basicallyiamfox.ani.util.TransKeyUtil
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.util.Identifier

fun FabricLanguageProvider.TranslationBuilder.addTransDesc(transId: Identifier, value: String) {
    add(TransKeyUtil.getTransformDescKey(transId.path), value)
}

fun FabricLanguageProvider.TranslationBuilder.addTransDesc(transId: Identifier, values: Iterable<String>) {
    values.withIndex().forEach {
        add(TransKeyUtil.getTransformDescKey("${transId.path}.${it.index}"), it.value)
    }
}

fun FabricLanguageProvider.TranslationBuilder.addAbilityName(abilityId: Identifier, value: String) {
    add(TransKeyUtil.getAbilityNameKey(abilityId.path), value)
}

fun FabricLanguageProvider.TranslationBuilder.addAbilityDesc(abilityId: Identifier, value: String) {
    add(TransKeyUtil.getAbilityDescKey(abilityId.path), value)
}

fun FabricLanguageProvider.TranslationBuilder.addAbilityDesc(abilityId: Identifier, values: Iterable<String>) {
    values.withIndex().forEach {
        add(TransKeyUtil.getTransformDescKey("${abilityId.path}.${it.index}"), it.value)
    }
}