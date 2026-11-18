# 运行时日志分析（2026-11-13）

日志：`D:\MCJAVA\.minecraft\versions\1.21.1-NeoForge_21.1.250\logs\latest.log`
环境：Minecraft 1.21.1 / NeoForge 21.1.250，约 200 个模组，含 `scgextra-1.0.0+4.0.0.jar`。

## 1. 致命问题（已修复）

创建新世界时崩溃：

```
java.lang.IllegalStateException: Unbound values in registry minecraft:worldgen/configured_feature:
[scgextra:grass_patch, scgextra:gravel_patch]
```

两个原因：

| 文件 | 1.20.1 写法 | 1.21 写法 | 修复 |
|---|---|---|---|
| `grass_patch.json` | `"Name": "minecraft:grass"` | 方块已改名 | 改为 `minecraft:short_grass` |
| `gravel_patch.json` | `"radius": {"type":"minecraft:uniform","value":{...}}` | 1.21 的 `IntProvider` 扁平化 | 改为 `{"type":"minecraft:uniform","min_inclusive":2,"max_inclusive":5}` |

## 2. 资源缺失（已补）

| 问题 | 修复 |
|---|---|
| `scgextra:blockstates/warzone_spruce_sapling.json` 缺失，`stage=0/1` 无模型 | 新增 blockstate（cross 模型，引用原版云杉树苗贴图） |
| `scgextra:models/item/warzone_spruce_sapling.json` 缺失 | 新增物品模型 |
| `scgextra:models/item/placeholder_gun.json` 缺失 | 新增物品模型（复用 `textures/entity/placeholder.png`） |
| `laser2.ogg` 不存在（上游文件名拼成 `laster2.ogg`） | 重命名为 `laser2.ogg` |
| `scrapguard.punch` 指向不存在的 `rrc/oppressor/punch` | 改指向 `rrc/oppressor/hurt_1` |
| `piglin.pigdread.idle` 引用不存在的 `idle_05..07` | 移除这三项 |

## 3. 仍存在但非阻塞的问题

### 3.1 12 个 GeckoLib 模型使用新格式（会渲染但 UV 可能错位）

上游把这 12 个模型导出为 Bedrock `1.21.0` / `1.21.110` 格式（逐面 UV + `uv_rotation`），
GeckoLib 4.9.3 只完整支持 `1.12.0`（Box UV），会打印：

```
Unsupported geometry json version: 1.21.0 for model ... Remove any rotated face UVs and re-export the model to fix
```

涉及：

- 护甲：`enlightened`、`oppressor`
- 实体：`asgharian/candle_fiend`、`asgharian/soul_ripper`、`cog/cog_trenchbleeder`、`piglin/piglin_hog_rider`、`piglin/piglin_kiln_hog`、`rrc/copper_knight`
- 物品：`netherite_chain_axe`、`spear_shovel`、`wrecker_rpg`、`wrecking_tool`

上游（GeckoLib 4.8.3）也是同样的提示，因此这是**上游遗留问题**，不是移植引入的。
修复方式只有两种：

1. 在 Blockbench 里把这 12 个模型改回 Box UV 后重新导出为 GeckoLib 4.x 格式（推荐，保真）；
2. 接受当前状态：模型仍会渲染，只是部分面 UV 可能错位。

> 说明：逐面 UV 无法无损转换回 Box UV（`uv_rotation` 会丢信息），因此没有做自动转换。

### 3.2 `drone.hurt` 无声音

`sounds.json` 已定义且 `sounds/rrc/drone/hurt1..8.ogg` 均存在，但运行期仍报
`Missing sound for event: scgextra:drone.hurt`。属于上游音频事件配置问题，不影响功能。

### 3.3 战利品箱为空（已修复）

第二次日志（14:16 启动）暴露了真正的数据问题：

```
Couldn't parse element ... loot_table]:scgextra:chests/fac
- No key value in MapLike[{"type":"minecraft:loot_table","name":"scgextra:chests/scgextra_default"}]

Couldn't parse element ... loot_table]:scgextra:entities/turtleman
- Unknown registry key ... minecraft:item]: minecraft:scute
```

原因与修复：

| 问题 | 1.20.1 写法 | 1.21 写法 | 修复文件数 |
|---|---|---|---|
| `loot_table` 引用条目字段改名 | `{"type":"minecraft:loot_table","name":"..."}` | 必须用 **`value`** | 9 个宝箱表 |
| 海龟鳞片改名 | `minecraft:scute` | `minecraft:turtle_scute` | 1 个实体表 |

这些表解析失败后，宝箱的 `LootTable` 无法解析，Lootr/原版都会报
`loot table 'scgextra:chests/...' couldn't be resolved`，箱子因此为空。

修复后所有 `minecraft:loot_table` 引用均已改为 `value`，残留检查为 0。

### 3.4 生物荧光贴图不显示（已修复）

GeckoLib 4.9 的 `GeoRenderer#reRender(...)` 最后一个参数是 **ARGB 颜色 `int`**（1.20.1 是 4 个 `float` 分量）。
移植时把原来的 `1, 1, 1, 1` 折叠成了单个 `1`，等价于 `0x00000001`（alpha = 0），
于是发光层以完全透明的方式绘制 —— 表现为“荧光贴图没贴上”。

修复（6 个文件）：

| 文件 | 说明 |
|---|---|
| `entity/common/client/BaseEntityRenderer.java` | 所有 `hasGlowLayer()` 生物共用的发光层 |
| `entity/common/client/GunnerRenderer.java` | 持枪单位 3 处发光层 |
| `entity/whaler/guardian_statue/GuardianStatueRenderer.java` | 雕像光束 |
| `entity/wreckers/wrecker_dozer/WreckerDozerRenderer.java` | 推土机 |
| `entity/wreckers/wrecker_helicube/WreckerHelicubeRenderer.java` | 直升机方块 |
| `item/armor/BaseArmorSetPartRenderer.java` | GeckoLib 护甲发光层 |

统一改为 `0xFFFFFFFF`（不透明白，保留贴图颜色）。

> 同时核对：所有 `*_glowmask.png` 都有对应的基础贴图，`_glowmask` 命名与 GeckoLib
> `AutoGlowingTexture` 的规则一致（变体 `xxx_0.png` → `xxx_0_glowmask.png`）。

日志中的 `Shader rendertype_entity_translucent_emissive could not find sampler named Sampler2`
来自资源包/ETF 对核心着色器的替换，与本次修复无关；如发光仍有异常，可先禁用 ETF 验证。

### 3.5 紫黑块贴图（部署问题，已确认）

日志里这些“贴图丢失”并不是模型或 PNG 的问题：

```
Failed to load texture: scgextra:textures/entity/asgharian/soul_ripper.png
java.util.zip.ZipException: invalid LOC header (bad signature)
```

`ZipException` 说明 **jar 在被读取时正被覆盖**：更新 `mods/` 里的 jar 时游戏仍在运行，
JVM 持有的旧中央目录与新的本地头对不上，于是随机若干条目读取失败 → 贴图变成紫黑块。

本次受影响的条目每次运行都不同（soul_ripper / cog_devastator / cog_vulture /
netherite_eater / scout / scrap_guard / armored_whale / fish_folk_1..2 等），也印证了这一点。

处理：

1. 关闭游戏后再替换 jar（本次已按此方式部署）；
2. 校验新 jar：2709 个条目、CRC 全部可读、0 个坏条目；
3. 顺带补上两个真正缺失的弹道贴图：
   - `textures/trail/mounted_gun_projectile.png`
   - `textures/trail/piglin_blaze_kopter_projectile.png`

若完整重启后**特定多碰撞箱敌人**（`cog_trenchbleeder` / `asgharian/soul_ripper` /
`piglin_hog_rider`）仍然异常，才会落到 §3.1 的 12 个新格式模型问题上。

### 3.6 后坐力被完全移除（已修复）

上游的 `recoil_multiplier` 属性是**百分比语义**（0 = 中性），其他所有属性消费点都写成
`1 + value/100`：

```java
LivingEntityMixin:    damageAmount *= (1 + GENERAL_DAMAGE_MULT / 100);
ProjectileEntityMixin: modifiedGravity *= (1 + BULLET_GRAVITY_MULT / 100);
RecoilHandlerMixin 第二个注入点: (1 + RECOIL_MULT / 100) * original
```

唯独 `RecoilHandlerMixin#onGetAdsRecoilReduction` 用了裸乘：

```java
// 1.20.1 scguns：getAdsRecoilReduction 返回“减免量”，0 = 不减免，所以裸乘是合理的
return (float) player.getAttributeValue(RECOIL_MULT) * original.call(instance, gun);
```

而 1.21 的 scguns 把该方法改成了**倍率**：

```java
public double getAdsRecoilReduction(Gun gun) {
    return 1.0 - gun.getGeneral().getRecoilAdsReduction() * AimingHandler.get().getNormalisedAdsProgress();
}
```

于是默认值 0 直接把相机后坐力乘成 0 —— 表现为“枪完全没有后坐力”。

修复：

1. 注入点改为 `(1 + value/100) * original`，默认 0 即 100% 后坐力；
2. `Medal of Obedience` 的 `-30` 修饰符从 `ADD_MULTIPLIED_TOTAL` 改为 `ADD_VALUE`
   （在基值 0 上做乘法永远是 0，等于这枚勋章原本无效）；
3. 属性显示名改为 `Recoil Modifier (%)`，与百分比语义一致。

修复后：

| 来源 | 属性值 | 后坐力倍率 |
|---|---|---|
| 无 | 0 | 100% |
| 主宰全套 4 件（每件 -5）+ 全套 -10 | -30 | 70% |
| 服从勋章 | -30 | 70%（与护甲叠加） |

### 3.7 海底神殿生成时崩溃（已修复）

第三次日志末尾（15:19:56）的 FATAL：

```
ReportedException: Feature placement
Caused by: java.lang.NullPointerException: Cannot invoke
  "net.minecraft.network.syncher.SynchedEntityData.get(...)" because "this.entityData" is null
  at LivingEntity.getSleepingPos
  at Entity.calculateViewVector
  at Entity.getLookAngle
  at GuardianStatueEntity.defineSynchedData(GuardianStatueEntity.java:265)
  at Entity.<init>
  at GuardianStatueEntity.<init>
  at OceanMonumentPieces$MonumentBuilding.generateWing   ← scgextra 的 OceanMonument Mixin 注入
```

原因：`GuardianStatueEntity#defineSynchedData` 里用

```java
builder.define(BEAM_LOOK_POS, this.position().add(this.getLookAngle()).toVector3f());
```

初始化光束朝向。但 `defineSynchedData` 是在 `Entity` **构造器内部**调用的，此时
`entityData` 还没有 build；`getLookAngle()` 会一路调到 `LivingEntity.getSleepingPos()`
读取 `entityData`，直接 NPE（Sable 的相机 mixin 只是让它更容易触发）。

修复：

```java
builder.define(BEAM_LOOK_POS, new org.joml.Vector3f());
```

真正的光束方向由 `BeamLaserAttackGoal#setBeamLookPos` 在开火时写入，初始值只需安全即可。

> 顺带全项目扫描了所有 `defineSynchedData` 方法体，只有这一处调用 `this.` 方法。

### 3.8 地堡结构不生成（已修复）

日志重复出现：

```
No starting jigsaw scgextra:trenches/bunker/start_top found in start pool scgextra:trenches/bunker/start
```

`bunker_trenches.json` 指定了 `start_jigsaw_name`，但它的起始池有 **6 个**元素
（`start_top` / `start_stairs_1..3` / `start_stairs` / `start_end`），只有 `start_top`
带这个 jigsaw。1.21 随机选到其他元素就会报错并放弃整个结构。

修复：移除 `bunker_trenches.json` 的 `start_jigsaw_name`，让任意起始元素都能展开。

### 3.9 其他日志噪声（非本模组）

- `Unknown recipe category: .../scgextra:mech_press/...`：scguns 自定义配方的
  `RecipeBookCategory` 未注册，scguns 自己的配方（`scguns:macerator/...`）也有同样警告；
- `Empty or non-existent pool: scguns:special`：由 scguns 数据引用触发，scg-extra 的
  模板池没有引用该池。

### 3.10 RRC Drone 不攻击 / Scout 被 scguns AI 顶替 / 模型朝向（已修复）

**Drone 不攻击**：`MountedGunAttackGoal#fireGun` 在阶段 4 因为依赖尚未接回的
`ArmoredWhaleProjectileEntity` 被留成空方法：

```java
private void fireGun(LivingEntity target) {
    // TODO(阶段 4): 依赖 whaler 的 ArmoredWhaleProjectileEntity…
}
```

已恢复为上游实现：生成 `ArmoredWhaleProjectileEntity`、按 3.0 速度朝目标射击、
播放 `BRUISER_SILENCED_FIRE`、触发枪口闪光；同时补上 `setYHeadRot`，让枪口跟随目标。

**Scout 被 scguns 本体 AI 顶替**：scguns 1.21 的 `GunnerMobSpawner#onEntityJoinWorld`
会对**任何手持 `GunItem` 的 `PathfinderMob`** 调用 `reassessWeaponGoal`，在优先级 0
插入它的 `GunAttackGoal`。scg-extra 的 `Gunner` 标记接口 + `GunnerMobSpawnerMixin`
本来用于阻止这一步，但 `ScoutEntity` 既没有实现 `Gunner`，也没有自己的
`SimpleGunAttackGoal`，因此被 scguns AI 接管。

修复：

```java
public class ScoutEntity extends GunnerEntity implements GeoEntity, Gunner
...
this.goalSelector.addGoal(1, new SimpleGunAttackGoal<>(this)
        .attackInterval(14).approachDist(8).maxRange(18).speedModifier(1.1));
```

**攻击时模型不朝向敌人**：`SimpleGunAttackGoal#handleAttack` 只调用
`mob.lookAt(...)`，头部转了但身体 `yBodyRot` 没同步，而 GeckoLib 的
`GeoEntityRenderer` 用的正是 `yBodyRot`（含 `yBodyRotO` 插值）。已在该方法里同时
设置 `yRot`/`yBodyRot`/`yHeadRot` 及对应 `O` 字段；Drone 的武器目标同理补了头部朝向。

> 其他仍继承 `GunnerEntity` 的旧单位（CopperKnight / Gargoyle / Oppressor /
> ScrapGuard / Tallman / Wrecker 系）目前仍走 scguns 的 `GunAttackGoal`；
> 如果也想统一成 scg-extra 的简化 AI，可以按 Scout 的方式逐个迁移。

### 3.11 调试碰撞箱在 Sable 物理结构上产生巨大框并掉帧（已修复）

`EntityHeadBoxDebug` 是本项目里的调试渲染器，但**它从未被注册**
（`register()` 没有调用点，jar 内也没有任何类引用它），真正在 F3+B 时绘制
头部碰撞箱的是 scguns 的 `HeadshotBoxDebugRenderer`（由
`EntityRenderDispatcherMixin` 调用）。

它的 `BoundingBoxManager.getHeadshotBoxes()` 对**任意** `MONSTER` / `CREATURE` /
`AMBIENT` 实体会回退到 `DynamicHeadshotBox`。Sable 的物理结构/子维度实体可能带有
远超常规的 AABB，于是每帧绘制一个巨大的线框，造成严重掉帧。

修复：

1. `EntityHeadBoxDebug` 内的两个取框方法增加 `isSaneBoundingBox()`（>16 格直接跳过），
   避免这段调试代码将来被启用时再次出问题；
2. 新增 **client-only Mixin** `HeadshotBoxDebugRendererMixin`，在
   `HeadshotBoxDebugRenderer#render` 的 HEAD 处检查传入的 AABB，任一轴超过 16 格
   就 `cancel()` —— 既保留普通生物的调试框，又不会给 Sable 结构画巨型框；
3. `scgextra.mixins.json` 增加 `"client"` 段（Mixin 引用了 `PoseStack` 等客户端类，
   不能注册到通用段）。

### 3.12 生物被移动后模型朝向偏移数秒（已修复）

现象：生物攻击时模型朝向正确；一旦被击退/传送/被 Sable 结构带走（即“被移动”），
模型朝向会偏移，约 3 秒后才慢慢转回目标。

原因在于我们自己的 `SimpleGunAttackGoal`：它原先只在**真正开火的那一帧**调用
`lookAt` 同步身体朝向。两次射击之间（`attackCooldown`）生物仍在移动/寻路，
`yBodyRot` 会被移动方向覆盖，而 GeckoLib 渲染用的正是 `yBodyRot`；
只有等移动结束、AI 重新开火，身体才会重新对准目标——表现为数秒的偏移。

修复：把“对准目标”从开火帧提升为**射程内每 tick 持续生效**：

```java
protected void tickAttack(LivingEntity target, double dist) {
    if (this.seeTime >= 10 && dist <= this.maxRange) {
        faceTarget(target);   // 每 tick 同步 yBodyRot / yHeadRot 及 O 值
        ...
    }
}

private void faceTarget(LivingEntity target) {
    float yaw = ...;
    this.mob.setYBodyRot(yaw);
    this.mob.setYHeadRot(yaw);
    this.mob.yBodyRotO = yaw;
    this.mob.yHeadRotO = yaw;
}
```

`handleAttack` 里也改为复用 `faceTarget`。这样即使生物正在被击退或随结构移动，
模型也会持续朝向敌人。

> 若重启后仍有部分生物（尤其是走 scguns `GunAttackGoal` 的旧单位或近战生物）
> 出现同样现象，请告知具体生物——那说明问题出在服务端旋转同步/移动控制，
> 需要另外在 `ServerEntity` 或 `BodyRotationControl` 层面处理。

### 3.13 生物发现敌人后转身极慢（已修复，客户端侧）

补充现象后可以确定根因在**客户端**的 `BodyRotationControl`：

```java
// 原版：站立不动时，身体每 tick 只转向头部朝向的 1/10
private static final int HOW_LONG_IT_TAKES_TO_FACE_FORWARD = 10;
...
this.mob.yBodyRot -= g / (float) HOW_LONG_IT_TAKES_TO_FACE_FORWARD;
```

这是渐近式的：180° 的差距需要好几秒才转过来，正是"发现敌人后模型极慢转向"、以及
"被移动后朝向偏移几秒才恢复"的来源。

关键点：`yBodyRot` 是**客户端本地计算**的，§3.12 在服务端 `setYBodyRot()` 并不会
同步到客户端（客户端只同步 `yRot` 与 `yHeadRot`），所以服务端改法对模型朝向无效。

修复：新增通用 Mixin `BodyRotationControlMixin`，注入
`rotateHeadTowardsFront` 的 HEAD，只对 **scg-extra 命名空间、且当前有攻击目标**的
生物生效，用 30°/tick 把 `yBodyRot` 转向 `yHeadRot`，然后取消原版逻辑：

```java
if (mob.getTarget() == null) return;
if (!"scgextra".equals(BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).getNamespace())) return;
mob.yBodyRot = Mth.approachDegrees(mob.yBodyRot, mob.yHeadRot, 30.0F);
ci.cancel();
```

30°/tick 意味着 180° 大约 6 tick（0.3 秒）完成，同时保留平滑过渡；不影响移动时的
身体朝向（那条路径由 `rotateBodyIfNecessary` 处理）。

> 之前的 `SimpleGunAttackGoal#faceTarget` 仍然保留：服务端同步 `yRot`/`yHeadRot`
> 是客户端计算的基础，两者配合才能让模型在开火时始终对准目标。

#### 3.13.1 按“方案一”改为战斗锁定（最终实现）

社区常见的两种做法：

- **方案一**：Mixin 拦截 `BodyRotationControl`，战斗状态下跳过原版的缓慢转正逻辑；
- **方案二**：在 GeckoLib 渲染层用 `bodyYaw - headYaw` 做上半身反向补偿。

最终采用**方案一**（从控制层修正，不碰渲染，兼容所有 GeckoLib 模型）：

```java
@Inject(method = "rotateHeadTowardsFront", at = @At("HEAD"), cancellable = true)
private void lockBodyToHead(CallbackInfo ci) {
    if (isGunUnit()) { snapBodyToHead(); ci.cancel(); }
}

@Inject(method = "clientTick", at = @At("TAIL"))
private void keepBodyLocked(CallbackInfo ci) {
    if (isGunUnit()) snapBodyToHead();
}
```

两个关键细节：

1. **不能用 `mob.getTarget()` 判断**——目标（target）不会同步到客户端，客户端的
   `getTarget()` 基本恒为 null。因此判据改为客户端可见的：
   `mob instanceof Gunner` 或主手持有 `scguns` 的 `GunItem`，并且实体属于
   `scgextra` 命名空间。
2. **`clientTick` 末尾再对齐一次**——移动时 `rotateBodyIfNecessary` 会把身体拉向移动
   方向，仅拦截 `rotateHeadTowardsFront` 不够；TAIL 处补一次才能保证被推挤/移动后
   身体仍锁定头部朝向。

同时写入 `yBodyRotO`，避免渲染插值造成一 tick 的拖影。

### 3.14 RRC 生物与 scguns 枪手 AI 打架、丢失动画（已修复）

RRC 的 `CopperKnight / Gargoyle / Oppressor / Tallman`（以及此前的 Scout）都继承了
`GunnerEntity` 但没有实现 `Gunner` 标记，于是 scguns 的 `GunnerMobSpawner` 在实体加入
世界时给它们塞入本体的 `GunAttackGoal`：

- 它的 AI 与 scg-extra 的控制器/动画状态互不认识，
  动画控制器里依赖的 `isAiming()` 永远为 false；
- 同时两套 AI 争夺 `MOVE`/`LOOK` 目标，表现为“AI 打架”。

修复分两步：

1. **`GunnerEntity#updateAimStates` 支持两种枪 AI**：除了读取 scguns
   `GunAttackGoal` 的 `aimingStabilityTimer`，现在也识别 scg-extra 的
   `SimpleGunAttackGoal`，用它的 `GoalState`（`AIMING_STATE` / `FIRING_STATE`）
   驱动 `AIMING` 同步数据，让 `idle_aim` 等动画重新生效；
2. **RRC 枪手改用自带 AI**：`CopperKnight / Gargoyle / Oppressor / Tallman`
   实现 `Gunner` 标记（`GunnerMobSpawnerMixin` 因此阻止 scguns 注入），并在
   `registerGoals` 里按各自体型/武器加上 `SimpleGunAttackGoal`：

| 生物 | 攻击间隔 | 接近距离 | 最大射程 |
|---|---|---|---|
| CopperKnight | 16 | 7 | 16 |
| Tallman | 18 | 9 | 20 |
| Oppressor | 20 | 10 | 22 |
| Gargoyle | 20 | 8 | 18 |
| Scout（上次已迁移） | 14 | 8 | 18 |

`ScrapGuard` 不用改：它自己注册了 `ScrapGuardAttackGoal extends GunAttackGoal`，
本来就满足 `hasGunAttackGoal()`，且走的是可读动画状态的 scguns 基线 AI。

> Wrecker 派系的 `GunnerEntity` 子类（Blue/Green/Red/Jumbo/TurretGunner）目前仍是
> scguns AI，如果也有动画丢失，可以按同样的方式迁移。

#### 3.14.1 Wrecker 派系排查结果

逐个核对了 Wrecker 的 `GunnerEntity` 子类：

| 生物 | 武器 | 原枪 AI | 结论 |
|---|---|---|---|
| WreckerRed | scguns SMG | **已是** `SimpleBurstGunAttackGoal` | 无需改动；新 `updateAimStates` 让它恢复动画状态 |
| WreckerTurretGunner | scguns 枪 | **已是** `SimpleBurstGunAttackGoal` | 同上 |
| WreckerGreen | `scgextra:wrecker_rpg` | 无 → scguns 注入 | **已迁移**：实现 `Gunner` + `SimpleGunAttackGoal`（间隔 30 / 接近 12 / 射程 24） |
| WreckerBlue | `scgextra:wrecking_tool`（近战） | 无（非 `GunItem`） | 纯近战 `WreckerBlueMeleeGoal`，不涉及枪 AI |
| WreckerJumbo | `scgextra:wrecker_sledge_hammer`（近战） | 无 | Brain 近战（`WreckerJumboAi`），不涉及枪 AI |
| WreckerBiker / Dozer / Helicube | 近战/自制攻击 | 无（非 `GunnerEntity`） | 各自 Goal/Brain，无冲突 |

也就是说 Wrecker 里只有 **Green** 存在“被 scguns AI 接管”的问题，已经修掉；Red 和
TurretGunner 本来就跑 scg-extra 的简化 AI，只是此前 `updateAimStates` 不认这种 Goal，
动画看起来丢失——§3.14 的状态机修复同时解决了它们。

RRC 内部还剩一个 `ScrapGuard`：它自己注册了 `ScrapGuardAttackGoal extends GunAttackGoal`
（scguns 基类 + 近战分支），属于有意为之，动画通过 `GunAttackGoalAccessor` 读取；
如果希望它也完全脱离 scguns AI，可以再迁移。

#### 3.14.2 回退记录（最终状态）

确认"原版动画可用"依赖的是 `GunAttackGoalAccessor` 桥接而不是 AI 本身之后，
按要求把 RRC 与 WreckerGreen **回退到初版**（即继续使用 scguns 的 `GunAttackGoal`）：

- 移除 `CopperKnight / Gargoyle / Oppressor / Scout / Tallman / WreckerGreen`
  上的 `Gunner` 标记与 `SimpleGunAttackGoal` 注册；
- 保留 `GunnerEntity#updateAimStates` 对两种 AI 的兼容（Asgharian 的
  `SimpleBurstGunAttackGoal` 仍需要它来驱动动画状态）；
- 保留 `GunnerMobSpawnerMixin`，让真正自制 AI 的生物（COG/FAC/whaler/piglin/
  neutral 等 `Gunner` 实现者）继续不被 scguns 注入。

回退后这些 RRC/Wrecker 单位的动画由 scguns `GunAttackGoal` 的
`aimingStabilityTimer` 经 accessor 驱动，与 1.20.1 行为一致。

#### 3.14.3 转向手感：改用自定义 `BodyRotationControl`（参考车万女仆）

之前用全局 Mixin 把 `yBodyRot` 每 tick 直接锁到 `yHeadRot`，虽然不慢了，但观感是
"纯机械地硬转"。车万女仆的做法更自然：`MaidMoveControl` 在移动时把身体方向直接对齐
移动方向，站立时交给原版控制慢慢转。

现在改成给 `GunnerEntity` 系列覆盖 `Mob#createBodyControl()`，使用自写的
`GunnerBodyRotationControl`（不再用全局 Mixin）：

| 状态 | 行为 |
|---|---|
| 移动中 | 身体以 45°/tick 追向移动方向（几乎即时但有过渡） |
| 站立/瞄准 | 指数平滑：每 tick 完成剩余角度的 **35%**，180° 约 6~8 tick，缓入缓出 |
| 任何状态 | 头部限制在身体 ±`getMaxHeadYRot()` 内，避免头身脱节 |

同时移除了 `BodyRotationControlMixin`，避免两套逻辑叠加。该控制只作用于
`GunnerEntity` 子类（RRC / Wrecker / Asgharian 枪手），其他派系的 Brain 生物不受影响。

#### 3.14.4 扩展到全部 scg-extra 生物

最初只有 `GunnerEntity` 子类（RRC/Wrecker）用上了新控制；FAC 的普通枪手单位继承的是
`EquippedEntity`（`FacBluecoat / FacCommissar / FacTrencher / FacTrenchSniper /
FacTankBuster / FacLion` 等），仍然走原版逻辑。

改为在 `Mob#createBodyControl()` 的返回值上统一替换：

```java
@Mixin(Mob.class)
public class MobBodyRotationMixin {
    @Inject(method = "createBodyControl", at = @At("RETURN"), cancellable = true)
    private void replaceBodyControl(CallbackInfoReturnable<BodyRotationControl> cir) {
        Mob self = (Mob) (Object) this;
        var key = BuiltInRegistries.ENTITY_TYPE.getKey(self.getType());
        if (key != null && "scgextra".equals(key.getNamespace())) {
            cir.setReturnValue(new GunnerBodyRotationControl(self));
        }
    }
}
```

这样所有 `scgextra` 命名空间的生物（RRC / FAC / COG / whaler / piglin / neutral /
wreckers / drone 等）都会使用同一套自然转向，`GunnerEntity` 里原来的覆盖同步移除，
避免重复。

### 3.15 移动时只播举枪动画、看起来像滑步（已修复）

现象：生物一边移动一边发现敌人时，只播放 `idle_aim` / 举枪动画，腿不动，像在滑行。

根因是 **GeckoLib 的 `AnimationState#isMoving()` 不可靠**——它按水平位移判断，但采样
时机在客户端不稳定，移动时偶尔返回 `false`。上游代码里其实已经有人踩过这个坑，
`CopperKnightEntity` 的控制器里留了注释：

```java
if (state.getAnimatable().isAiming()) {
    if (((x - xo)² + (z - zo)²) > 0.0002) {
        return walk_holding_aim;   // 用坐标差而不是 state.isMoving()
    }
    return AIMING;
}
```

但 `Tallman / Scout / Oppressor / ScrapGuard / WreckerGreen` 以及大量 FAC/COG/whaler
单位仍然直接用 `state.isMoving()`，于是移动+瞄准时走进 `AIMING` 分支，腿停在原地。

修复：

1. `MobUtil` 新增稳定判断：

```java
public static boolean isMoving(Entity entity) {
    double dx = entity.getX() - entity.xOld;
    double dz = entity.getZ() - entity.zOld;
    return dx * dx + dz * dz > 2.0E-4D;
}
```

2. 全项目 **63 个文件**里的 `state.isMoving()` 统一替换为
   `MobUtil.isMoving(state.getAnimatable())`。

这样移动+瞄准会正确进入 `walk_holding_aim`（有举枪的行走动画），不再出现"平移"。

### 3.16 海洋结构内部漏水（已修复）

现象：潜艇 / 战舰等海洋结构里的半砖、楼梯等可含水方块被错误地灌满了水，看起来像漏水。

根因是 1.21 的 `LiquidSettings` 语义：`JigsawPlacement.addPieces` 与
`JigsawStructure` 的 `liquid_settings` 默认是 **`APPLY_WATERLOGGING`**，放置结构时会把
可含水方块（slab / stairs / fence 等）自动 waterlog。1.20.1 的模板默认是"不注水"，
所以这是 1.21 数据格式变化带来的回归。

修复：

1. 所有 `minecraft:jigsaw` 结构 JSON（16 个）增加：

```json
"liquid_settings": "ignore_waterlogging"
```

2. 5 个自定义结构类（`MineTrench / DestroyedMines / CampingSite / TrenchesAnchor /
   WaterAvoiding`）里 `JigsawPlacement.addPieces(...)` 的参数由
   `LiquidSettings.APPLY_WATERLOGGING` 改为 `LiquidSettings.IGNORE_WATERLOGGING`。

这样结构放置时不再向半砖/楼梯注水，船体内部保持干燥。

### 3.17 `leviathan_prismarine_gate` 不生成（已修复）

检查发现两个问题叠加：

1. **硬编码的水位检测**：`WaterAvoidingStructure#findGenerationPoint` 里写死检查
   `getBlock(62)`，对海滩结构来说 y=62 经常正好落在海平面以下的水/沙交界处，
   大量本应生成的位置被直接 `Optional.empty()` 否决；
2. **生物群系列表过窄**：原 JSON 只列了 `minecraft:beach` / `minecraft:snowy_beach`，
   漏掉了 `stony_shore`，而海滩本身只是很窄的一条带，随机分布下几乎遇不到。

修复：

- `WaterAvoidingStructure` 改为检测 `project_start_to_heightmap` 投影到的**实际地表方块**
  （`getFirstOccupiedHeight`），不再用固定 y=62；
- `leviathan_prismarine_gate.json` 的 `biomes` 改为 **`#minecraft:is_beach`**
  （覆盖 beach / snowy_beach / stony_shore）。

注意：世界生成只在新区块生效，旧存档需要去未加载过的海滩区域或开新世界验证；
可以用 `/locate structure scgextra:leviathan_prismarine_gate` 检查。

### 3.18 `bunker_trenches` 生成到天空中（已修复）

§3.8 为了让 6 元素起始池都能生成，移除了 `bunker_trenches.json` 的
`start_jigsaw_name`。这带来了副作用：没有命名 jigsaw 做锚点对齐时，
`JigsawPlacement` 会把模板**原点**直接放在地表投影点上，而模板原点是包围盒最小角
（底部），于是整座地堡被"原点对地表"地摆到空中。

修复：

1. **恢复** `"start_jigsaw_name": "scgextra:trenches/bunker/start_top"`，
   让 `start_top` 通过命名 jigsaw 正确下沉到地表以下；
2. 新建单元素入口池 `scgextra:trenches/bunker/start_entry`（只含 `start_top`），
   `bunker_trenches.json` 的 `start_pool` 指向它。原来那个包含 6 个元素的
   `start` 池继续作为**连接链的目标池**使用——下游 `start_stairs*` / `start_end`
   的 jigsaw 都写的是 `"pool": "scgextra:trenches/bunker/start"`，靠名字匹配取件，
   所以不会再把它们当作结构的起始块抽到。

这样结构永远从 `start_top` 开始锚定，不再有空中的情况，也不会再出现
“No starting jigsaw” 报错。

### 3.18.1 起始池里那 5 个变体到底是什么

`start` 池里的 6 个元素其实是一条**垂直入口链**，不是 6 种平级入口：

| 模板 | 尺寸 (x×y×z) | 作用 |
|---|---|---|
| `start_top` | 19×10×19 | 地表入口段，唯一带 `start_top` jigsaw 的起点 |
| `start_stairs` | 5×16×5 | 第一段竖井楼梯，jigsaw 名 `start_stairs` |
| `start_stairs_1` | 5×16×5 | 第二段楼梯，jigsaw 名 `start_stairs_1` |
| `start_stairs_2` | 5×16×5 | 第三段楼梯，jigsaw 名 `start_stairs_2` |
| `start_stairs_3` | 5×16×5 | 第四段楼梯，jigsaw 名 `start_stairs_3` |
| `start_end` | 16×7×15 | 底部连接段，jigsaw 名 `start`，通向 `start_hallways` 走廊网 |

连接关系（jigsaw 的 `target` → 目标 jigsaw 名）：

```
start_top(start_top) → start_stairs → start_stairs_1 → start_stairs_2 → start_stairs_3 → start_end(start) → start_hallways(走廊)
```

所以它们必须留在 `start` 池里供连接链按名字取件，但**只能让 `start_top` 当结构起点**；
之前把它们和 `start_top` 平级放在起始池、又删掉 `start_jigsaw_name`，就是空中地堡的根源。

### 3.19 地雷爆炸后生物攻击"已消失的地雷"（已修复）

`scguns:mine_unit`（战壕结构里的地雷）触发时，scguns 的
`MineUnitBlockEntity#triggerGrenade` 会调用 `ItemEntity#getControllingPassenger()`
来拿"投掷者"。1.21 的 `ItemEntity` 已经不再有投掷者语义（该方法恒为 null），
所以移植时我们在 `MineUnitBlockEntityMixin` 里造了一个临时的 `ArmorStand` 当作伤害来源：

```java
return new ArmorStand(instance.level(), instance.getX(), instance.getY(), instance.getZ());
```

问题在于这个盔甲架**没有放进世界、也没有被移除**：它在内存里始终 `isAlive() == true`。
爆炸把伤害归因给它后，被炸到的生物会把 `HURT_BY_ENTITY` / `ATTACK_TARGET`
指向这个"幽灵盔甲架"；而地雷本身已经消失，于是生物会走到地雷位置对着空气挥击。

修复：临时盔甲架创建后立即 `discard()`：

```java
ArmorStand stand = new ArmorStand(level, x, y, z);
stand.setInvisible(true);
stand.discard();   // isAlive() == false，不再成为有效攻击目标
return stand;
```

它仍能作为 `DamageSource` 的实体来源（爆炸伤害、击退照常），但 `isAlive()` 为 false，
`StopAttackingIfTargetInvalid` 与 `HurtByTargetGoal` 会立刻清掉这个无效目标。

## 4. 结论

- **世界创建崩溃已定位并修复**，重新构建的 jar 可以再次测试；
- 其余 `scgextra` 相关日志均为资源/模型警告，不再有异常或崩溃；
- Mixin、网络、实体注册、TerraBlender 区域（`Registered region scgextra:overworld to index 1`）均正常加载。

### 3.20 举枪移动出现 T-pose（已修复）

部分 RRC 枪手在 `isAiming() && isMoving()` 时播放 `walk_holding_aim`，但
`tallman` / `scrap_guard` 的动画文件里并没有这个动画（只有 `walk` / `idle_aim`），
GeckoLib 找不到动画就停在 T-pose。

修复：移动 + 瞄准时统一改用 `walk`：

- `CopperKnight` / `ScrapGuard` / `Tallman`：`walk_holding_aim` → `walk`；
- `Scout`：瞄准分支增加移动判断，移动时播 `walk`，站立才播 `idle_aim`。

已核对 `copper_knight / oppressor / scout / scrap_guard / tallman` 五个动画文件都同时
包含 `idle_aim` 与 `walk`，不会再出现缺失动画。

### 3.21 scguns 本体袭击仍掉落蓝图前置材料（已修复）

上游 3.1.x 在 `data/scguns/loot_tables/raids/` 下覆盖了 scguns 本体袭击的 boss 战利品表，
把铜/海洋袭击 boss 的整张表清空，用来阻止蓝图前置材料掉落。4.0.0 的源码里这些覆盖表已经
不存在了，所以 1.21.1 移植版一直沿用 scguns 默认表，自然刷新的小型袭击会把材料掉出来。

修复：按 1.21 的 `data/scguns/loot_table/raids/` 路径恢复覆盖表，但**不再清空整张表**，
只移除"战利品/材料"那一条（`rusty_medal`、`labor_trophy`、`gold_idol`、
`snapped_cogwheel`、`cog_heart`、`ceremonial_cod`、`leviathan_tooth`），
枪械、弹药、护甲、信号弹、头盔等全部保留。

- 覆盖 antique / copper / diamond_steel / frontier / iron / ocean / piglin /
  treated_brass / wrecker 九张表；
- **sculk_boss 不覆盖**：scg-extra 没有幽匿派系袭击，保持 scguns 原样；
- `fac_boss` 沿用 3.1.x 的自定义表，同样只去掉 `labor_trophy`。

### 3.22 套装护甲 tooltip 动态化 + 完整中文语言包

原来的 `ArmorSetPartItem#appendHoverText` 对每一件护甲都固定输出全套/散件两段说明，
不区分当前穿了几件，看起来像「单件就触发套装」。

现在改为动态显示：

- 新增 `armorset.progress`（`已装备：%s/4 件`）；
- 客户端读取同步过来的 `scgextra:<套装>` 计数器（每件 +0.5），换算成 0-4 件；
- 达到 2 件的散件效果显示绿色，达到 4 件的全套效果显示绿色，未达到的显示灰色。

同时补了完整 `zh_cn`：

- `assets/scgextra/lang/zh_cn.json`：557 条，覆盖实体、刷怪蛋、物品、盔甲、勋章、属性、方块、效果、突袭文案与全部字幕；
- 术语对齐 scguns 1.21.1 移植版自带的社区汉化（ZR4U 提供）：`突袭`、`锈岭`、`联邦`、
  `联邦军备联合会`、`利维坦联合军`、`扫荡者`、`古典 / 边疆 / 烈焰黄铜 / 幽匿`、
  `子弹`（不写弹丸）、`装填速度`（不写换弹速度）、`硫中毒`；
- 不再自带 `assets/scguns/lang/zh_cn.json`：scguns 的 jar 里已经带了完整社区汉化，
  覆盖它反而会造成同名键冲突；
- 翻译脚本保留在 `tools/gen_zh_cn.py`，后续增删键可以直接重跑。

### 3.23 生物被机械动力坐垫 / 船抓走（已修复）

Create 6.x 的 `SeatBlock#canBePickedUp` 允许把敌对生物塞进坐垫（取决于服务端
`seatHostileMobs` 配置），而 `SeatEntity`/原版船、矿车在 `startRiding` 层并不限制
乘客类型。生物一旦被塞进去，Brain/Goal AI 会卡在骑乘状态，原地不动。

修复做了两层：

1. **Create 实体标签**：新增 `scgextra:cannot_use_seats` 标签列出 71 个本模组生物，
   `data/create/tags/entity_type/ignore_seat.json` 引用它；
2. **`EntityRideGuardMixin`**：在 `Entity#startRiding(Entity, boolean)` 入口拦截，
   只对 `scgextra:` 命名空间的生物生效，拒绝船、矿车和 `create:` 命名空间里名字带
   `seat` 的载具。

这样无论 Create 配置怎么改、玩家怎么用船推，本模组生物都不会被“控住”。

### 3.24 毁灭者的炮塔头不锁定目标（已修复）

`cog_devastator` 的模型头部是坦克炮塔造型。服务端已经在 AI 里调用
`getLookControl().setLookAt(target, 90, 90)` 设置了头部朝向，但 GeckoLib 不会自动把
`head` 骨骼按实体的 `yHeadRot` 旋转，所以视觉上头部始终跟着身体。

修复：新增客户端 `CogDevastatorModel`，在 `setCustomAnimations` 里覆盖 `head` 骨骼：

- 客户端通过同步的 `TARGET_ID` 解析出目标；
- 用双方位置计算世界偏航/俯仰，再减去身体朝向得到骨骼局部旋转；
- 没有目标时不覆盖，保留待机动画的头部动作。

渲染注册改为使用该模型（原来的 `DefaultedEntityGeoModel(..., "chest")` 换成
`CogDevastatorModel`）。
