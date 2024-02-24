package basicallyiamfox.ani.extensions

import basicallyiamfox.ani.MOD_ID

fun String.toCategoryKey(): String = "category.${MOD_ID}.$this"

fun String.toItemGroupKey(): String = "itemGroup.${MOD_ID}.$this"

fun String.toTooltipKey(): String = "${MOD_ID}.tooltip.${MOD_ID}.$this"

fun String.toAbilitySignKey(): String = "${MOD_ID}.ability_sign.${MOD_ID}.$this"

fun String.toKeyBindKey(): String = "key.${MOD_ID}.$this"

fun String.toTransformationKey(): String = "${MOD_ID}.transformation.${MOD_ID}.$this"

fun String.toTransformDescKey(): String = "desc.$this".toTransformationKey()

fun String.toAbilityKey(): String = "${MOD_ID}.ability.${MOD_ID}.$this"

fun String.toAbilityNameKey(): String = "name.$this".toAbilityKey()

fun String.toAbilityDescKey(): String = "desc.$this".toAbilityKey()
