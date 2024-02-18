package basicallyiamfox.ani.loot

import basicallyiamfox.ani.decorator.rule.BeeflyRuleDecorator.BeeflyPlayerEntity
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext

class StingerOPollenCondition(
    private val divideByXEveryYTicks: Int,
    private val defaultDivider: Double,
    private val divider: Int,
    private val undividedChance: Float,
    private val minDivider: Double,
    private val capChance: Double,
    private val compareMult: Double
) : LootCondition {

    companion object {
        val CODEC: Codec<StingerOPollenCondition> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("divide_by_number_every_x_ticks").forGetter(StingerOPollenCondition::divideByXEveryYTicks),
                Codec.DOUBLE.fieldOf("default_divider").forGetter(StingerOPollenCondition::defaultDivider),
                Codec.INT.fieldOf("divider").forGetter(StingerOPollenCondition::divider),
                Codec.FLOAT.fieldOf("undivided_chance").forGetter(StingerOPollenCondition::undividedChance),
                Codec.DOUBLE.fieldOf("minimum_divider").forGetter(StingerOPollenCondition::minDivider),
                Codec.DOUBLE.fieldOf("capped_chance").forGetter(StingerOPollenCondition::capChance),
                Codec.DOUBLE.fieldOf("comparison_multipiler").forGetter(StingerOPollenCondition::compareMult)
            ).apply(it, ::StingerOPollenCondition)
        }
    }

    override fun test(t: LootContext?): Boolean {
        val player = t!!.get(AniContextParameters.THIS_PLAYER)

        player as BeeflyPlayerEntity
        val hours = player.stingerTick / divideByXEveryYTicks
        var divider = defaultDivider / (this.divider * hours)
        if (divider < minDivider) {
            divider = minDivider
        }

        var chance = undividedChance / divider
        if (chance >= undividedChance * compareMult) {
            chance = capChance
        }
        return t.random.nextDouble() <= chance
    }

    override fun getType(): LootConditionType {
        return AniConditionTypes.STINGER_O_POLLEN
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
            return StingerOPollenCondition(
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