package basicallyiamfox.ani

import basicallyiamfox.ani.core.Transformation
import basicallyiamfox.ani.core.Transformations
import basicallyiamfox.ani.core.ability.Abilities
import basicallyiamfox.ani.core.ability.Ability
import basicallyiamfox.ani.core.condition.Condition
import basicallyiamfox.ani.core.rule.Rule
import basicallyiamfox.ani.datagen.AnimorphsAbilityProvider
import basicallyiamfox.ani.datagen.AnimorphsTransformationProvider
import basicallyiamfox.ani.decorator.condition.*
import basicallyiamfox.ani.decorator.rule.*
import basicallyiamfox.ani.extensions.*
import basicallyiamfox.ani.item.AnimorphsItems
import basicallyiamfox.ani.loot.AniLootTableIds
import basicallyiamfox.ani.loot.MagmaJellyCondition
import basicallyiamfox.ani.loot.StingerOPollenCondition
import basicallyiamfox.ani.util.ComparisonOperator
import basicallyiamfox.ani.util.StatModifier
import basicallyiamfox.ani.util.TransKeyUtil
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.apache.commons.lang3.math.Fraction
import java.awt.Color
import java.lang.reflect.Modifier
import java.util.function.BiConsumer
import java.util.function.Consumer

class AnimorphsDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()
        pack.addProvider(::AbilityGenerator)
        pack.addProvider(::TransformationGenerator)
        pack.addProvider(::LangGenerator)
        pack.addProvider(::ModelGenerator)
        pack.addProvider(::RecipeGenerator)
        pack.addProvider(::LootTableGenerator)
    }

    class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
        override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator?) {
        }

        override fun generateItemModels(itemModelGenerator: ItemModelGenerator?) {
            itemModelGenerator!!.register(AnimorphsItems.STINGER_O_POLLEN, Models.GENERATED)
            itemModelGenerator.register(AnimorphsItems.MAGMA_JELLY, Models.GENERATED)
            itemModelGenerator.register(AnimorphsItems.UNFINISHED_SYMPHONY, Models.GENERATED)
            itemModelGenerator.register(AnimorphsItems.DOLPHIN_FIN, Models.GENERATED)
        }
    }

    class LangGenerator(output: FabricDataOutput) : FabricLanguageProvider(output, "en_us") {
        override fun generateTranslations(translationBuilder: TranslationBuilder?) {
            translationBuilder!!.add(TransKeyUtil.getItemGroupKey("transformations_group"), "Transformation items")

            translationBuilder.add(AnimorphsItems.STINGER_O_POLLEN, "Stinger o' Pollen")
            translationBuilder.addTransDesc(
                Transformations.BEE,
                arrayListOf<String>()
                    .addSelf("Turns you into a werebee when in light.")
                    .addSelf("Grants ability to slowly fly up.")
                    .addSelf("Also increases your stats when werebee.")
            )
            translationBuilder.add(AnimorphsItems.MAGMA_JELLY, "Magma Jelly")
            translationBuilder.addTransDesc(
                Transformations.MAGMA_CUBE,
                arrayListOf<String>()
                    .addSelf("Turns you into a magma cube when in Nether.")
                    .addSelf("Grants ability to make really high jumps.")
                    .addSelf("Also increases fire resistance.")
            )
            translationBuilder.add(AnimorphsItems.UNFINISHED_SYMPHONY, "Unfinished Symphony")
            translationBuilder.addTransDesc(
                Transformations.NOTE_BLOCK,
                arrayListOf<String>()
                    .addSelf("'Flapping my arms I began to cluck,")
                    .addSelf("look at me, I'm the disco duck.'")
                    .addSelf("")
                    .addSelf("Turns you into a note block.")
                    .addSelf("Movements will sometimes play sounds.")
            )

            translationBuilder.add(AnimorphsItems.DOLPHIN_FIN, "Dolphin fin")
            translationBuilder.addTransDesc(
                Transformations.DOLPHIN,
                arrayListOf(
                    "dolllllllllllllllll fin"
                )
            )

            translationBuilder.addAbilityName(Abilities.BEEFLY, "Beefly")
            translationBuilder.addAbilityDesc(Abilities.BEEFLY, "Allows to fly while holding a special key.")

            translationBuilder.addAbilityName(Abilities.SOFT_WINGS, "Soft Wings")
            translationBuilder.addAbilityDesc(Abilities.SOFT_WINGS, "You drown much faster.")

            translationBuilder.addAbilityName(Abilities.MAGMATIC_JUMP, "Magmatic Jump")
            translationBuilder.addAbilityDesc(
                Abilities.MAGMATIC_JUMP,
                "Holding a special key for long enough will allow to jump high."
            )

            translationBuilder.addAbilityName(Abilities.WET_OBSIDIAN, "Wet Obsidian")
            translationBuilder.addAbilityDesc(Abilities.WET_OBSIDIAN, "You take damage from touching water.")

            translationBuilder.addAbilityName(Abilities.NOTE_TICK, "Note Tick")
            translationBuilder.addAbilityDesc(Abilities.NOTE_TICK, "Moving will occasionally play notes.")

            translationBuilder.addAbilityName(Abilities.STING, "Sting")
            translationBuilder.addAbilityDesc(
                Abilities.STING,
                "Effect poison to target when attacks. But you will be died."
            )

            translationBuilder.addAbilityName(Abilities.MOIST_SKIN, "Moist Skin")
            translationBuilder.addAbilityDesc(Abilities.MOIST_SKIN, "Needs to keep skin moisture")

            (DamageTypes::class.java.declaredFields).forEach { e ->
                if (!Modifier.isStatic(e.modifiers) && e.canAccess(null))
                    return@forEach

                val damageType = (e.get(null) as RegistryKey<DamageType>)
                translationBuilder.addAbilityName(
                    ModIdentifier("immune_to_${damageType.value.path}_damage"),
                    "Negates ${damageType.value.path.replace('_', ' ')} damage"
                )
            }

            translationBuilder.add(TransKeyUtil.getTooltipKey("hold_shift_to_show"), "Hold [SHIFT] to show abilities.")
            translationBuilder.add(TransKeyUtil.getTooltipKey("is_visual_active"), "Is Visual Active:")
            translationBuilder.add(TransKeyUtil.getAbilitySignKey("positive"), "[+]")
            translationBuilder.add(TransKeyUtil.getAbilitySignKey("neutral"), "[=]")
            translationBuilder.add(TransKeyUtil.getAbilitySignKey("negative"), "[-]")
            translationBuilder.add(TransKeyUtil.getCategoryKey("keys"), "Animorphs key binds")
            translationBuilder.add(TransKeyUtil.getKeyBindKey("beefly"), "Bee Fly ability")
            translationBuilder.add(TransKeyUtil.getKeyBindKey("magma_jump"), "Magmatic Jump ability")
        }
    }

    class TransformationGenerator(output: FabricDataOutput) : AnimorphsTransformationProvider(output) {
        override fun generateTransformations(consumer: Consumer<Transformation?>) {
            Consumer<Consumer<Transformation?>> { t ->
                t.accept(
                    Transformation()
                        .setColor(Color(238, 196, 65).rgb)
                        .setId(Transformations.BEE)
                        .setSkin(ModIdentifier("textures/transformations/bee.png"))
                        .setSlim(ModIdentifier("textures/transformations/bee_slim.png"))
                        .setItem(AnimorphsItems.STINGER_O_POLLEN)
                        .setDesc(
                            arrayListOf<String>()
                                .addSelf(TransKeyUtil.getTransformDescKey("bee.0"))
                                .addSelf(TransKeyUtil.getTransformDescKey("bee.1"))
                                .addSelf(TransKeyUtil.getTransformDescKey("bee.2"))
                        )
                        .addAbilities(
                            arrayListOf<Identifier>()
                                .addSelf(Abilities.BEEFLY)
                                .addSelf(Abilities.JUMP_BOOST_STATUS_EFFECT)
                                .addSelf(Abilities.IMMUNE_TO_FALL_DAMAGE)
                                .addSelf(Abilities.SOFT_WINGS)
                                .addSelf(Abilities.STING)
                        )
                        .addConditions(
                            arrayListOf<Condition>()
                                .addSelf(
                                    Condition().setDecorator(
                                        OrConditionDecorator(
                                            LightLevelConditionDecorator(ComparisonOperator.greater, 7),
                                            AndConditionDecorator(
                                                ConditionDecorators.isDay,
                                                ConditionDecorators.isSkyVisible
                                            )
                                        )
                                    )
                                )
                                .addSelf(Condition().setDecorator(ConditionDecorators.inDimension(World.OVERWORLD)))
                        )
                )

                t.accept(
                    Transformation()
                        .setColor(Color(197, 93, 24).rgb)
                        .setId(Transformations.MAGMA_CUBE)
                        .setSkin(ModIdentifier("textures/transformations/magmacube.png"))
                        .setSlim(ModIdentifier("textures/transformations/magmacube.png"))
                        .setItem(AnimorphsItems.MAGMA_JELLY)
                        .setDesc(
                            arrayListOf<String>()
                                .addSelf(TransKeyUtil.getTransformDescKey("magma_cube.0"))
                                .addSelf(TransKeyUtil.getTransformDescKey("magma_cube.1"))
                                .addSelf(TransKeyUtil.getTransformDescKey("magma_cube.2"))
                        )
                        .addAbilities(
                            arrayListOf<Identifier>()
                                .addSelf(Abilities.MAGMATIC_JUMP)
                                .addSelf(ModIdentifier("fire_resistance_status_effect"))
                                .addSelf(Abilities.IMMUNE_TO_FALL_DAMAGE)
                                .addSelf(Abilities.WET_OBSIDIAN)
                        )
                        .addConditions(
                            arrayListOf<Condition>()
                                .addSelf(
                                    Condition().setDecorator(
                                        BiomeTemperatureConditionDecorator(
                                            ComparisonOperator.greaterOrEqual,
                                            1.5f
                                        )
                                    )
                                )
                        )
                )

                t.accept(
                    Transformation()
                        .setColor(Color(119, 215, 0).rgb)
                        .setId(Transformations.NOTE_BLOCK)
                        .setSkin(ModIdentifier("textures/transformations/noteman.png"))
                        .setSlim(ModIdentifier("textures/transformations/noteman_slim.png"))
                        .setItem(AnimorphsItems.UNFINISHED_SYMPHONY)
                        .setDesc(
                            arrayListOf<String>()
                                .addSelf(TransKeyUtil.getTransformDescKey("note_block.0"))
                                .addSelf(TransKeyUtil.getTransformDescKey("note_block.1"))
                                .addSelf(TransKeyUtil.getTransformDescKey("note_block.2"))
                                .addSelf(TransKeyUtil.getTransformDescKey("note_block.3"))
                                .addSelf(TransKeyUtil.getTransformDescKey("note_block.4"))
                        )
                        .addAbilities(
                            arrayListOf<Identifier>()
                                .addSelf(Abilities.NOTE_TICK)
                        )
                )

                t.accept(
                    Transformation()
                        .setColor(Color(217, 118, 77).rgb)
                        .setId(Transformations.DOLPHIN)
                        .setSkin(ModIdentifier("textures/transformations/dolphin.png"))
                        .setSlim(ModIdentifier("textures/transformations/dolphin_slim.png"))
                        .setItem(AnimorphsItems.DOLPHIN_FIN)
                        .setDesc(
                            arrayListOf(
                                TransKeyUtil.getTransformDescKey("${Transformations.DOLPHIN.path}.0")
                            )
                        )
                        .addAbilities(
                            arrayListOf(
                                ModIdentifier("dolphins_grace_status_effect"),
                                Abilities.MOIST_SKIN
                            )
                        )
                )

            }.accept(consumer)
        }
    }

    class AbilityGenerator(output: FabricDataOutput) : AnimorphsAbilityProvider(output) {
        override fun generateAbilities(consumer: Consumer<Ability?>) {
            Consumer<Consumer<Ability?>> { t ->
                t.accept(
                    Ability()
                        .setId(Abilities.BEEFLY)
                        .setName(TransKeyUtil.getAbilityNameKey("beefly"))
                        .setColor(Color(238, 196, 65))
                        .setSign(Ability.Sign.POSITIVE)
                        .setDesc(arrayListOf<String>().addSelf(TransKeyUtil.getAbilityDescKey("beefly")))
                        .addRules(
                            arrayListOf<Rule>()
                                .addSelf(Rule().setDecorator(BeeflyRuleDecorator(0.077f, 0.0796f, 30)))
                        )
                )
                t.accept(
                    Ability()
                        .setId(Abilities.SOFT_WINGS)
                        .setName(TransKeyUtil.getAbilityNameKey("soft_wings"))
                        .setColor(Color(220, 234, 255))
                        .setSign(Ability.Sign.NEGATIVE)
                        .setDesc(arrayListOf<String>().addSelf(TransKeyUtil.getAbilityDescKey("soft_wings")))
                        .addRules(
                            arrayListOf<Rule>()
                                .addSelf(
                                    Rule()
                                        .addConditions(
                                            arrayListOf<Condition>()
                                                .addSelf(Condition().setDecorator(BeingRainedOnConditionDecorator()))
                                                .addSelf(
                                                    Condition().setDecorator(
                                                        DamagePlayerConditionDecorator(
                                                            DamageTypes.DROWN,
                                                            0.5f
                                                        )
                                                    )
                                                )
                                        )
                                        .setDecorator(
                                            PlaySoundRuleDecorator(
                                                SoundEvents.ENTITY_PLAYER_HURT_DROWN,
                                                0.4f,
                                                2.0f
                                            )
                                        )
                                )
                                .addSelf(Rule().setDecorator(ModifyAirGenerationRuleDecorator(-7)))
                        )
                )

                t.accept(
                    Ability()
                        .setId(Abilities.MAGMATIC_JUMP)
                        .setName(TransKeyUtil.getAbilityNameKey("magmatic_jump"))
                        .setColor(Color(197, 93, 24))
                        .setSign(Ability.Sign.POSITIVE)
                        .setDesc(arrayListOf<String>().addSelf(TransKeyUtil.getAbilityDescKey("magmatic_jump")))
                        .addRules(
                            arrayListOf<Rule>()
                                .addSelf(Rule().setDecorator(MagmaticJumpRuleDecorator(100, 175)))
                        )
                )
                t.accept(
                    Ability()
                        .setId(Abilities.WET_OBSIDIAN)
                        .setName(TransKeyUtil.getAbilityNameKey("wet_obsidian"))
                        .setColor(Color(98, 78, 98))
                        .setSign(Ability.Sign.NEGATIVE)
                        .setDesc(arrayListOf<String>().addSelf(TransKeyUtil.getAbilityDescKey("wet_obsidian")))
                        .addRules(
                            arrayListOf<Rule>()
                                .addSelf(
                                    Rule()
                                        .addConditions(
                                            arrayListOf<Condition>()
                                                .addSelf(
                                                    Condition().setDecorator(
                                                        IsTouchingWaterOrRainConditionDecorator()
                                                    )
                                                )
                                                .addSelf(
                                                    Condition().setDecorator(
                                                        DamagePlayerConditionDecorator(
                                                            DamageTypes.DROWN,
                                                            1.0f + Fraction.ONE_THIRD.toFloat()
                                                        )
                                                    )
                                                )
                                        )
                                        .setDecorator(
                                            PlaySoundRuleDecorator(
                                                SoundEvents.ENTITY_PLAYER_HURT_DROWN,
                                                0.4f,
                                                2.0f
                                            )
                                        )
                                )
                                .addSelf(Rule().setDecorator(ModifyAirGenerationRuleDecorator(-2)))
                                .addSelf(
                                    Rule().setDecorator(
                                        ModifyDamageReceivedRuleDecorator(
                                            DamageTypes.DROWN,
                                            StatModifier(0f, 1f, 3f, 0f)
                                        )
                                    )
                                )
                        )
                )

                t.accept(
                    Ability()
                        .setId(Abilities.NOTE_TICK)
                        .setName(TransKeyUtil.getAbilityNameKey("note_tick"))
                        .setColor(Color(119, 215, 0))
                        .setSign(Ability.Sign.POSITIVE)
                        .setDesc(arrayListOf<String>().addSelf(TransKeyUtil.getAbilityDescKey("note_tick")))
                        .addRules(
                            arrayListOf<Rule>()
                                .addSelf(Rule().setDecorator(NoteTickRuleDecorator(10)))
                        )
                )

                t.accept(
                    Ability()
                        .setId(Abilities.STING)
                        .setName(TransKeyUtil.getAbilityNameKey("sting"))
                        .setColor(Color(73, 185, 96))
                        .setSign(Ability.Sign.NEUTRAL)
                        .setDesc(arrayListOf<String>().addSelf(TransKeyUtil.getAbilityDescKey("sting")))
                        .addRules(
                            arrayListOf<Rule>()
                                .addSelf(Rule().setDecorator(StingDecorator()))
                        )
                )

                t.accept(
                    Ability()
                        .setId(Abilities.MOIST_SKIN)
                        .setName(TransKeyUtil.getAbilityNameKey("moist_skin"))
                        .setColor(Color(42, 45, 89))
                        .setSign(Ability.Sign.NEGATIVE)
                        .setDesc(arrayListOf(TransKeyUtil.getAbilityDescKey("moist_skin")))
                        .addRules(
                            arrayListOf(Rule().setDecorator(MoistSkinRuleDecorator()))
                        )
                )

                (DamageTypes::class.java.declaredFields).forEach { e ->
                    if (!Modifier.isStatic(e.modifiers) && e.canAccess(null))
                        return@forEach

                    val damageType = (e.get(null) as RegistryKey<DamageType>)
                    t.accept(
                        Ability()
                            .setId(ModIdentifier("immune_to_${damageType.value.path}_damage"))
                            .setName(TransKeyUtil.getAbilityNameKey("immune_to_${damageType.value.path}_damage"))
                            .setSign(Ability.Sign.POSITIVE)
                            .setColor(Color(200, 220, 255))
                            .addRules(
                                arrayListOf<Rule>()
                                    .addSelf(Rule().setDecorator(RuleDecorators.immuneDamageTypeDecorator(damageType)))
                            )
                    )
                }

                Registries.STATUS_EFFECT.entrySet.forEach { e ->
                    t.accept(
                        Ability()
                            .setId(ModIdentifier("${e.key.value.path}_status_effect"))
                            .setSign(if (e.value.isBeneficial) Ability.Sign.POSITIVE else Ability.Sign.NEGATIVE)
                            .setName(e.value.translationKey)
                            .setColor(e.value.color)
                            .addRules(
                                arrayListOf<Rule>()
                                    .addSelf(Rule().setDecorator(RuleDecorators.effectDecorator(e.value)))
                            )
                    )
                }

            }.accept(consumer)
        }

        override fun getId(type: Ability): Identifier = type.id

        override fun toJson(type: Ability): JsonObject {
            val obj = JsonObject()
            Ability.Serializer.toJson(obj, type)
            return obj
        }

        override fun getName(): String = "Animorphs/Abilities"
    }

    class RecipeGenerator(output: FabricDataOutput) : FabricRecipeProvider(output) {
        override fun generate(exporter: RecipeExporter) {
            ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, AnimorphsItems.UNFINISHED_SYMPHONY)
                .input('#', ItemTags.TERRACOTTA)
                .input('N', Items.NOTE_BLOCK)
                .pattern("###")
                .pattern("#N#")
                .pattern("###")
                .criterion(hasItem(Items.NOTE_BLOCK), conditionsFromItem(Items.NOTE_BLOCK))
                .offerTo(exporter)
        }
    }

    class LootTableGenerator(output: FabricDataOutput) : SimpleFabricLootTableProvider(output, LootContextTypes.BLOCK) {
        override fun accept(t: BiConsumer<Identifier, LootTable.Builder>?) {
            t!!.accept(
                AniLootTableIds.STINGER_O_POLLEN, LootTable.builder()
                    .pool(
                        LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1.0F))
                            .conditionally(
                                StingerOPollenCondition.Builder()
                                    .setDivideByXEveryYTicks(72000)
                                    .setDefaultDivider(1E+09)
                                    .setDivider(10)
                                    .setUndividedChance(635.0F)
                                    .setMinDivider(100.0)
                                    .setCapChance(0.035)
                                    .setCompareMult(1E-04)
                            )
                            .with(ItemEntry.builder(AnimorphsItems.STINGER_O_POLLEN))
                    )
            )
            t.accept(
                AniLootTableIds.MAGMA_JELLY, LootTable.builder()
                    .pool(
                        LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1.0F))
                            .conditionally(
                                MagmaJellyCondition.Builder()
                                    .setDivider(1E+05)
                                    .setChance(8184.0)
                            )
                            .with(ItemEntry.builder(AnimorphsItems.MAGMA_JELLY))
                    )
            )
        }
    }
}