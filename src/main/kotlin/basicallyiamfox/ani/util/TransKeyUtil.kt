package basicallyiamfox.ani.util

import basicallyiamfox.ani.MOD_ID

class TransKeyUtil {
    companion object {
        fun getCategoryKey(name: String): String {
            return "category.${MOD_ID}.${name}"
        }

        fun getItemGroupKey(name: String): String {
            return "itemGroup.${MOD_ID}.${name}"
        }

        fun getTooltipKey(name: String): String {
            return "${MOD_ID}.tooltip.${MOD_ID}.${name}"
        }

        fun getAbilitySignKey(name: String): String {
            return "${MOD_ID}.ability_sign.${MOD_ID}.${name}"
        }

        fun getKeyBindKey(name: String): String {
            return "key.${MOD_ID}.${name}"
        }

        fun getTransformationKey(name: String): String {
            return "animorphs.transformation.animorphs.${name}"
        }

        fun getTransformDescKey(name: String): String {
            return getTransformationKey("desc.${name}")
        }

        fun getAbilityKey(name: String): String {
            return "${MOD_ID}.ability.${MOD_ID}.${name}"
        }

        fun getAbilityNameKey(name: String): String {
            return getAbilityKey("name.${name}")
        }

        fun getAbilityDescKey(name: String): String {
            return getAbilityKey("desc.${name}")
        }
    }
}