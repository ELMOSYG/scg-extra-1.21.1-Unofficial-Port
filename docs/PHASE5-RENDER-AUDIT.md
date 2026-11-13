# 阶段 5：客户端渲染静态审计

日期：2026-11-13
范围：`src/main/java` 中全部渲染器 / 模型 / 客户端初始化代码，以及 `assets/scgextra` 的模型与贴图。
方法：源码扫描 + 资源存在性比对（编译已通过；运行时仍受离线限制，见文末）。

## 1. 结论

| 检查项 | 结果 |
|---|---|
| GeckoLib 4.9 `renderRecursively` 14 参残留 | **0**（12 处引用全部为 11 参新签名） |
| 旧顶点链 `vertex(...).endVertex()` 残留 | **0**（已全部改为 `addVertex/setColor/setUv/setOverlay/setLight/setNormal`） |
| `RenderUtils` 旧类名残留 | 0（统一为 `RenderUtil`） |
| 模型路径总数（实体 / 物品 / 护甲） | **75** |
| 缺失 `.geo.json` | **0** |
| 缺失基础贴图（含变体贴图 `_0.._7`） | **0** |
| `hasGlowLayer` 链 | **69**，对应 `_glowmask.png` 均存在（实体/护甲） |
| 缺失 `.animation.json` | 3 处，均为**无动画的静态模型**（`atlantic_mace`、`spear_shovel`、`whaler/net`），GeckoLib 允许缺省 |
| 实体渲染器注册 | 全部已注册（`EntityRenderers.register` / 方块实体渲染器） |
| 物品/护甲 `IClientItemExtensions` | 7 处 `initializeClient` 覆写，1.21 仍可用（已标记 deprecated for removal），后续可迁移到 `RegisterClientExtensionsEvent` |

## 2. 资源覆盖明细

- **实体模型**：`assets/scgextra/geo/entity/<path>.geo.json` 全部存在；贴图按变体（`fish_folk_0..3`、`tentacliator_1..2`、`ink_projectile_1..2`）存在，变体由渲染器 `getTextureResource` 选择。
- **物品模型**：`geo/item/*.geo.json` 与 `textures/item/*.png` 全部存在；`netherite_chain_axe`、`wrecker_rpg`、`wrecking_tool` 带动画，`atlantic_mace` / `spear_shovel` 为静态模型。
- **护甲模型**：9 套（oppressor / commissar / wrecker / leviathan / golden_idol / enlightened / juggernaut / ritual / pioneer）的 geo、贴图、glowmask 齐全；仅 `wrecker` 带动画文件。
- **方块模型**：`wrecker_turret` 的 `WreckerTurretBlockModel` 使用 `geo/block/...` 路径，已随资源迁移。

## 3. 本阶段处理的问题（编译期已修）

1. 5 个渲染器的 `renderRecursively` 由 14 参改为 11 参（`int color`），并在 `GuardianStatueRenderer` 中把 `alpha` 重新合成为 ARGB；
2. `PoseStack.Pose#setNormal()` → `normal()`；
3. `VertexConsumer` 全部迁移到 1.21 的 `addVertex/setColor/setUv/setOverlay/setLight/setNormal`；
4. `reRender(...)` 去除多余 3 个颜色分量；
5. 颜色从 `new float[]{...}` 改为 ARGB `int`（异常体/守护者光束）；
6. `BlockEntity` 渲染包围盒 `getRenderBoundingBox()` 已移除（1.21 无此扩展点），由默认 AABB 渲染。

## 4. 仍需运行时确认（离线阻塞）

`runClient` 仍缺 `neoforge-21.1.249-moddev-config.json`（只能从 `maven.neoforged.net` 下载）。联网后需要目视检查：

- [ ] 各派系模型朝向 / 缩放 / 骨骼命名是否与 1.20.1 一致；
- [ ] 持枪层（`HeldGunGeoLayer`、`GunGeoLayer`）的手部定位；
- [ ] `GuardianStatueRenderer` 光束与激光的 alpha 混合；
- [ ] `CogJuggernautRenderer` 枪口闪光的透明度；
- [ ] `hasGlowLayer` 在 4.9 下的发光强度；
- [ ] 9 套护甲在玩家模型上的贴合度；
- [ ] `wrecker_turret` 方块渲染（`ENTITYBLOCK_ANIMATED`）与乘坐视角。

## 5. 已知非阻塞项

- `initializeClient` 在 1.21 被标记 deprecated-for-removal，但 21.1.249 仍生效；若后续升级 NeoForge 需迁移到 `RegisterClientExtensionsEvent`。
- `PiglinKilnHogMagmaBall` / `NetEntity` 无动画文件，渲染为静态姿态。
