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

## 已同步：盔甲属性系统重构

- 新增 `PercentRenderedAttribute`：0.1 = 10%，装有 AttributeSlib 时使用其百分比文本。
- `SCGEAttributes` 重写为 7 个百分比属性：后坐力减免、子弹抗性、额外暴击率/暴击伤害、子弹重力、亡灵增伤、魔法抗性；旧套装计数属性与旧倍率属性移除。
- `ArmorSet` 重写：`fullSetTraits + halfSetTraits + partModifiers`，直接扫描已穿件数，不再使用隐藏计数属性。
- `ArmorSetPartItem` 直接携带每件 `partModifiers`；`ModItems` 36 件护甲改用 `ArmorSetPartItem`/`GeoArmorSetPartItem`。
- 新增 `AttributeHandler`：子弹/魔法抗性、亡灵增伤在 `LivingDamageEvent.Pre` 处理；旧 `LivingEntityMixin` 只保留仪式套复活。
- `RecoilHandlerMixin` 改用 `RECOIL_REDUCTION`，`ProjectileEntityMixin` 改用 `BULLET_GRAVITY` 与暴击属性。
- 九套护甲与九枚勋章数值按 4.1.0 全部重写；4.1.0 的 Enlightened / Leviathan 套装类补回。
- 中英文语言文件同步新属性名。

### 其他

- `Hog rider` / `Tentacliator` 刷怪蛋放进刷怪笼时的崩溃修复：4.1.0 未在实体类里体现明显的 `SPAWNER` 特判，需要结合崩溃日志再定位。
