# SCG Extra — 1.21.1 / NeoForge 移植版

[SCG Extra](https://www.curseforge.com/minecraft/mc-mods/scg-extra) 4.0.0 的非官方 **Minecraft 1.21.1 / NeoForge** 移植。
原工程基于 Forge 1.20.1（47.4.x），作者 **Zinc Studios**（Daragetsu、Krei、Funado、Nope）。

> 本移植版按上游的 **GPL-3.0** 发布。原始 `LICENSE` 保留在本仓库根目录；源码与二进制同样受 GPL-3.0 约束。

## 运行要求

| 依赖 | 版本 |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.249+（当前构建与测试基线） |
| Java | 21 |
| Scorched Guns（scguns） | 1.1.0+（本仓库 `libs/scguns-1.1.0hotfix.jar`，对外版本号 1.5.0） |
| Framework | 0.13.11+ |
| GeckoLib | 4.9.3+ |
| Curios | 9.5.1+ |
| TerraBlender | 4.1.0+ |

## 当前进度（阶段 1-9 全部完成）

已完成的移植内容：

- **工程**：NeoForge 21.1.249 / ModDevGradle 2.0.146 / Java 21，可离线 `gradlew build`；
- **注册层**：物品（含 71 个刷怪蛋）、护甲材料与 9 套 GeckoLib 护甲、武器、医疗物品、勋章、方块（铁丝网 / 电线 / 炮台 / 战争云杉树苗）、声音、状态效果、粒子、自定义属性、配置、创造标签；
- **实体**：8 个派系注册表共 **82 个生物** + 5 类弹丸 + `turret_seat`，含多部件 PartEntity、Boss、骑乘、持枪 AI；
- **AI 与枪械**：`entity/common` 的 brain / goal / gun 体系，瞄准状态经 `GunAttackGoalAccessor` 读取；
- **客户端渲染**：全部实体 / 物品 / 护甲 / 方块渲染器迁移到 GeckoLib 4.9（`renderRecursively(..., int color)`），参见 [docs/PHASE5-RENDER-AUDIT.md](docs/PHASE5-RENDER-AUDIT.md)；
- **网络**：Framework 0.13 `StreamCodec` 重写（枪口闪光 / 长按攻击 / 炮台扳机）；
- **袭击与 Mixin**：`WaveRaidManager`（`SavedData.Factory`）、数据包 reload、`/scgextra` 命令、16 个 Mixin；
- **世界生成**：5 个结构、2 个 processor、TerraBlender 区域与表面规则、战争云杉；
- **数据**：上游 `src/generated/resources` 的 333 个产物已并入（群系、地物、战利品表、配方、进度、刷怪蛋模型），并完成 1.21 数据包单数化与 `result.id`、`neoforge:`/`c:` 迁移。

### 已知限制

- **运行时未验证**：离线环境缺少 ModDevGradle 的 `neoforge-21.1.249-moddev-config.json`（需从 `maven.neoforged.net` 获取），`runClient` / `runServer` 尚未实机冒烟；模型朝向、持枪层、光束混合、护甲贴合、Mixin 注入点需联网后目视确认。
- 上游只提供 `en_us` 语言文件，本移植版未额外生成 `zh_cn`。
- 护甲的额外属性加成已改为 `ItemAttributeModifiers`；部分武器的“额外触及距离”仍未恢复。

详见 [PORTING-CHECKLIST.md](PORTING-CHECKLIST.md) 与 [PORTING-MATRIX.md](PORTING-MATRIX.md)。

## 构建

```bash
./gradlew build     # 产物 build/libs/scgextra-<version>.jar
./gradlew runClient
```

本机开发环境（离线）：

```properties
org.gradle.java.home=E:\mod\SG2-1.21\jdk21\jdk-21.0.11+10
```

依赖全部 vendored 在 `libs/`，无需访问 Maven 仓库。

## 许可

GNU General Public License v3.0，见 [LICENSE](LICENSE)。
本移植版保留上游作者署名，详见 [CREDITS.txt](CREDITS.txt)。
