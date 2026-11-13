# SCG Extra 1.21.1 / NeoForge 移植清单

- **源工程**：`E:\mod\scg-extra-main\scg-extra-main`（SCG Extra 4.0.0，Forge 1.20.1-47.4.10，GPL-3.0）
- **目标工程**：`E:\mod\scg-extra-1.21`（当前是空白 NeoForge MDK 模板）
- **目标环境**：Minecraft 1.21.1 + NeoForge 21.1.x + Java 21
- **配套文件**：[PORTING-MATRIX.md](PORTING-MATRIX.md) —— 480 个文件逐个的分级与标记
- **生成日期**：2026-11-13

> 本清单的所有数字与结论都来自对源工程和 `libs/` 里 1.21.1 依赖 jar 的实际扫描（javap / 解包比对），不是照抄移植指南。指南与实测冲突的地方在 §2.4 单独标出。

---

## 阶段进度（2026-11-13）

| 阶段 | 状态 | 说明 |
|---|---|---|
| P0 工程化 | ✅ 完成 | NeoForge 21.1.249 / ModDevGradle 2.0.146 / Gradle 8.8 / JDK 21；`gradlew build` 离线成功，产物 15 MB |
| 阶段 1 注册层 | ✅ 完成（有 3 处延后） | 物品/护甲/武器/方块/声音/效果/粒子/属性/配置/创造标签全部编译并通过 resource 打包 |
| 阶段 1 延后项 | ⏳ | 刷怪蛋（依赖实体）、炮台方块与物品（依赖实体）、长按攻击的客户端网络同步（依赖阶段 6） |
| 运行验证 | ⚠️ 离线受阻 | `gradlew build` 通过；`runServer` / `runClient` 需要 MDG 的 `neoforge-21.1.249-moddev-config.json`，该文件只能从 `maven.neoforged.net` 下载（本机不可达）。联网或从另一台机器拷入 Gradle 缓存后即可运行。 |
| 阶段 2 实体公共框架 | ✅ 完成 | `entity` 根 8 + `entity/common` 91（含 brain 35 / goal 10 / gun 14 / client 15 / part 6）+ 1 个普通实体 `raid_summoner`；`defineSynchedData(Builder)`、`finalizeSpawn` 4 参、`PartEntity`、`ItemAttributeModifiers`、Brain 注册表等已迁移 |
| 阶段 2 延后项 | ⏳ | 多部件**具体生物**（PartEntity 基类已编译，实体在阶段 4 的 COG 包）、刷怪蛋与 82 个实体注册（阶段 4）、枪口闪光网络（阶段 6）、`GunAttackGoalAccessor` Mixin（阶段 7） |
| 阶段 3 AI 与枪械模拟 | ✅ 完成（静态） | `entity/common/gun` 14 个文件 + brain 35 + goal 10 全部编译；新增可实战的持枪单位 **`scgextra:scout`**（`ScoutEntity` + `ScoutRenderer`，`data/scguns/entity/equipment/scout.json` 提供 bruiser / scrapper / umax_pistol 装备池）；弹药与附件改走 `CustomData` |
| 阶段 3 延后项 | ⏳ | 枪口闪光/弹道网络同步（阶段 6）、`GunAttackGoalAccessor`（阶段 7）、`MountedGun` 类载具（阶段 4）、实际开火手感需运行时验证 |
| 阶段 4 派系实体 | ✅ 完成 | 8 个派系注册表全部接入（RRC / FAC / neutral / whaler / piglin / COG / asgharian / wreckers）+ `turret_seat` + 4 类弹丸；**71 个刷怪蛋恢复**；炮台方块/方块实体/渲染器与 `TurretSeatEntity` 完整接回 |
| 阶段 4 收尾项 | ⏳ | 运行时验证（离线仍缺 `moddev-config.json`）；`turret` 交互事件改用 `useItemOn` 后需实机确认 |
| 阶段 5 客户端渲染 | ✅ 静态审计完成 | 75 个 GeckoLib 模型路径：geo/贴图/发光遮罩齐全；12 处 `renderRecursively` 全部 11 参；旧顶点链 0 残留；详见 [docs/PHASE5-RENDER-AUDIT.md](docs/PHASE5-RENDER-AUDIT.md) |
| 阶段 5 待运行时验证 | ⚠️ | 离线缺 `moddev-config.json`；模型朝向/持枪层/光束混合/护甲贴合需实机目视 |
| 阶段 6 网络 | ✅ 完成 | Framework 0.13 重写：3 个消息全部改为 `StreamCodec` + 静态 handler；`SCGEPacketHandler` 用 `registerPlayMessage(name, class, codec, handler, PacketFlow)`；恢复 6 处枪口闪光发送、长按攻击客户端 tick、炮台输入 `TurretInputHandler`、墨汁屏幕层 `ScreenRenderer`；`GunFlashHandler` 客户端 tick 重新挂载；网络在 `commonSetup` 初始化 |
| 阶段 7 袭击/Mixin | ✅ 完成 | 16 个 Mixin 全部接入（`scgextra.mixins.json` JAVA_21、无 refmap、jar 内已声明）；5 个袭击文件迁移到 `SavedData.Factory` + `save(tag, registries)` + `LevelTickEvent.Post`；`RaidDataLoader` 数据包 reload；`DevTestCommands`；`GunAttackGoalAccessor` 重新启用（持枪瞄准状态） |
| 阶段 8 世界生成 | ✅ 完成 | 16 个 worldgen 文件：`ModStructures`（5 个结构，`StructureType` 函数式注册）、2 个 processor（`MapCodec` + `StructureProcessorType.register`）、`JigsawPlacement.addPieces` 补 `PoolAliasLookup/DimensionPadding/LiquidSettings`；`TreeGrower` 改静态实例 + 恢复 `WARZONE_SPRUCE_SAPLING`；`BootstapContext`→`BootstrapContext`、`Blocks.GRASS`→`SHORT_GRASS`；TerraBlender 区域与表面规则在 `commonSetup` 注册 |
| 阶段 9 数据与收尾 | ✅ 完成 | 合并源工程 `src/generated/resources` 的 **333 个 datagen 产物**（含缺失的 `warzone_biome.json`、`configured_feature`、`placed_feature`、刷怪蛋模型、战利品表）；1.21 数据包单数化（`advancement`/`loot_table`/`recipe`/`structure`、`tags/block|item|entity_type`、`neoforge/biome_modifier`）；配方 `result.item`→`result.id`；`forge:` 标签/loader/types → `neoforge:`/`c:` |
| 最终状态 | ✅ | **474 个 Java 文件**，jar **15.64 MB**，`gradlew build` 通过；`data` 内：结构 150、战利品表 95、配方 55、进度 51、群系 1、模板池 51、结构 NBT 125、物品模型 147 |
| 已知限制 | ⚠️ | ① 运行时未验证（离线缺 `moddev-config.json`）；② 上游只有 `en_us`，未生成 `zh_cn`（保持与上游一致）；③ Mixin 注入点需实机冒烟 |
| 运行时修复（2026-11-13 第十七次） | ✅ | 地雷幽灵攻击者：`MineUnitBlockEntityMixin` 造的临时 ArmorStand 未移除，生物会把它当目标；创建后立即 `discard()` |
| 运行时修复（2026-11-13 第十六次） | ✅ | bunker 入口链：新建单元素 `start_entry` 池（仅 start_top）作为结构起点，原 `start` 池保留 6 件作为连接目标池；移除上一步的“地下 40 格”退化逻辑 |
| 运行时修复（2026-11-13 第十五次） | ✅ | bunker_trenches 生成到空中：恢复 `start_jigsaw_name`（命名 jigsaw 锚定），并对不含该名字的起始变体加“地表下 40 格”的退化路径 |
| 运行时修复（2026-11-13 第十四次） | ✅ | 结构不生成：`WaterAvoidingStructure` 的固定 y=62 水位检测改为按 `project_start_to_heightmap` 实际地表检测；`leviathan_prismarine_gate` 的 biomes 改为 `#minecraft:is_beach` |
| 运行时修复（2026-11-13 第十三次） | ✅ | 海洋结构漏水：1.21 的 `liquid_settings` 默认 `APPLY_WATERLOGGING`；16 个 jigsaw JSON 加 `ignore_waterlogging`，5 个自定义结构的 `addPieces` 改 `IGNORE_WATERLOGGING` |
| 运行时修复（2026-11-13 第十二次） | ✅ | 移动滑步：GeckoLib `state.isMoving()` 不可靠，新增 `MobUtil.isMoving(Entity)` 并替换全项目 63 个文件中的判断，移动+瞄准正确播放 `walk_holding_aim` |
| 运行时修复（2026-11-13 第十一次） | ✅ | 转向覆盖：新增 `MobBodyRotationMixin`，在 `Mob#createBodyControl` RETURN 处对全部 `scgextra` 生物替换为 `GunnerBodyRotationControl`（FAC 的 `EquippedEntity` 单位也因此生效） |
| 运行时修复（2026-11-13 第十次） | ✅ | 转向手感：移除全局 `BodyRotationControlMixin`，改为 `GunnerEntity#createBodyControl` 返回自写 `GunnerBodyRotationControl`（移动 45°/tick，站立指数平滑 35%/tick，头身夹角限制） |
| 运行时修复（2026-11-13 第九次） | ✅ | 回退：确认动画由 `GunAttackGoalAccessor` 桥接驱动后，RRC 五单位与 WreckerGreen 恢复初版（scguns `GunAttackGoal`），移除 `Gunner`/`SimpleGunAttackGoal`；仅保留 `updateAimStates` 的双 AI 兼容 |
| 运行时修复（2026-11-13 第八次） | ✅ | Wrecker 排查：Red/TurretGunner 已用 `SimpleBurstGunAttackGoal`（随状态机修复恢复动画）；Green 为 RPG 且被 scguns 注入，已迁移到 `Gunner` + `SimpleGunAttackGoal`；Blue/Jumbo 为近战/Brain，不涉及枪 AI |
| 运行时修复（2026-11-13 第七次） | ✅ | RRC AI：`GunnerEntity.updateAimStates` 支持 `SimpleGunAttackGoal` 状态；`CopperKnight/Gargoyle/Oppressor/Tallman` 实现 `Gunner` 并注册自带枪 AI，阻止 scguns 注入导致动画丢失 |
| 运行时修复（2026-11-13 第六次） | ✅ | 转身过慢：原版 `BodyRotationControl` 站立时每 tick 只转 1/10，且 `yBodyRot` 是客户端本地值；新增 `BodyRotationControlMixin`，对 scg-extra 且有目标的生物用 30°/tick 转向 |
| 运行时修复（2026-11-13 第五次） | ✅ | 调试碰撞箱：`EntityHeadBoxDebug` 实际未注册，真凶是 scguns 的 `HeadshotBoxDebugRenderer` 对 Sable 巨型实体回退 `DynamicHeadshotBox`；新增 client Mixin 跳过任一轴 >16 格的框，并给 `EntityHeadBoxDebug` 加同样的保护 |
| 运行时修复（2026-11-13 第四次） | ✅ | ① RRC Drone 不攻击：恢复阶段 4 留空的 `MountedGunAttackGoal#fireGun`；② Scout 被 scguns AI 接管：实现 `Gunner` 标记 + `SimpleGunAttackGoal`，`GunnerMobSpawnerMixin` 生效；③ 攻击时模型不转向：`SimpleGunAttackGoal#handleAttack` 同步 `yRot/yBodyRot/yHeadRot` 与 `O` 字段 | |
| 运行时修复（2026-11-13 第三次日志） | ✅ | ① 海底神殿生成崩溃：`GuardianStatueEntity#defineSynchedData` 在构造期调用 `getLookAngle()`，`entityData` 尚未创建 → NPE，改为初始 `Vector3f(0,0,0)`；② `bunker_trenches` 的 `start_jigsaw_name` 与 6 元素起始池冲突，移除该字段 | 日志见 [docs/RUNTIME-ISSUES.md](docs/RUNTIME-ISSUES.md)：① `grass_patch`/`gravel_patch` 的 1.21 格式错误导致建世界崩溃；② **宝箱为空**：战利品表内 `minecraft:loot_table` 引用的 `name` 字段在 1.21 改为 `value`（9 个表），另 `minecraft:scute`→`turtle_scute`；③ **荧光层不可见**：`reRender` 的 ARGB 颜色参数被误写成 `1`（alpha=0），6 个渲染器改为 `0xFFFFFFFF`；④ **后坐力为 0**：`RecoilHandlerMixin` 裸乘属性值，1.21 scguns 的 `getAdsRecoilReduction` 已改为倍率，改为 `1+value/100`，并修正服从勋章的修改符类型；⑤ 补齐树苗/placeholder/弹道贴图与 3 处声音引用；12 个 GeckoLib 新格式模型为上游遗留（仍可渲染） |

阶段 1 实际落地的文件：`SCGExtra`、`CommonConfig`、`attributes/SCGEAttributes`、`effects/*`(7)、`particle/ModParticleTypes`、`sounds/*`(11)、`tag/ModTags`、`item/**`(38)、`block/ModBlocks`、`block/ModBlockEntities`、`block/barbed_wires/*`、`block/electrical_wires/*`、`client/particle/*`(2)，共约 66 个 Java 文件。

阶段 1 中为兼容 1.21 做的语义改动（后续阶段会再次用到）：

- `ArmorMaterial` 在 1.21.1 变成 **record**：`ModArmorMaterials` 重写为 `DeferredRegister<ArmorMaterial>`，耐久倍率移到 `armorProperties()` 写进 `Item.Properties`；
- 护甲额外属性从 `getDefaultAttributeModifiers(EquipmentSlot)` 改为 **`getDefaultAttributeModifiers()` + `ItemAttributeModifiers`**；
- 武器构造器 `super(tier, damage, speed, props)` → **`super(tier, props.attributes(XxxItem.createAttributes(...)))`**；
- `AttributeModifier` 使用 `ResourceLocation` id，`Operation.ADDITION` → **`ADD_VALUE`**；
- 药水效果全部走 **`Holder<MobEffect>`**；`MobEffect.applyEffectTick` 返回 boolean、`isDurationEffectTick` → `shouldApplyEffectTickThisTick`；
- 电线方块实体改用 **`RegisterCapabilitiesEvent` + `level.getCapability(Capabilities.EnergyStorage.BLOCK, ...)`**，NBT 用 `saveAdditional(tag, registries)` / `loadAdditional(...)`；
- `appendHoverText(ItemStack, Level, ...)` → **`(ItemStack, Item.TooltipContext, ...)`**；
- `hurtAndBreak(n, entity, consumer)` → `(n, entity, EquipmentSlot)` 或 `(n, ServerLevel, entity, consumer)`；
- 玩家 tick 事件统一 `PlayerTickEvent.Post` + `event.getEntity()`。

---

## 0. 一句话结论

要移植的是 **480 个 Java 文件（48,449 物理行 / 41,938 非空行）**，其中：

- **49 个必须重写实现**（16 Mixin、16 世界生成、8 datagen、4 网络、5 渲染器）；
- **170 个需要改语义**（实体同步数据、GeckoLib、PartEntity、配置、附魔等）；
- **261 个是机械替换**（`net.minecraftforge.*` → `net.neoforged.*`、注册表 API）。

好消息：**scguns 1.1.0 的接口和 12 个 Mixin 注入点全部还在**，TerraBlender / Curios 用到的那部分 API 也没变，创造标签也已经是 1.21 的新写法；真正要重写的是 **Framework 网络层** 和 **GeckoLib 渲染签名**。

---

## 1. 规模与分级

### 1.1 按包统计

| 包 | 文件 | 行 | 重写 | 中等 | 机械 | 主要工作 |
|---|---:|---:|---:|---:|---:|---|
| `entity` | 354 | 37,915 | 5 | 149 | 200 | 82 个实体注册、属性/生成、SynchedEntityData、GeckoLib、PartEntity |
| `item` | 38 | 3,121 | 0 | 14 | 24 | 装备/勋章/医疗包/军刀；Curios 渲染；刷怪蛋 |
| `worldgen` | 16 | 912 | 16 | 0 | 0 | 群系 / 结构 / 地物 / 树；TerraBlender |
| `mixin` | 16 | 820 | 16 | 0 | 0 | 12 个注入 scguns，4 个注入原版 |
| `sounds` | 11 | 823 | 0 | 0 | 11 | SoundEvent 注册 |
| `block` | 9 | 945 | 0 | 4 | 5 | 铁丝网 / 电线 / 固定炮台（方块实体 + 渲染） |
| `datagen` | 8 | 1,623 | 8 | 0 | 0 | 8 个 DataProvider |
| `effects` | 7 | 147 | 0 | 0 | 7 | MobEffect 注册 |
| `raid` | 5 | 902 | 0 | 2 | 3 | 波次袭击管理器 |
| `client` | 4 | 278 | 0 | 0 | 4 | 屏幕渲染 / 炮台输入 / 粒子 |
| `network` | 4 | 179 | 4 | 0 | 0 | Framework 网络 |
| `debug` | 2 | 141 | 0 | 0 | 2 | 测试命令 |
| 根（`SCGExtra` / `CommonConfig`） | 2 | 382 | 0 | 1 | 1 | 主类 + 配置 |
| `attributes` | 1 | 83 | 0 | 0 | 1 | 自定义属性 |
| `data` | 1 | 119 | 0 | 0 | 1 | 袭击数据 reload loader |
| `particle` | 1 | 47 | 0 | 0 | 1 | 粒子类型 |
| `tag` | 1 | 12 | 0 | 0 | 1 | 标签常量 |
| **合计** | **480** | **48,449** | **49** | **170** | **261** | |

### 1.2 `entity` 包内部分布（工作的 78%）

| 子包 | 文件 | 实体类 | 客户端类 | 行 | 说明 |
|---|---:|---:|---:|---:|---|
| `entity/common` | 91 | — | 15 | 5,848 | 公共框架：`GunnerEntity`、`EquippedEntity`、brain 35、goal 10、gun 14、part 6 |
| `entity/whaler` | 38 | 9 | 7 | 4,602 | 利维坦商会 |
| `entity/piglin` | 38 | 8 | 4 | 4,677 | 猪灵 |
| `entity/neutral` | 35 | 13 | 4 | 4,865 | 末地/下界/主世界中立生物 |
| `entity/cog` | 33 | 10 | 7 | 3,971 | COG（多部件巨型单位集中地） |
| `entity/fac` | 28 | 11 | 4 | 3,446 | 联合军 |
| `entity/asgharian` | 27 | 6 | 1 | 3,132 | 阿斯加里安 |
| `entity/rrc` | 23 | 10 | 2 | 3,320 | RRC |
| `entity/wreckers` | 20 | 8 | 4 | 2,718 | 拆解者 |
| `entity/projectile` | 10 | 4 | 3 | 645 | 自定义弹丸 |
| `entity/turret` | 3 | 1 | 0 | 186 | 炮台座位 |
| `entity`（根） | 8 | — | — | 505 | `ModEntities`、`Faction`、`AbilityState` 等 |

- 实体类合计 **80** 个；`EntityType.Builder.of(...)` 出现 **82** 次（`ModEntities` 里另有 2 个非派系实体）。
- 引用 scguns 的文件 **122** 个；引用 GeckoLib 的文件 **151** 个。
- 客户端相关（`client` 目录或 `*Renderer/*Model/*Layer`）**51** 个；名字以 `Renderer` 结尾的 **43** 个。

---

## 2. 兼容性实测（决定工作量的关键一节）

### 2.1 结论速览

| 依赖 | 实测版本 | 结论 | 影响面 |
|---|---|---|---|
| **scguns** | 1.1.0hotfix（对外版本 `1.5.0+1.1.0hotfix`） | ✅ 61 个被 import 的类**全部存在**；`GunItem#getModifiedGun`、`Gun#getAmmoCount`、`GunModifierHelper#getModifiedRate` 签名保留 | 122 个文件 |
| **scguns Mixin 目标** | 同上 | ✅ 12 个注入目标成员**全部存在**（含 `ProjectileEntity` 6 参构造器、`performGunAttack` 5 参） | §5.1 |
| **TerraBlender** | 4.1.0.8 | ✅ `Region` 构造器、`RegionType.OVERWORLD`、`SurfaceRuleManager.addSurfaceRules` 与 1.20.1 一致 | 16 个 worldgen 文件 |
| **Curios** | 9.5.1+1.21.1 | ✅ `CuriosRendererRegistry.register/getRenderer` 保留；`ICurioRenderer` 需按编译错误微调 | 10 处 import |
| **GeckoLib** | 4.9.3 | ⚠️ `GeoEntityRenderer#renderRecursively` 由 **14 参变为 11 参**（RGBA 合成一个 `int color`） | 5 个渲染器 + 151 个模型类 |
| **Framework** | 0.13.11 | ❌ `IMessage` 已删除；`registerPlayMessage` 改为 `(String, Class, StreamCodec, BiConsumer<T, MessageContext>)` | 4 个网络文件（必须重写） |

### 2.2 已验证“不用改”的部分（可省大量时间）

1. **scguns 包名与核心 API 原样保留**：`top.ribs.scguns.*` 61 个 import 100% 命中；`Gun.getAmmoCount(ItemStack)`（静态）/`GunItem#getModifiedGun` 都在。
2. **12 个 scguns Mixin 的注入成员全部还在**（逐个 `javap -p` 验证，见 §5.1 表），包括最容易坏的精确描述符：
   - `ProjectileEntity(EntityType, Level, LivingEntity, ItemStack, GunItem, Gun)` 6 参构造器；
   - `AIGunEvent.performGunAttack(Mob, LivingEntity, ItemStack, Gun, float)`；
   - `RaidManager.hasActiveRaid()/surrenderRaid()`、`RaidSaveData` 的 3 个字段、`GunAttackGoal` 的 4 个字段。
3. **TerraBlender 这块不用重写**：`Region(ResourceLocation, RegionType, int)`、`RegionType`、`SurfaceRuleManager.RuleCategory` 与指南说的“API 大改”相反——实测签名未变，工作量在结构/群系数据而不是 API。
4. **4 个原版 Mixin 目标在 1.21.1 仍存在**：`Entity#dampensVibrations`、`LivingEntity#getDamageAfterMagicAbsorb`、`LivingEntity#checkTotemDeathProtection`（private）、`OceanMonumentPieces.MonumentBuilding#generateWing`（private）、`ResourceOrTagKeyArgument#listSuggestions`。
5. **创造标签已经是 1.21 写法**：`ModItems` 里已经有 `DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ...)` 和 `BuildCreativeModeTabContentsEvent`，不需要重做，只换包名与 `RegistryObject` → `DeferredItem`。

### 2.3 必须改的部分

| 项 | 1.20.1 写法 | 1.21.1 写法 | 涉及文件 |
|---|---|---|---|
| 网络消息 | `IMessage` + `registerPlayMessage(Class, MessageDirection)` | 消息类 + `StreamCodec`；`registerPlayMessage(String, Class, StreamCodec, BiConsumer<T, MessageContext>)`；发送走 `FrameworkNetwork#sendToServer(Object)` | 4 |
| GeckoLib 渲染 | `renderRecursively(..., float r, float g, float b, float a)` 14 参 | `renderRecursively(..., int packedOverlay, int color)` 11 参 | 5 |
| 实体同步数据 | `defineSynchedData()` | `defineSynchedData(SynchedEntityData.Builder)` | 45 |
| 生成回调 | `finalizeSpawn(ServerLevelAccessor, DifficultyInstance, MobSpawnType, SpawnGroupData)` | 签名基本保留，但 `MobSpawnType` 的调用点需核对 | 24 |
| 实体网络包 | `getAddEntityPacket()` | `getAddEntityPacket(ServerEntity)` | 1 |
| 创造标签 | 已经是 `CreativeModeTab` + `BuildCreativeModeTabContentsEvent`（1.20.1 就有） | 只需换包名与 `RegistryObject` → `DeferredItem`；`ForgeSpawnEggItem` → `DeferredSpawnEggItem` | `ModItems` |
| 配置 | `ForgeConfigSpec` | `net.neoforged.neoforge.common.ModConfigSpec` | `CommonConfig` |
| 生成事件 | `SpawnPlacementRegisterEvent` | `net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent` | 3 |
| 属性常量 | `net.minecraftforge.common.ForgeMod` | `net.neoforged.neoforge.common.NeoForgeMod` | 10 处 import |
| 多部件实体 | `net.minecraftforge.entity.PartEntity` | `net.neoforged.neoforge.entity.PartEntity` | 27 个文件 |
| 客户端扩展 | `net.minecraftforge.client.extensions.common.IClientItemExtensions` | `net.neoforged.neoforge.client.extensions.common.IClientItemExtensions` | 8 |
| 主类构造 | `SCGExtra(FMLJavaModLoadingContext)` | `SCGExtra(IEventBus, ModContainer)`（MDK 写法） | 1 |
| 事件总线 | `Bus.FORGE` / `MinecraftForge.EVENT_BUS` | `Bus.GAME` / `NeoForge.EVENT_BUS` | 5 |
| 旧注册表 | `ForgeRegistries.X` + `RegistryObject` | `Registries.X` / `DeferredRegister.Blocks/Items` + `DeferredHolder/DeferredItem/DeferredBlock` | 18 + 26 |

### 2.4 与移植指南不一致的三处（以实测为准）

| 指南说法 | 实测 |
|---|---|
| “TerraBlender 1.21.1 API 有变动，`BiomeProvider` 构造方式不同” | 本项目只用到 `Region`/`RegionType`/`SurfaceRuleManager`，**签名未变**；真正要改的是 `warzone` 群系数据与结构 JSON |
| “GeckoLib `renderRecursively` 增加了两个 float 参数” | 方向相反：**参数减少了 3 个**，`red/green/blue/alpha` 合并成一个 `int color`（14 → 11） |
| “`EnchantmentHelper.getEnchantments` 变了” | 全工程只有 **1 个文件**用 `EnchantmentHelper`，影响面远小于指南暗示 |

---

## 3. P0：工程化改造（不写业务代码，先让项目能编译）

> 当前实测：用本地 Gradle 8.8 以 `--offline` 配置本项目，7 秒内失败——`Plugin net.neoforged.moddev:2.0.148 was not found`；`maven.neoforged.net` 连接超时。必须先把构建链对齐到本机已有缓存。

### 3.1 版本决策

| 项 | 选用 | 理由 |
|---|---|---|
| ModDevGradle | **2.0.146** | 本机 Gradle 缓存已有；2.0.148 未缓存且 `maven.neoforged.net` 不可达 |
| NeoForge | **21.1.249** | 本机缓存 + 隔壁 scguns 移植已验证；合并 jar 在 `E:\mod\scgun-0.5.5-1.21.1-neoforge\build\moddev\artifacts\neoforge-21.1.249-merged.jar` |
| Gradle | **8.8**（`gradle-8.8-bin` 已完整缓存） | 9.2.1 的 wrapper 下载只有 `.part`，且 `services.gradle.org` 的 TLS 校验在本机报 `CRYPT_E_REVOCATION_OFFLINE` |
| JDK | **`E:\mod\SG2-1.21\jdk21\jdk-21.0.11+10`** | 唯一可用的完整 JDK 21（PATH 上的 `java` 是 1.8、`javac` 是 17） |

### 3.2 逐步清单

- [ ] **`gradle.properties`**：身份信息改成 scgextra

  ```properties
  org.gradle.jvmargs=-Xmx3G
  org.gradle.java.home=E\:\\mod\\SG2-1.21\\jdk21\\jdk-21.0.11+10

  minecraft_version=1.21.1
  minecraft_version_range=[1.21.1]
  neo_version=21.1.249
  loader_version_range=[1,)

  mod_id=scgextra
  mod_name=SCG Extra
  mod_license=GPL-3.0-or-later
  # 建议沿用隔壁 scguns 移植的版本策略：本移植版 1.0.0 + 上游 4.0.0
  mod_version=1.0.0+4.0.0
  mod_group_id=net.zincstudios.scgextra
  ```

- [ ] **`build.gradle`**：插件版本 `2.0.146`，依赖接 `libs/`（当前 `dependencies {}` 是空的）

  ```groovy
  dependencies {
      implementation files('libs/framework-neoforge-1.21.1-0.13.11.jar')
      implementation files('libs/geckolib-neoforge-1.21.1-4.9.3.jar')
      implementation files('libs/curios-neoforge-9.5.1+1.21.1.jar')
      implementation files('libs/TerraBlender-neoforge-1.21.1-4.1.0.8.jar')
      implementation files('libs/scguns-1.1.0hotfix.jar')
  }
  ```

- [ ] **Gradle wrapper**：`distributionUrl` 改 `https://mirrors.tencent.com/gradle/gradle-8.8-bin.zip`（与隔壁 scguns 移植一致），或直接用本地已下载的 8.8。
- [ ] **`neoforge.mods.toml`**：填 scguns / framework / geckolib / curios / terrablender 依赖，加 `[[mixins]] config="scgextra.mixins.json"`；**不需要** `[[accessTransformers]]`（原 AT 文件是 0 字节）。
- [ ] **`scgextra.mixins.json`**：从原工程复制，`compatibilityLevel` 改 **`JAVA_21`**，**删掉 `refmap` 字段**（NeoForge 1.21 运行时用官方名，隔壁 scguns 的 mixin 配置同样没有 refmap）。
- [ ] **删除模板残留**：`com/example/examplemod/*`、`assets/examplemod/`、模板说明文字。
- [ ] **补 `LICENSE`（GPL-3.0）与 `CREDITS.txt`**：目标目录现在两个都没有；GPL 合规是硬要求。
- [ ] **README**：写明“移植自 SCG Extra（GPL-3.0）”，保留原作者署名（Zinc Studios、Daragetsu、Krei、Funado、Nope）。
- [ ] **验收**：`gradlew build` 产出 `build/libs/scgextra-1.0.0+4.0.0.jar`；`gradlew runClient` 能进主菜单；jar 内 `META-INF/neoforge.mods.toml` 的 modId 是 `scgextra`。

---

## 4. 分阶段实施清单

> 顺序原则：**先注册层 → 再实体骨架 → 再内容 → 再渲染 → 最后世界生成**。每个阶段都要求“可编译 + 可进游戏”，避免一次性搬 480 个文件后面对上千条编译错误。

### 阶段 1：注册层（约 75 个文件，约 2,300 行）

**文件**：`item/ModItems`(581)、`item/armor/ModArmorMaterials`(204)、`block/ModBlocks`、`block/ModBlockEntities`、`sounds/*`(11)、`effects/ModEffects`、`particle/ModParticleTypes`、`attributes/SCGEAttributes`、`CommonConfig`(217)、`SCGExtra`(165)。

- [ ] `DeferredRegister.create(ForgeRegistries.X, id)` → `DeferredRegister.create(Registries.X, id)`；物品/方块用 `DeferredRegister.Items` / `DeferredRegister.Blocks`。
- [ ] `RegistryObject<T>` → `DeferredHolder<T,T>` / `DeferredItem<T>` / `DeferredBlock<T>`（26 处）。
- [ ] 刷怪蛋：`ForgeSpawnEggItem` → `DeferredSpawnEggItem`（仅 `ModItems` 1 处）。
- [ ] 创造标签：源码**已是** `CreativeModeTab` + `BuildCreativeModeTabContentsEvent`，只需换事件包名（`net.neoforged.neoforge.event.*`）与 `RegistryObject` → `DeferredItem`；语言键 `itemGroup.scgextra.tab` 保留。
- [ ] `ForgeConfigSpec` → `ModConfigSpec`；`CommonConfig` 的静态字段被群系/袭击代码直接读取，**先保持字段名不变**，避免连锁改动。
- [ ] 主类构造器改 `(IEventBus modEventBus, ModContainer modContainer)`；`MinecraftForge.EVENT_BUS.addListener` → `NeoForge.EVENT_BUS.addListener`；`Bus.FORGE` → `Bus.GAME`。
- [ ] `SCGEPacketHandler.init()` 暂时空实现（阶段 6 再补），先让主类能加载。
- [ ] **验收**：进游戏，创造模式能刷出全部物品/方块/刷怪蛋；`/give` 不崩；配置能生成。

### 阶段 2：实体公共框架（`entity` 根 8 + `entity/common` 非 client/brain 41，共 49 个文件）

**核心文件**：`ModEntities`(26 注册 + 各派系注册共 82)、`EntityAdjustments`、`Faction`、`EntityTypeTags`、`AbilityState`、`BrainActivities/Memories/Sensors`、`Gunner`、`GunnerEntity`、`EquippedEntity`、`Stunnable`、`WeakPointBox`、`HeadShotHandler`、`MobUtil`、`common/part/*`(6)。

- [ ] `RegistryObject` → `DeferredHolder`；`DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ...)` → `Registries.ENTITY_TYPE`。
- [ ] `EntityType.Builder.build(String)` 的调用点按 1.21 编译错误调整（部分版本要求 `ResourceKey`）。
- [ ] `defineSynchedData()` → `defineSynchedData(SynchedEntityData.Builder builder)`（45 个文件，集中在实体里）。
- [ ] `PartEntity` 换包：`net.neoforged.neoforge.entity.PartEntity`；多部件实体的碰撞箱在赋值后调用 `refreshDimensions()`（隔壁 scguns 移植踩过）。
- [ ] `EntityAttributeCreationEvent` 保留；`SpawnPlacementRegisterEvent` → `RegisterSpawnPlacementsEvent`（3 个文件）。
- [ ] `Brain`/`MemoryModuleType`/`SensorType` 的注册方式按 1.21 核对（`ModBrainActivities` 等）。
- [ ] **验收**：至少 1 个普通实体 + 1 个多部件实体能生成、有属性、能被打。

### 阶段 3：AI 与枪械模拟（`entity/common/brain` 35 + `goal` 10 + `gun` 14）

- [ ] `BehaviorControl`/`Sensor` 泛型与 `Brain#setMemory` 的 1.21 签名核对。
- [ ] `Gun`/`GunItem` 调用点：`gunItem.getModifiedGun(stack)`、`Gun.getAmmoCount(stack)`（静态）、`GunModifierHelper.getModifiedRate/getMuzzleFlashScale`、`ModSyncedDataKeys`——实测均保留。
- [ ] `SimulatedGun`/`ScorchedSimGun` 的弹道模拟：不要自己实现弹道，继续走 scguns 的 `ProjectileEntity`/`ProjectileFactory`。
- [ ] **验收**：测试生物会持枪、瞄准、开火、换弹；子弹伤害走 scguns 的弹丸。

### 阶段 4：实体内容（255 个文件 / 9 个派系）

| 派系 | 文件 | 实体类 | 客户端 | 行 | 备注 |
|---|---:|---:|---:|---:|---|
| `whaler` | 38 | 9 | 7 | 4,602 | 含装甲鲸（PartEntity）与守护者雕像（Mixin 生成） |
| `piglin` | 38 | 8 | 4 | 4,677 | 含骑乘/飞行单位 |
| `neutral` | 35 | 13 | 4 | 4,865 | 末地/下界/主世界 |
| `cog` | 33 | 10 | 7 | 3,971 | 多部件巨型单位集中 |
| `fac` | 28 | 11 | 4 | 3,446 | 联合军 |
| `asgharian` | 27 | 6 | 1 | 3,132 | 含灵魂撕裂者 |
| `rrc` | 23 | 10 | 2 | 3,320 | |
| `wreckers` | 20 | 8 | 4 | 2,718 | 含推土机/直升机 |
| `projectile` | 10 | 4 | 3 | 645 | 自定义弹丸 |
| `turret` | 3 | 1 | 0 | 186 | 炮台座位 |

- [ ] 按派系分批：每批 = 实体类 + 渲染器 + 刷怪蛋 + 掉落表（loot 在资源里）。
- [ ] 每批完成后跑一次 `runClient` 刷怪测试，不要等 255 个文件全部改完。
- [ ] **验收**：9 个派系的代表生物都能生成、有模型、能战斗、能掉落。

### 阶段 5：客户端渲染（51 个客户端文件 + 5 个渲染器重写）

- [ ] **先修 GeckoLib 签名**：5 个文件
  - `entity/cog/juggernaut/CogJuggernautRenderer`(81)
  - `entity/neutral/end/end_scorpion/EndScorpionRenderer`(60)
  - `entity/piglin/piglin_hog_rider/PiglinHogRiderRenderer`(58)
  - `entity/piglin/piglin_kiln_hog/PiglinKilnHogRenderer`(34)
  - `entity/whaler/guardian_statue/GuardianStatueRenderer`(261)
  
  改法：`renderRecursively(..., int packedLight, int packedOverlay, int color)`，原来拆开的 `red/green/blue/alpha` 用 `FastColor`/`ARGB` 合成一个 `int`。
- [ ] `IClientItemExtensions` 换包（8 处）。
- [ ] `GeoLayer`/`GeoModel`/`AnimationController` 的 4.9.3 调整按编译错误逐个修。
- [ ] 实体渲染器注册（`EntityRenderers.register` / `EntityRenderersEvent`）。
- [ ] **验收**：模型、动画、持枪姿势、枪口火焰、第一/第三人称都正常。

### 阶段 6：网络与输入（4 个网络文件 + 2 个 client 文件）

- [ ] 重写 `SCGEPacketHandler`：`FrameworkAPI.createNetworkBuilder(id, 1)` + 新的 `registerPlayMessage(String, Class, StreamCodec, BiConsumer<T, MessageContext>)`。
- [ ] 3 个消息类（`GunFlashMessage`、`HoldAttackMessage`、`TurretTriggerMessage`）补 `StreamCodec` 与 handler；**不要在网络包里持有活 `ItemStack`**（先 `copy()`）。
- [ ] `client/TurretInputHandler`、`client/ScreenRenderer` 按 1.21 输入/渲染事件调整。
- [ ] **验收**：开枪的枪口闪光能同步给附近玩家；长按攻击/炮台触发正常。

### 阶段 7：袭击 / 数据 / 命令（`raid` 5 + `data` 1 + `debug` 2 + 12 个 Mixin）

- [ ] `WaveRaidManager`(205)/`WaveRaidState`(219)/`WaveRaidData`(209)/`WaveRaidUtil`(132) 的 `ServerLevel`/`SavedData`/`BossEvent` API 核对。
- [ ] `RaidDataLoader`（reload listener）按 1.21 `AddReloadListenerEvent` 调整。
- [ ] `/scgextra` 命令（`DevTestCommands` + `Mixin` 里的 `executeStopAllRaids`）按 1.21 `CommandSourceStack` 调整。
- [ ] 12 个 scguns Mixin 逐个启用：先全部注释，跑通启动，再按 §5.1 顺序放开，每次 `runClient` 验证。
- [ ] **验收**：信号弹能触发自定义袭击；波次推进、Boss 血条、`/scgextra` 命令可用。

### 阶段 8：世界生成（16 个文件 + 311 个 data 资源）

- [ ] `ModBiomes`：1.21.1 仍可用代码注册，但建议按指南改数据驱动（`data/scgextra/worldgen/biome/warzone.json`）；`ModOverworldRegion` 的 `Region` 实测 API 未变。
- [ ] `ModSurfaceRules`：`SurfaceRuleManager.addSurfaceRules` 签名未变，直接迁。
- [ ] `ModStructures`：`StructureType` 的 Codec 注册；5 个结构类（`CampingSite`/`DestroyedMines`/`MineTrench`/`TrenchesAnchor`/`WaterAvoiding`）按 1.21 `Structure`/`StructurePiece` 调整。
- [ ] 结构 NBT：**133 个 `.nbt`** 的目录与引用路径；`data/scgextra/worldgen/structure/*.json` 的 `type` 与 `biomes` 标签要重写。
- [ ] `ModConfiguredFeatures`/`ModPlacedFeatures`/`WarzoneSpruceTreeGrower`。
- [ ] **验收**：`/locate structure scgextra:...` 能找到；`/execute in` 到 warzone 群系能看到地形/树/结构。

### 阶段 9：datagen、资源、语言、许可（8 个 provider + 1,173 assets + 561 语言键）

- [ ] 8 个 provider 全部重写：`DataGenerators`(41)、`ModEntityLootTableProvider`(488)、`ModRecipeProvider`(504)、`ModEntityTagProvider`(244)、`ModItemTagProvider`(44)、`ModItemModelProvider`(211)、`RaidLootSubProvider`(66)、`ModWorldGenProvider`(25)。
- [ ] 输出目录保持 `src/generated/resources`；1.21 的 `PackOutput`/`RegistrySetBuilder`/`HolderLookup.Provider` 全套新 API。
- [ ] 资源迁移：`assets/scgextra` 1,173 个（sounds 646、textures 322、geo 91、animations 79、models 27、particles 3、blockstates 3、lang 1，外加根目录 `sounds.json`）；`data/` 311 个（scgextra 258、scguns 41、betteroceanmonuments 6、betterfortresses 2、minecraft 4）。
- [ ] 中文语言包：`E:\mod\汉化\scgextra汉化资源包-4.0.0`（**561 键、JSON 合法**）直接放入 `assets/scgextra/lang/zh_cn.json`；`en_us.json` 目前 556 键，补 5 个缺失键或删 5 个多余键。
- [ ] 许可与元数据：`LICENSE`、`CREDITS.txt`、README 移植声明、`mods.toml` 里的 license/authors/credits。
- [ ] **验收**：`gradlew build` 的 jar 内资源完整；`gradlew runData` 无 diff；中文全部生效；jar 可被 NeoForge 21.1.249 加载。

---

## 5. 49 个“重写”文件明细

### 5.1 Mixin（16 个，12 个注入 scguns）

| 文件 | 目标类 | 注入成员 | 1.1.0 成员存在 | 说明 |
|---|---|---|---|---|
| `AIGunEventMixin` | `AIGunEvent` | `performGunAttack` | ✅ 5 参签名一致 | MixinExtras `WrapOperation` 改 `ProjectileEntity.setPos`，处理枪口偏移 |
| `GunAttackGoalAccessor` | `GunAttackGoal` | 4 个 `@Accessor` 字段 | ✅ | 读取瞄准稳定/换弹/连发/攻击计时 |
| `GunnerMobSpawnerMixin` | `GunnerMobSpawner` | `hasGunAttackGoal` | ✅ | 让 `Gunner` 也算持枪 AI |
| `LightningProjectileEntityMixin` | `LightningProjectileEntity` | `onHitEntity`/`onHitBlock` | ✅ | |
| `MineUnitBlockEntityMixin` | `MineUnitBlockEntity` | `triggerGrenade` | ✅ | 用 ArmorStand 替换控制者，规避空指针 |
| `ModCommandsMixin` | `ModCommands` | `executeStopAllRaids` | ✅ | 停止袭击时同步清自定义波次 |
| `ProjectileEntityMixin` | `ProjectileEntity` | 6 参构造器、`getCriticalDamage`、`onHit`、`getHitResult` | ✅ 构造器描述符一致 | 171 行，最复杂的 Mixin |
| `RaidFlareEntityMixin` | `RaidFlareEntity` | `performBurst` | ✅ | 信号弹触发自定义袭击 |
| `RaidManagerMixin` | `RaidManager` | `hasActiveRaid`/`surrenderRaid`/`INSTANCES` | ✅ | |
| `RaidSaveDataAccessor` | `RaidSaveData` | 3 个 `@Accessor` 字段 | ✅ | |
| `RecoilHandlerMixin` | `RecoilHandler` | `onGunFire`/`onRenderOverlay` | ✅ | 客户端，后坐力 |
| `SignalBeaconEntityMixin` | `SignalBeaconEntity` | `spawnSkyCarriers` | ✅ | 替换天空单位生成 |
| `EntityMixin` | 原版 `Entity` | `dampensVibrations` | ✅ | Enlightenment 属性免振动 |
| `LivingEntityMixin` | 原版 `LivingEntity` | `getDamageAfterMagicAbsorb`/`checkTotemDeathProtection` | ✅（后者 private） | 123 行，伤害/亡灵/图腾逻辑 |
| `OceanMonumentEntryRoomMixin` | 原版 `MonumentBuilding` | `generateWing` | ✅（private） | 海底神殿放守护者雕像 |
| `ResourceOrTagKeyArgumentMixin` | 原版 `ResourceOrTagKeyArgument` | `listSuggestions` | ✅ | 过滤 `scgextra:craters` 建议 |

**写法要点**：`remap = false` 的 scguns 目标全部保留；MixinExtras 由 NeoForge 21.1.249 以 jar-in-jar 内置（实测 `mixinextras-neoforge-0.5.3`，原工程用的是 0.4.1，`WrapOperation/WrapMethod/@Local` 兼容），**不必自己 jarJar**；`compatibilityLevel` 改 `JAVA_21`，删 `refmap`。

### 5.2 网络（4 个，必须重写）

| 文件 | 行 | 方向 | 重写要点 |
|---|---:|---|---|
| `SCGEPacketHandler` | 37 | — | 新 builder API + `StreamCodec` 注册 |
| `GunFlashMessage` | 61 | S2C | 枪口闪光同步；不持活 `ItemStack` |
| `HoldAttackMessage` | 38 | C2S | 长按攻击状态 |
| `TurretTriggerMessage` | 43 | C2S | 炮台触发 |

### 5.3 datagen（8 个，全部重写）

`DataGenerators`(41)、`ModEntityLootTableProvider`(488)、`ModRecipeProvider`(504)、`ModEntityTagProvider`(244)、`ModItemTagProvider`(44)、`ModItemModelProvider`(211)、`RaidLootSubProvider`(66)、`ModWorldGenProvider`(25)。1.21 的 `DataProvider`/`PackOutput`/`RegistrySetBuilder` 与 1.20.1 差异大，建议直接以 1.21.1 新模板重写，不要逐行改。

### 5.4 世界生成（16 个，全部重写）

`ModBiomes`、`ModOverworldRegion`、`ModTerrablender`、`ModSurfaceRules`、`ModStructures`、`ModStructureProcessors`、`CogFactoryProcessor`、`WarshipProcessor`、5 个 `*Structure`、`ModConfiguredFeatures`、`ModPlacedFeatures`、`WarzoneSpruceTreeGrower`。API 本身（TerraBlender）没变，重写的是数据与 `StructureType`/`StructurePiece` 的 1.21 模型。

### 5.5 渲染器（5 个，GeckoLib 签名）

见阶段 5。`GuardianStatueRenderer`(261) 最大，`CogJuggernautRenderer` 也带枪口火焰逻辑，建议先改这两个验证签名，再批量改另外三个。

---

## 6. 高风险清单与对策

| # | 风险 | 触发点 | 对策 |
|---|---|---|---|
| 1 | **Mixin 运行期不匹配** | 12 个 scguns 注入 | 成员已逐个验证存在；仍要“先注释、逐个放开”，每次启动验证；优先用 `@WrapMethod`/`WrapOperation` 而非 `@Inject` 到具体行号 |
| 2 | **网络层重写** | 4 个文件 | 以隔壁 `scgun-0.5.5-1.21.1-neoforge` 的 `PacketHandler`/`FrameworkMessageBridge` 为参考实现 |
| 3 | **45 个实体同步数据** | `defineSynchedData` | 一次性全局替换为 `Builder` 版本，编译错误会集中在少数几个方法 |
| 4 | **GeckoLib 14→11 参** | 5 个渲染器 | 先改签名，颜色用 `FastColor.ARGB32` 合成；模型类大多不用动 |
| 5 | **PartEntity 多部件** | 27 个文件 | 换包 + `refreshDimensions()`；碰撞箱偏移逐个体检 |
| 6 | **1.21 数据组件** | 32 个文件用 `CompoundTag` | 键名保持原样，用 `DataComponents.CUSTOM_DATA` + `CustomData`，保证与 scguns 互通 |
| 7 | **结构 NBT 133 个** | `data/scgextra/worldgen/structure` | 1.20 NBT 结构在 1.21 基本可读，但 `structure` JSON 的 `type`/`biomes`/`step` 要按 1.21 重写 |
| 8 | **离线构建** | `maven.neoforged.net` 超时 | 固定 21.1.249 + 2.0.146；任何升级前先确认缓存；不要依赖 Gradle 插件门户 |
| 9 | **许可合规** | 发布 jar | GPL-3.0 源码 + LICENSE + CREDITS + README 声明，缺一不可 |
| 10 | **刷怪蛋与物品总表** | `ModItems` 581 行 | `ForgeSpawnEggItem` 已删除，换 `DeferredSpawnEggItem`；`ModItems` 是所有物品的注册总表，改错会连锁到实体/方块 |

---

## 7. 提交前验收

- [ ] `gradlew build` 在本机离线可重复成功，产物 `build/libs/scgextra-1.0.0+4.0.0.jar`。
- [ ] jar 内：`META-INF/neoforge.mods.toml`（modId=scgextra）、`scgextra.mixins.json`、`LICENSE`、`CREDITS.txt`。
- [ ] 无 `com/example/examplemod`、无 `assets/examplemod` 残留。
- [ ] `grep -r "net.minecraftforge" src` 为空。
- [ ] `grep -r "examplemod" .` 只剩模板说明或为空。
- [ ] 9 个派系代表生物 + 82 个实体注册全部可生成（用刷怪蛋逐个点）。
- [ ] 12 个 Mixin 全部启用后能正常启动、开枪、触发袭击。
- [ ] 中文 561 键全部生效，`en_us` 与 `zh_cn` 键集一致。
- [ ] 在 NeoForge 21.1.249 纯净客户端 + scguns 1.1.0 + Framework 0.13.11 + GeckoLib 4.9.3 + Curios 9.5.1 + TerraBlender 4.1.0.8 下能进世界。

---

## 8. 环境决策记录（下次接手先看）

| 事实 | 值 |
|---|---|
| 本机 PATH 上的 Java | `java` 1.8.0_411，`javac` 17.0.10（**都不是 21**） |
| 可用 JDK 21 | `E:\mod\SG2-1.21\jdk21\jdk-21.0.11+10`（javac 21.0.11） |
| 完整缓存的 Gradle | `~/.gradle/wrapper/dists/gradle-8.8-bin/...`（8.5/8.12/8.14/9.2.1 都只有 `.part`） |
| 缓存的 NeoForge | `21.1.249`；ModDevGradle `2.0.146`（`2.0.148` 没有） |
| `maven.neoforged.net` | 连接超时（2026-11-13 实测） |
| 相邻参考工程 | `E:\mod\scgun-0.5.5-1.21.1-neoforge`（已构建成功，含 merged jar 与网络/工具链实现） |
| 源工程是否 git 仓库 | 否（zip 解压） |
| 目标工程是否 git 仓库 | 否 |

> 如果之后网络恢复，可再评估升级到 NeoForge 21.1.265 / ModDevGradle 2.0.148；在没有网络的前提下，**不要动版本号**，否则会立刻退回“插件找不到”的状态。
