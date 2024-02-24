package basicallyiamfox.ani.extensions

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.util.Identifier

fun FabricLanguageProvider.TranslationBuilder.addTransDesc(transId: Identifier, value: String) {
    add(transId.path.toTransformDescKey(), value)
}

fun FabricLanguageProvider.TranslationBuilder.addTransDesc(transId: Identifier, values: Iterable<String>) {
    values.withIndex().forEach {
        add("${transId.path}.${it.index}".toTransformDescKey(), it.value)
    }
}

fun FabricLanguageProvider.TranslationBuilder.addAbilityName(abilityId: Identifier, value: String) {
    add(abilityId.path.toAbilityNameKey(), value)
}

fun FabricLanguageProvider.TranslationBuilder.addAbilityDesc(abilityId: Identifier, value: String) {
    add(abilityId.path.toAbilityDescKey(), value)
}

fun FabricLanguageProvider.TranslationBuilder.addAbilityDesc(abilityId: Identifier, values: Iterable<String>) {
    values.withIndex().forEach {
        add("${abilityId.path}.${it.index}".toTransformDescKey(), it.value)
    }
}