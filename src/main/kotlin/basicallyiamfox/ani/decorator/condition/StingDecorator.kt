package basicallyiamfox.ani.decorator.condition

import basicallyiamfox.ani.core.ability.Abilities
import basicallyiamfox.ani.core.rule.RuleDecorator
import basicallyiamfox.ani.core.serializer.ISerializer
import basicallyiamfox.ani.extensions.addProperty
import basicallyiamfox.ani.extensions.getIdentifier
import com.google.gson.JsonObject
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.world.World

class StingDecorator : RuleDecorator() {
    object Serializer: ISerializer<StingDecorator> {
        override fun toJson(obj: JsonObject, type: StingDecorator) {
            obj.addProperty("id", type.id)
        }
        override fun fromJson(obj: JsonObject): StingDecorator {
            val inst = StingDecorator()
            inst.id = obj.getIdentifier("id")
            return inst
        }
        override fun toPacket(buf: PacketByteBuf, type: StingDecorator) {
            buf.writeIdentifier(type.id)
        }
        override fun fromPacket(buf: PacketByteBuf): StingDecorator {
            val inst = StingDecorator()
            inst.id = buf.readIdentifier()
            return inst
        }
    }

    init {
        id = Abilities.STING
    }

    override fun update(world: World, player: PlayerEntity) {}
}