# SCG Extra 4.1.0 同步记录（1.21.1 移植版）

上游 4.1.0（Forge 1.20.1）于 2026-11-22 发布。这里记录已同步和待同步内容。

## 已同步

| 4.1.0 变更 | 同步方式 |
|---|---|
| Trench no longer generate with mines | 用 4.1.0 的 125 个结构 NBT 覆盖本移植版 `data/scgextra/structure/`，矿车/地雷相关方块随之移除 |
| Trench water logging and mob suffocating | 同一批 NBT 更新 + 本移植版已有的 `LiquidSettings.IGNORE_WATERLOGGING` |
| Guardian statue spawn stacking | 结构实体的 `persist_mobs_processor`（4.1.0 新增），模板池全部引用 |
| 结构刷出的枪手被自然清除 | 新增 `MobPersistProcessor`，结构实体写入 `PersistenceRequired` |
| 战区动物生成 | 新增 `SpawnEventHandler#onCheckSpawn`，战区禁止 `Animal` 自然生成 |
| 破坏者 RPG / 破坏者·绿 炸掉落物 | `SpawnEventHandler#onExplosionDetonate` 从爆炸影响列表移除 `ItemEntity` |
| 结构刷怪持久化事件 | `SpawnEventHandler#onFinalizeSpawn` 对 12 类结构枪手 `setPersistenceRequired()` |
| 猪灵/猪灵蛮兵不追击袭击目标 | 新增 `MoveToRaidTargetGoal`，`WaveRaidData#handleRaiderEdgeCase` 对 `minecraft:piglin` / `piglin_brute` 注入 |
| 4.1.0 的模板池 processor | 复制 4.1.0 的 51 个模板池 JSON（保留本移植版自定义 `trenches/bunker/start_entry.json`） |

## 待同步

### 盔甲属性系统重构

4.1.0 重写了整套属性与护甲：

- 移除隐藏的"套装件数"属性（`oppressor`/`commissar`/...），改为直接扫描已穿护甲件数；
- 新增百分比属性：`recoil_reduction`、`bullet_damage_resistance`、`magic_damage_resistance`、`bullet_gravity`、`undead_damage_multiplier`、`bullet_additional_crit_chance`、`bullet_additional_crit_damage_multiplier`；
- `ArmorSet` 改为 `fullSetTraits + halfSetTraits + partModifiers`；
- `ArmorSetPartItem` 直接携带每件的 `partModifiers`，删除 `ArmorSetPartWithExtrasItem`；
- 新增 `PercentRenderedAttribute`（配合 AttributeSlib 显示百分比）；
- 新增 `AttributeHandler`，把子弹/魔法抗性、亡灵增伤移到 `LivingHurtEvent`；
- 护甲/勋章数值全部改成小数（0.1 = 10%）。

这部分会改动属性注册、所有护甲/勋章、`ModItems`、`RecoilHandlerMixin`、`ProjectileEntityMixin`、`LivingEntityMixin`、tooltip 和语言文件，属于需要实机回归的独立大改，暂未合入。

### 其他

- `Hog rider` / `Tentacliator` 刷怪蛋放进刷怪笼时的崩溃修复：4.1.0 未在实体类里体现明显的 `SPAWNER` 特判，需要结合崩溃日志再定位。
