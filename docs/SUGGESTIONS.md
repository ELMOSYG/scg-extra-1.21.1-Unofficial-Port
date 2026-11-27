# 待处理建议（仅记录，未修改代码）

## 1. 猪灵主世界下界传送门结构增加出生点 512 格保护

**状态：** 待实现（2026-11-27 记录）

**问题**

`scgextra:piglin_mega_portal_overworld` 目前会在主世界任意位置生成，包括玩家出生点附近：

- 结构类型：`scgextra:water_avoiding_structure`
- 生物群系：`#minecraft:is_overworld`
- 当前没有任何“离世界出生点最小距离”的限制
- 出生点附近遇到时，前期玩家几乎没有还手能力，容易被猪灵/猪灵蛮兵追杀

**建议行为**

- 结构生成中心距离**世界出生点至少 512 格**（水平距离）
- 做成可配置项，例如：

```toml
# 猪灵主世界传送门离世界出生点的最小距离，0 = 不限制
piglinPortalMinSpawnDistance = 512
```

**实现思路（供以后动手时参考）**

1. 给该结构单独建一个类，例如 `PiglinPortalStructure extends WaterAvoidingStructure`，在 `findGenerationPoint` 里：
   - 取当前 chunk 中心
   - 取世界出生点
   - 若水平距离 < 配置值，直接 `Optional.empty()`
2. 出生点不在 `Structure.GenerationContext` 里，需要：
   - 服务端 worldgen 线程通过 `ServerLifecycleHooks.getCurrentServer()` 取 `overworld().getSharedSpawnPos()`；或
   - 用自定义 `StructurePlacement` / 保存的 world spawn 数据判断。
3. 结构 JSON 的 `type` 改成新结构类型；`start_pool`、`biomes`、`terrain_adaptation` 等保持不变。
4. 添加中英文配置说明。

**补充：猪灵窑炉兽的索敌距离**

前期被追杀的另一个直接原因是 `PiglinKilnHogEntity` 本身：

- `Attributes.FOLLOW_RANGE = 128.0`
- AI 使用 `ModBrainSensors.EXTREMELY_LONG_RANGE_PLAYER`，即 `VarRangePlayerSensor(60, 128)`
  - 每 60 tick 扫描一次
  - 玩家可见半径 128 格

也就是说，即使传送门结构离出生点有一段距离，窑炉兽也能从 128 格外发现玩家并开始追击。其他猪灵单位（猪灵贵族、烈焰旋翼机、疣猪骑士、猪灵蛮兵首领等）目前也都是 128 格。

可选的平衡方向（尚未修改）：

1. 只把窑炉兽改成 `FOLLOW_RANGE = 48/64` + `VERY_LONG_RANGE_PLAYER`（48 格）；
2. 把所有猪灵单位统一降到 48~64 格；
3. 保留 128 格，但只让传送门结构在出生点 512 格外生成；
4. 做成配置项，例如 `piglinAggroRange` / `piglinPortalMinSpawnDistance`。

**注意**

- 只对**新生成区块**生效；旧存档已生成的传送门不会消失。
- 若不想新增结构类，也可以先通过结构集（structure set）的 `spacing/separation` 降低整体生成率，但无法精确做到“出生点 512 格内不生成”。
