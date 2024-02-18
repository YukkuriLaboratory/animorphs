package basicallyiamfox.ani.loot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import kotlin.math.pow

class MagmaJellyCondition(
    private val divider: Double,
    private val chance: Double
) : LootCondition {

    companion object {
        val CODEC: Codec<MagmaJellyCondition> = RecordCodecBuilder.create {
            it.group(
                Codec.DOUBLE.fieldOf("chance").forGetter(MagmaJellyCondition::chance),
                Codec.DOUBLE.fieldOf("divider").forGetter(MagmaJellyCondition::divider)
            ).apply(it, ::MagmaJellyCondition)
        }
    }

    override fun test(t: LootContext?): Boolean {
        var divider: Double = divider
        if (t!!.random.nextFloat() < 0.3f) {
            divider /= t.random.nextBetween(10, 20).toDouble().pow(0.5) * 4
        } else {
            divider *= (t.random.nextFloat() * 0.5).pow(0.5)
        }
        return t.random.nextFloat() <= (chance / divider / 100.0).toFloat()
    }

    override fun getType(): LootConditionType {
        return AniConditionTypes.MAGMA_JELLY
    }

    class Builder : LootCondition.Builder {
        private var divider: Double = 1E+05
        private var chance: Double = 8184.0

        fun setDivider(value: Double): Builder {
            divider = value
            return this
        }

        fun setChance(value: Double): Builder {
            chance = value
            return this
        }

        override fun build(): LootCondition {
            return MagmaJellyCondition(
                divider,
                chance
            )
        }
    }
}