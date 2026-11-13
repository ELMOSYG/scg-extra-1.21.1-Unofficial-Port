# SCG Extra — 1.21.1 / NeoForge 非官方移植版

[SCG Extra](https://www.curseforge.com/minecraft/mc-mods/scg-extra) 4.0.0 的非官方 **Minecraft 1.21.1 / NeoForge** 移植。
原工程基于 Forge 1.20.1（47.4.x），作者 **Zinc Studios**（Daragetsu、Krei、Funado、Nope）。

> 本移植版按上游的 **GPL-3.0** 发布。原始 `LICENSE` 保留在仓库根目录；源码与二进制同样受 GPL-3.0 约束。

## 运行要求

| 依赖 | 版本 |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.249+（当前构建基线） |
| Java | 21 |
| Scorched Guns（scguns） | 1.1.0+ —— [ELMOSYG 的 0.5.5 1.21.1 非官方移植](https://github.com/ELMOSYG/scorched-guns-0.5.5-Unofficial-Port)，本仓库 `libs/scguns-1.1.0hotfix.jar` |
| Framework | 0.13.11+ |
| GeckoLib | 4.9.3+ |
| Curios | 9.5.1+ |
| TerraBlender | 4.1.0+ |

## 当前进度（阶段 1-9 全部完成）

- **工程**：NeoForge 21.1.249 / ModDevGradle 2.0.146 / Java 21，可离线 `gradlew build`；
- **注册层**：物品（含 71 个刷怪蛋）、护甲材料与 9 套 GeckoLib 护甲、武器、医疗物品、勋章、方块（铁丝网 / 电线 / 炮台 / 战争云杉树苗）、声音、状态效果、粒子、自定义属性、配置、创造标签；
- **实体**：8 个派系注册表共 **82 个生物** + 5 类弹丸 + `turret_seat`，含多部件 PartEntity、Boss、骑乘、持枪 AI；
- **AI 与枪械**：`entity/common` 的 brain / goal / gun 体系，瞄准状态经 `GunAttackGoalAccessor` 读取；
- **客户端渲染**：全部实体 / 物品 / 护甲 / 方块渲染器迁移到 GeckoLib 4.9（`renderRecursively(..., int color)`），详见 [docs/PHASE5-RENDER-AUDIT.md](docs/PHASE5-RENDER-AUDIT.md)；
- **网络**：Framework 0.13 `StreamCodec` 重写（枪口闪光 / 长按攻击 / 炮台扳机）；
- **袭击与 Mixin**：`WaveRaidManager`（`SavedData.Factory`）、数据包 reload、`/scgextra` 命令、16+ Mixin；
- **世界生成**：5 个结构、2 个 processor、TerraBlender 区域与表面规则、战争云杉；
- **数据**：上游 `src/generated/resources` 的 333 个产物已并入（群系、地物、战利品表、配方、进度、刷怪蛋模型），并完成 1.21 数据包单数化与 `result.id`、`neoforge:`/`c:` 迁移。

### 运行时修复记录

第一次实机测试后修复的问题（详见 [docs/RUNTIME-ISSUES.md](docs/RUNTIME-ISSUES.md)）：

- 建世界崩溃：`grass_patch` / `gravel_patch` 的 1.21 格式；
- 宝箱为空：战利品表 `loot_table` 引用的 `name` → `value`、`scute` → `turtle_scute`；
- 荧光层不可见：`reRender` 的 ARGB 颜色误写为 `1`；
- 后坐力为 0：`RecoilHandlerMixin` 裸乘属性值；
- 海底神殿崩溃：`GuardianStatueEntity` 在构造期读取 `entityData`；
- 结构生成：`bunker_trenches` 锚点、`leviathan_prismarine_gate` 水位检测、海洋结构漏水；
- 生物转向与滑步：`BodyRotationControl`、`state.isMoving()` 不可靠；
- 调试碰撞箱、地雷幽灵攻击者等。

### 已知限制

- 上游只提供 `en_us` 语言文件，本移植版未额外生成 `zh_cn`；
- 护甲的额外属性加成已改为 `ItemAttributeModifiers`；部分武器的"额外触及距离"仍未恢复；
- 12 个 GeckoLib 模型为上游的新格式（`1.21.0` / `1.21.110` 逐面 UV），GeckoLib 4.9.3 会提示不支持，但仍可渲染，个别面 UV 可能错位。

详见 [PORTING-CHECKLIST.md](PORTING-CHECKLIST.md) 与 [PORTING-MATRIX.md](PORTING-MATRIX.md)。

## 构建

```bash
./gradlew build     # 产物 build/libs/scgextra-<version>.jar
```

依赖全部 vendored 在 `libs/`，无需访问 Maven 仓库；JDK 21 通过 `JAVA_HOME` 指定即可。

## 许可

GNU General Public License v3.0，见 [LICENSE](LICENSE)。
本移植版保留上游作者署名，详见 [CREDITS.txt](CREDITS.txt)。
