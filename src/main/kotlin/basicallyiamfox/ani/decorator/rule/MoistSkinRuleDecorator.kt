package basicallyiamfox.ani.decorator.rule

import basicallyiamfox.ani.core.ability.Abilities
import basicallyiamfox.ani.core.rule.RuleDecorator
import basicallyiamfox.ani.core.serializer.ISerializer
import basicallyiamfox.ani.extensions.addProperty
import basicallyiamfox.ani.extensions.getIdentifier
import basicallyiamfox.ani.extensions.getInt
import com.google.gson.JsonObject
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.world.World
import kotlin.math.min

class MoistSkinRuleDecorator(id: Identifier = Abilities.MOIST_SKIN, val moistTick: Int = 200): RuleDecorator() {
    object Serializer : ISerializer<MoistSkinRuleDecorator> {
        override fun toJson(obj: JsonObject, type: MoistSkinRuleDecorator) {
            obj.addProperty("id", type.id)
            obj.addProperty("moistTick", type.moistTick)
        }

        override fun fromJson(obj: JsonObject): MoistSkinRuleDecorator {
            return MoistSkinRuleDecorator(
                obj.getIdentifier("id"),
                obj.getInt("moistTick")
            )
        }

        override fun toPacket(buf: PacketByteBuf, type: MoistSkinRuleDecorator) {
            buf.writeIdentifier(type.id)
            buf.writeInt(type.moistTick)
        }

        override fun fromPacket(buf: PacketByteBuf): MoistSkinRuleDecorator {
            return MoistSkinRuleDecorator(buf.readIdentifier(), buf.readInt())
        }
    }

    interface MoistSkinPlayerEntity {
        var `animorphs$currentMoisture`: Int
    }

    init {
        this.id = id
    }

    override fun update(world: World, player: PlayerEntity) {
        if (player !is MoistSkinPlayerEntity) return

        if (player.isWet) {
            val currentMoisture = player.`animorphs$currentMoisture`
            player.`animorphs$currentMoisture` = min(moistTick, currentMoisture + 5)
        } else {
            if (--player.`animorphs$currentMoisture` <= 0) {
                player.damage(player.damageSources.dryOut(), 3f)
            }
        }
    }
}