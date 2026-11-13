# scg-extra：1.20.1 / Forge → 1.21.1 / NeoForge 移植注意事项

> 给接手移植的智能体。原文：`E:\mod\scg-extra-main\scg-extra-main`（**480 个 Java 文件 / 41,938 行 / 1,488 个资源**）。
> 目标环境：**1.21.1 + NeoForge 21.1.x + Java 21**。
>
> **重要：本模组不新增任何枪械。** 它只使用 scguns 已有的枪与弹道，新增的是生物、装备、物品、方块、袭击与群系。
> 因此不存在“把枪做成数据包”的捷径 —— 真正要移植的就是这 480 个 Java 文件。

## 0. 许可（先读）

本仓库自带 **GNU GPL-3.0** 的 `LICENSE`，因此**可以自由移植**，但产物必须：

- 同样以 **GPL-3.0** 发布，**公开源码**；
- 保留原 `LICENSE`、`CREDITS.txt` 与作者署名；
- 在 README 注明“移植自 scg-extra（GPL-3.0）”。

> 发布出去的 jar 里没有许可声明，看起来像 All Rights Reserved —— 那是打包疏漏，**源码许可优先**。

## 1. 依赖与版本

| 依赖 | 1.20.1 | 1.21.1 对应 |
|---|---|---|
| Forge | 47.4.x | **NeoForge 21.1.150+** |
| scguns | `[0.5.5,)`（原版） | **本移植版 `scguns` 1.1.x**（见第 3 节） |
| TerraBlender | `[3.0.1,)` | 1.21.1 分支（API 有变动，见第 4 节） |
| GeckoLib | 4.8.4 | **4.9.3+**（`renderRecursively` 签名变了，见第 5 节） |
| Curios | 5.14.1 | 9.5.1+（`ICurio` → `ICuriosItemHandler` 路径调整） |
| 可选 | Better Ocean Monuments / Better Fortresses | 1.21.1 对应版本，或先注释掉 |

## 2. Forge → NeoForge 的机械改动

1. **包名**：`net.minecraftforge.*` → `net.neoforged.*`
   - `net.minecraftforge.eventbus.api` → `net.neoforged.bus.api`
   - `net.minecraftforge.registries.*` → `net.neoforged.neoforge.registries.*`
   - `net.minecraftforge.items.IItemHandler` → `net.neoforged.neoforge.items.IItemHandler`
   - `net.minecraftforge.api.distmarker.Dist` → `net.neoforged.api.distmarker.Dist`
2. **事件总线**：`@Mod.EventBusSubscriber(bus = Bus.MOD / Bus.FORGE)` → `bus = Bus.MOD / Bus.GAME`。
3. **注册**：`DeferredRegister.create(ForgeRegistries.X, id)` → `DeferredRegister.create(Registries.X, id)` / `DeferredRegister.Items` / `DeferredRegister.Blocks`。
4. **物品属性**：`new Item.Properties().tab(...)` 已删除 → 用**创造模式标签**的 `CreativeModeTab` 注册 + `BuildCreativeModeTabContentsEvent`（原来的 `itemGroup.scgextra.tab` 要在标签事件里填充）。
5. **Capability**：`IItemHandler` 等不再用 `Capability`/`LazyOptional` → **`RegisterCapabilitiesEvent` + BlockEntity 直接持有 `ItemStackHandler`**；`stack.getCapability(...)` 改成 `stack.getCapability(Capabilities.ItemHandler.ITEM)`。
6. **数据组件**：NBT 直读 → `DataComponents.CUSTOM_DATA` + `CustomData`（**键名保持原样**，方便与 scguns 互通）。
7. **网络**：1.21.1 用 `StreamCodec` / `CustomPacketPayload`；`SimpleChannel` 已不可用。若原代码用 `NetworkHooks.openScreen`，改为 `player.openMenu(...)` + `RegistryFriendlyByteBuf`。
8. **`mods.toml`**：改名 `META-INF/neoforge.mods.toml`，`modLoader="javafml"` 保留，`loaderVersion="[4,)"`；`[[dependencies.x]]` 里的 `mandatory` 改 `type="required"/"optional"`。
9. **Gradle**：`net.minecraftforge.gradle` → **ModDevGradle**（`net.neoforged.moddev`），`minecraft_version=1.21.1`、`neo_version=21.1.xxx`。
10. **Mixin**：`compatibilityLevel` 改 **`JAVA_21`**；`mods.toml`/`neoforge.mods.toml` 里 `[[mixins]] config="..."`。

## 3. 与 scguns（本移植版）的接口差异

> 注意：scg-extra **不注册任何 `GunItem`**，也不带枪械 JSON。它只是**调用** scguns 的类来做“持枪生物”和弹药/后坐力等交互，
> 所以这里关心的是**调用签名**是否还成立，而不是数据格式。

原代码大量直接调用 `top.ribs.scguns.*`。本移植版**包名保持一致**，但下列成员有变化：

| 原用法 | 1.21.1 移植版 |
|---|---|
| `GunItem.getModifiedGun(stack)` | **同名保留** ✓ |
| `Gun.General.getFireMode()` / `getRate()` | 保留 ✓ |
| `ProjectileEntity` / `Gun.Projectile` | 保留 ✓，但 `ProjectileEntity#createExplosion/createRocketExplosion` 内部**已修**（见下） |
| `ModItems.*` / `ModSounds.*` / `ModTags.*` | 保留 ✓（个别名字随 1.21 数据化改动，需编译期核对） |
| `IAttachment.Type` | 保留 ✓ |
| `GunModifierHelper.getModifiedRate(...)` 等 | 保留 ✓ |
| `ModSyncedDataKeys.*`（Framework 的 `SyncedDataKey`） | 保留 ✓（Framework 1.21.1 版本） |
| `Gun.getAmmoCount(stack)` | 保留 ✓（读 `CustomData` 的 `AmmoCount`） |

**特别注意**：本移植版**已经修掉**以下上游 bug，scg-extra 不要重复实现：

1. `RocketExplosion` 未把方块集合回填到 `getToBlow()`（导致火箭炸不坏方块）；
2. 高爆榴弹 `createHeGrenadeExplosion` 完全没有爆炸对象（不破坏方块）；
3. 火箭半径原为 7.0（已改 4.0）；
4. 弹丸碰撞箱不刷新（`refreshDimensions()` 缺失）；
5. 近战包 `S2CMessageMeleeAttack` 持有活 `ItemStack`（换弹时并发崩服）。

## 4. TerraBlender / 群系

- 1.21.1 的 TerraBlender 用 `Region` / `SurfaceRuleManager` 注册，**`BiomeProvider` 构造方式与 1.20.1 不同**；
- `warzone` 群系的注册要从 `BiomeRegistry` 迁到 **`data/<ns>/worldgen/biome/*.json` 数据驱动**（推荐），Java 只留 `TerraBlender.registerBiomes` 的注入点；
- `biome.scgextra.warzone_biome` 语言键保留即可。

## 5. GeckoLib 4.8 → 4.9+

- `GeoModel#getTextureResource(T)` / `getModelResource(T)` 保留；
- **`renderRecursively(PoseStack, T, GeoBone, RenderType, MultiBufferSource, VertexConsumer, boolean, float, int, int, float)` 增加了两个 float 参数**（`red, green, blue, alpha` 合成一个 ARGB 或拆开，取决于 4.9 小版本）——按编译错误对齐；
- `AnimationController` 的 `registerSound`/`triggerableAnim` API 有调整；
- 枪械模型类（`*Model implements IOverrideModel`）要确认本移植版 `IOverrideModel#render` 的签名**没有变**（应该没变）。

## 6. 我们踩过、你大概率也会踩的 1.21.1 坑

| 坑 | 表现 | 解决 |
|---|---|---|
| `LivingEntity#getCooldowns()` | 编译错：只有 `Player` 有 | 用 `player.getCooldowns()` |
| `Player#crit` | 以为是字段 | 是方法 `player.crit(entity)` / `magicCrit(entity)` |
| `AbstractArrow#setKnockback(int)` | 不存在 | Punch 在 `doKnockback` 里从 `firedFromWeapon` 读取 |
| `EnchantmentHelper.modifyDamage(...)` | 需要 `ServerLevel` | 传 `player.serverLevel()` |
| 附魔书读附魔 | `EnchantmentHelper.getEnchantments` 变了 | `ItemEnchantments` 走 `DataComponents.STORED_ENCHANTMENTS` |
| 自定义生成数据 | `IEntityWithComplexSpawn#writeSpawnData` | 参数是 **`RegistryFriendlyByteBuf`** |
| 模型/贴图路径 | 资源包加载不到 | `geo` 与 `textures/animated` 必须放 **`assets/scguns/`** 下（按 gun id 硬拼路径） |
| `ItemStack` 放进网络包 | 崩服 `ConcurrentModificationException` | **先 `copy()`**，别持活引用 |
| 实体碰撞箱 | 改 `EntityDimensions` 无效 | 赋值后必须 `refreshDimensions()` |

## 7. 建议的分期

| 阶段 | 目标 | 验收 |
|---|---|---|
| **1** | 工程能编译、能进游戏，**先只注册物品与方块**（实体/→ 群系先注释） | 创造模式能刷出全部物品，无崩溃 |
| **2** | 实体注册 + GeckoLib 模型 + 装备/掉落（**无枪械**） | 生物能生成、能持 scguns 的枪开火、装备能掉落 |
| **3** | 袭击 / → 群系 / → 结构 / → 战利品 | 突袭能触发、群系能进、掉落正常 |

## 8. 提交前自检清单

- [ ] `LICENSE` 与 `CREDITS.txt` 完整，README 写明移植自 scg-extra（GPL-3.0）
- [ ] `neoforge.mods.toml` 里 `scguns` 依赖改成 `[1.0.0,)` 之类**能匹配本移植版**的范围（原 `[0.5.5,)` 也能满足，但语义不清）
- [ ] 所有 `net.minecraftforge.*` 引用清零（`grep -r "net.minecraftforge" src` 应为空）
- [ ] 资源与数据 JSON 的 `namespace` 全部对（`assets/scguns/...` vs `assets/scgextra/...`）
- [ ] 生物持枪逻辑走的是 scguns 的 `GunItem` / `ProjectileEntity`，没有自己实现一套弹道
- [ ] 中文语言文件沿用现成的 `scgextra` 资源包（**561 条，已修完重名与术语**）
