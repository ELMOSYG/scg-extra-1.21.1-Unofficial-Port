"""1.20.1 Forge -> 1.21.1 NeoForge 机械重写（分批复用）。

用法：python tools/port_rewrite.py
只做"包名/注册表/GeckoLib 4.9/常量改名"这类机械替换；语义改动由编译错误驱动。
"""
import os
import re

ROOT = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
                    "src", "main", "java", "net", "zincstudios", "scgextra")

RULES = [
    ("net.minecraftforge.registries.ForgeRegistries", "net.minecraft.core.registries.Registries"),
    ("net.minecraftforge.registries.DeferredRegister", "net.neoforged.neoforge.registries.DeferredRegister"),
    ("net.minecraftforge.registries.RegistryObject", "net.neoforged.neoforge.registries.DeferredHolder"),
    ("net.minecraftforge.eventbus.api", "net.neoforged.bus.api"),
    ("net.minecraftforge.client.extensions.common", "net.neoforged.neoforge.client.extensions.common"),
    ("net.minecraftforge.client.event", "net.neoforged.neoforge.client.event"),
    ("net.minecraftforge.client", "net.neoforged.neoforge.client"),
    ("net.minecraftforge.api.distmarker", "net.neoforged.api.distmarker"),
    ("net.minecraftforge.common.MinecraftForge", "net.neoforged.neoforge.common.NeoForge"),
    ("net.minecraftforge.common.ForgeMod", "net.neoforged.neoforge.common.NeoForgeMod"),
    ("net.minecraftforge.common.ForgeConfigSpec", "net.neoforged.neoforge.common.ModConfigSpec"),
    ("net.minecraftforge.common.ForgeSpawnEggItem", "net.neoforged.neoforge.common.DeferredSpawnEggItem"),
    ("net.minecraftforge.common.capabilities", "net.neoforged.neoforge.capabilities"),
    ("net.minecraftforge.common.util.LazyOptional", "net.neoforged.neoforge.common.util.LazyOptional"),
    ("net.minecraftforge.entity.PartEntity", "net.neoforged.neoforge.entity.PartEntity"),
    ("net.minecraftforge.event.entity.living", "net.neoforged.neoforge.event.entity.living"),
    ("net.minecraftforge.event.entity.player", "net.neoforged.neoforge.event.entity.player"),
    ("net.minecraftforge.event.entity", "net.neoforged.neoforge.event.entity"),
    ("net.minecraftforge.event.level", "net.neoforged.neoforge.event.level"),
    ("net.minecraftforge.event.server", "net.neoforged.neoforge.event.server"),
    ("net.minecraftforge.event", "net.neoforged.neoforge.event"),
    ("net.minecraftforge.fml.event.lifecycle", "net.neoforged.fml.event.lifecycle"),
    ("net.minecraftforge.fml.loading", "net.neoforged.fml.loading"),
    ("net.minecraftforge.fml.common.EventBusSubscriber", "net.neoforged.fml.common.EventBusSubscriber"),
    ("net.minecraftforge.fml.common.Mod", "net.neoforged.fml.common.Mod"),
    ("net.minecraftforge.fluids", "net.neoforged.neoforge.fluids"),
    ("net.minecraftforge.items", "net.neoforged.neoforge.items"),
    ("net.minecraftforge.network", "net.neoforged.neoforge.network"),
    ("net.minecraftforge", "net.neoforged.neoforge"),
]

FIELD = {
    "ITEMS": "ITEM", "BLOCKS": "BLOCK", "ENTITY_TYPES": "ENTITY_TYPE",
    "SOUND_EVENTS": "SOUND_EVENT", "MOB_EFFECTS": "MOB_EFFECT",
    "PARTICLE_TYPES": "PARTICLE_TYPE", "BLOCK_ENTITY_TYPES": "BLOCK_ENTITY_TYPE",
    "ATTRIBUTES": "ATTRIBUTE", "ACTIVITIES": "ACTIVITY", "SENSOR_TYPES": "SENSOR_TYPE",
    "MEMORY_MODULE_TYPES": "MEMORY_MODULE_TYPE", "CREATIVE_MODE_TAB": "CREATIVE_MODE_TAB",
}


def find_block(text, brace_index):
    depth = 0
    i = brace_index
    while i < len(text):
        if text[i] == "{":
            depth += 1
        elif text[i] == "}":
            depth -= 1
            if depth == 0:
                return i
        i += 1
    return -1


def rewrite(text):
    t = text
    t = re.sub(r"RegistryObject<([A-Za-z0-9_.]+)>",
               lambda m: "DeferredHolder<%s, %s>" % (m.group(1), m.group(1)), t)
    for a, b in RULES:
        t = t.replace(a, b)
    for a, b in FIELD.items():
        t = t.replace("ForgeRegistries." + a, "Registries." + b)
    t = t.replace("RegistryObject", "DeferredHolder")
    t = t.replace("MinecraftForge.EVENT_BUS", "NeoForge.EVENT_BUS")
    t = t.replace("ForgeSpawnEggItem", "DeferredSpawnEggItem")

    # GeckoLib 4.9
    t = t.replace("software.bernie.geckolib.core.", "software.bernie.geckolib.")
    t = t.replace("software.bernie.geckolib.animatable.model.CoreGeoBone", "software.bernie.geckolib.cache.object.GeoBone")
    t = t.replace("CoreGeoBone", "GeoBone")
    t = t.replace("software.bernie.geckolib.object.PlayState", "software.bernie.geckolib.animation.PlayState")
    t = t.replace("software.bernie.geckolib.util.RenderUtils", "software.bernie.geckolib.util.RenderUtil")
    t = t.replace("MobRenderUtil.", "MobRenderUtils.")

    # 1.21 常量/方法
    t = t.replace("AttributeModifier.Operation.ADDITION", "AttributeModifier.Operation.ADD_VALUE")
    t = t.replace("AttributeModifier.Operation.MULTIPLY_BASE", "AttributeModifier.Operation.ADD_MULTIPLIED_BASE")
    t = t.replace("AttributeModifier.Operation.MULTIPLY_TOTAL", "AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL")
    t = t.replace(".setSecondsOnFire(", ".igniteForSeconds(")
    t = t.replace("getPerceivedTargetDistanceSquareForMeleeAttack(", "distanceToSqr(")
    t = re.sub(r"(SCAttributes\.[A-Z_]+|SCGEAttributes\.[A-Z_]+|ModEffects\.[A-Z_]+)\.get\(\)", r"\1", t)

    # defineSynchedData()
    while True:
        m = re.search(r"(protected|public)\s+void\s+defineSynchedData\s*\(\s*\)\s*\{", t)
        if not m:
            break
        brace = t.index("{", m.start())
        end = find_block(t, brace)
        if end < 0:
            break
        body = t[brace + 1:end].replace("this.entityData.define(", "builder.define(")
        header = t[m.start():brace + 1].replace("defineSynchedData()", "defineSynchedData(SynchedEntityData.Builder builder)")
        t = t[:m.start()] + header + body + t[end:]
        if "import net.minecraft.network.syncher.SynchedEntityData;" not in t:
            t = re.sub(r"(package [^\n]+;\n)", r"\1\nimport net.minecraft.network.syncher.SynchedEntityData;\n", t, count=1)
    t = t.replace("SyncedEntityData.", "SynchedEntityData.")
    t = t.replace("super.defineSynchedData();", "super.defineSynchedData(builder);")

    # MobType
    t = t.replace("import net.minecraft.world.entity.MobType;\n", "")
    t = t.replace("entity.getMobType().equals(MobType.UNDEAD)",
                  "entity.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)")
    t = t.replace("this.getMobType() == MobType.UNDEAD",
                  "this.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)")

    # finalizeSpawn 5 参 -> 4 参
    t = re.sub(r",\s*(@Nullable\s+)?CompoundTag\s+dataTag\s*\)", ")", t)
    t = t.replace("super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag)",
                  "super.finalizeSpawn(level, difficulty, reason, spawnData)")

    # 嵌套泛型修正（DeferredHolder<EntityType<X>>）
    t = re.sub(r"DeferredHolder<EntityType<([A-Za-z0-9_]+)>>", r"DeferredHolder<EntityType<?>, EntityType<\1>>", t)
    return t


def main():
    changed = 0
    for dp, _, fs in os.walk(ROOT):
        for f in fs:
            if not f.endswith(".java"):
                continue
            p = os.path.join(dp, f)
            text = open(p, encoding="utf-8").read()
            new = rewrite(text)
            if new != text:
                open(p, "w", encoding="utf-8").write(new)
                changed += 1
    print("rewritten:", changed)


if __name__ == "__main__":
    main()
