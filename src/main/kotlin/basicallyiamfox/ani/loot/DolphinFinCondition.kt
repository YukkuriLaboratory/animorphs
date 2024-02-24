package basicallyiamfox.ani.loot

import basicallyiamfox.ani.decorator.rule.BeeflyRuleDecorator.BeeflyPlayerEntity
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters

class DolphinFinCondition(
    private val divideByXEveryYTicks: Int,
    private val defaultDivider: Double,
    private val divider: Int,
    private val undividedChance: Float,
    private val minDivider: Double,
    private val capChance: Double,
    private val compareMult: Double
) : LootCondition {

    companion object {
        val CODEC: Codec<DolphinFinCondition> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("divide_by_number_every_x_ticks").forGetter(DolphinFinCondition::divideByXEveryYTicks),
                Codec.DOUBLE.fieldOf("default_divider").forGetter(DolphinFinCondition::defaultDivider),
                Codec.INT.fieldOf("divider").forGetter(DolphinFinCondition::divider),
                Codec.FLOAT.fieldOf("undivided_chance").forGetter(DolphinFinCondition::undividedChance),
                Codec.DOUBLE.fieldOf("minimum_divider").forGetter(DolphinFinCondition::minDivider),
                Codec.DOUBLE.fieldOf("capped_chance").forGetter(DolphinFinCondition::capChance),
                Codec.DOUBLE.fieldOf("comparison_multipiler").forGetter(DolphinFinCondition::compareMult)
            ).apply(it, ::DolphinFinCondition)
        }
    }

    override fun test(t: LootContext): Boolean {
        return t.random.nextDouble() <= 0.1
    }

    override fun getType(): LootConditionType {
        return AniConditionTypes.DOLPHIN_FIN
    }

    class Builder : LootCondition.Builder {
        private var divideByXEveryYTicks = 72000
        private var defaultDivider = 1E+09
        private var divider = 10
        private var undividedChance = 635.0F
        private var minDivider = 100.0
        private var capChance = 0.035
        private var compareMult = 1E-04

        fun setDivideByXEveryYTicks(value: Int): Builder {
            divideByXEveryYTicks = value
            return this
        }

        fun setDefaultDivider(value: Double): Builder {
            defaultDivider = value
            return this
        }

        fun setDivider(value: Int): Builder {
            divider = value
            return this
        }

        fun setUndividedChance(value: Float): Builder {
            undividedChance = value
            return this
        }

        fun setMinDivider(value: Double): Builder {
            minDivider = value
            return this
        }

        fun setCapChance(value: Double): Builder {
            capChance = value
            return this
        }

        fun setCompareMult(value: Double): Builder {
            compareMult = value
            return this
        }

        override fun build(): LootCondition {
            return DolphinFinCondition(
                divideByXEveryYTicks,
                defaultDivider,
                divider,
                undividedChance,
                minDivider,
                capChance,
                compareMult
            )
        }
    }
}