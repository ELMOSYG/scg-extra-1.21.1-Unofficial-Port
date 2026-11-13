# 附录：SCG Extra 480 文件移植矩阵

> 由扫描原工程自动生成，配套 `PORTING-CHECKLIST.md` 使用。行数为含空行的物理行数（非空行合计 41,938）。

> 分级：**重写**＝需要重新设计；**中等**＝需改语义/API 模型；**机械**＝改 import 与注册写法即可。

> 标记：MIXIN=Mixin 注入｜NET=Framework 网络重写｜DATAGEN=datagen 重写｜WORLDGEN=群系/结构数据化｜CONFIG=配置 API｜PART=PartEntity｜CAP=Capability｜NBT=数据组件｜ENCH=附魔读取｜GEO=GeckoLib｜SCG=scguns 接口｜CLIENT=客户端｜RENDERREC=renderRecursively 11 参｜SYNC=defineSynchedData(Builder)｜FINALIZE=finalizeSpawn｜PACKET=getAddEntityPacket(ServerEntity)


### SCGExtra.java（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| SCGExtra.java | 165 | 机械 | SCG, FMLCTX |

### CommonConfig.java（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CommonConfig.java | 217 | 中等 | CONFIG |

### attributes（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| SCGEAttributes.java | 83 | 机械 | — |

### block（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ModBlockEntities.java | 28 | 机械 | — |
| ModBlocks.java | 71 | 机械 | — |

### block/barbed_wires（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BarbedWireBlock.java | 80 | 机械 | SCG |

### block/electrical_wires（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ElectricalWiresBlock.java | 125 | 机械 | SCG |
| ElectricalWiresBlockEntity.java | 129 | 中等 | CAP, NBT |

### block/wreckerturret（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerTurretBlock.java | 114 | 机械 | SCG |
| WreckerTurretBlockEntity.java | 284 | 中等 | NBT, GEO, SCG |
| WreckerTurretBlockModel.java | 31 | 中等 | GEO |
| WreckerTurretBlockRenderer.java | 83 | 中等 | GEO, SCG, CLIENT |

### client（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ScreenRenderer.java | 83 | 机械 | CLIENT |
| TurretInputHandler.java | 53 | 机械 | CLIENT |

### client/particle（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CopperFireBallParticle.java | 80 | 机械 | CLIENT |
| CopperFlameParticle.java | 62 | 机械 | CLIENT |

### data（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| RaidDataLoader.java | 119 | 机械 | — |

### datagen（8 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| DataGenerators.java | 41 | 重写 | DATAGEN |
| ModEntityLootTableProvider.java | 488 | 重写 | DATAGEN, SCG |
| ModEntityTagProvider.java | 244 | 重写 | DATAGEN, SCG |
| ModItemModelProvider.java | 211 | 重写 | DATAGEN, SCG |
| ModItemTagProvider.java | 44 | 重写 | DATAGEN |
| ModRecipeProvider.java | 504 | 重写 | DATAGEN, SCG |
| ModWorldGenProvider.java | 25 | 重写 | DATAGEN |
| RaidLootSubProvider.java | 66 | 重写 | DATAGEN |

### debug（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| DevTestCommands.java | 17 | 机械 | — |
| EntityHeadBoxDebug.java | 124 | 机械 | SCG, CLIENT |

### effects（7 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AttributeMobEffect.java | 13 | 机械 | — |
| EnlightenmentHealthBoostMobEffect.java | 37 | 机械 | — |
| GlowingInkEffect.java | 11 | 机械 | — |
| InkEffect.java | 11 | 机械 | — |
| ModEffects.java | 31 | 机械 | — |
| MortalMobEffect.java | 30 | 机械 | — |
| StunnedEffect.java | 14 | 机械 | — |

### entity（8 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AbilityState.java | 33 | 机械 | — |
| EntityAdjustments.java | 71 | 机械 | SCG |
| EntityTypeTags.java | 22 | 机械 | — |
| Faction.java | 97 | 机械 | — |
| ModBrainActivities.java | 28 | 机械 | — |
| ModBrainMemories.java | 76 | 机械 | — |
| ModBrainSensors.java | 42 | 机械 | — |
| ModEntities.java | 136 | 机械 | SCG, CLIENT |

### entity/asgharian（8 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AbilityGoal.java | 114 | 机械 | — |
| AsgharianEntities.java | 128 | 中等 | GEO, SCG, CLIENT |
| BulletSpawnOffset.java | 13 | 机械 | — |
| GoalState.java | 25 | 机械 | — |
| GoalStateHandler.java | 8 | 机械 | — |
| SimpleBurstGunAttackGoal.java | 57 | 机械 | — |
| SimpleGunAttackGoal.java | 232 | 机械 | SCG |
| WeakPointPart.java | 6 | 机械 | — |

### entity/asgharian\candlefiend（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BreakBlocksToTargetGoal.java | 104 | 机械 | — |
| CandleFiendAttackGoal.java | 192 | 机械 | SCG |
| CandleFiendEntity.java | 390 | 中等 | NBT, GEO, CLIENT, SYNC |
| LeapGoal.java | 72 | 机械 | — |
| Leaping.java | 9 | 机械 | — |

### entity/asgharian\failedone（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FailedOneEntity.java | 178 | 中等 | NBT, GEO, FINALIZE |

### entity/asgharian\flamer（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AsgharFlamerEntity.java | 159 | 中等 | GEO |

### entity/asgharian\soulripper（6 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| SoulRipperChargeAttackGoal.java | 117 | 机械 | — |
| SoulRipperEntity.java | 465 | 中等 | PART, NBT, GEO, SCG, SYNC |
| SoulRipperFireballGoal.java | 62 | 机械 | — |
| SoulRipperMoveControl.java | 38 | 机械 | — |
| SoulRipperRandomMoveGoal.java | 46 | 机械 | — |
| SoulRipperSummonVexGoal.java | 60 | 中等 | FINALIZE |

### entity/asgharian\surgeon（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AsgharSurgeonAttackGoal.java | 88 | 机械 | SCG |
| AsgharSurgeonEntity.java | 194 | 中等 | GEO, SYNC |
| ConstantSummonGoal.java | 57 | 中等 | FINALIZE |
| HealNearbyGoal.java | 54 | 机械 | — |

### entity/asgharian\worker（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AsgharWorkerEntity.java | 245 | 中等 | GEO, SCG |
| AsgharWorkerModel.java | 19 | 中等 | GEO, CLIENT |

### entity/cog（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| COGEntities.java | 186 | 中等 | GEO, SCG, CLIENT |

### entity/cog\bombardier（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogBombardierAi.java | 106 | 机械 | — |
| CogBombardierEntity.java | 184 | 中等 | GEO, SCG |

### entity/cog\centipede（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogCentipedeAi.java | 97 | 机械 | — |
| CogCentipedeEntity.java | 211 | 中等 | PART, GEO, SCG |
| CogCentipedeRenderer.java | 15 | 中等 | GEO, CLIENT |
| PlasmaCannonProjectileEntity.java | 178 | 机械 | SCG |

### entity/cog\devastator（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogDevastatorAi.java | 83 | 机械 | — |
| CogDevastatorEntity.java | 239 | 中等 | GEO, SCG, SYNC |
| CogDevastatorRenderer.java | 21 | 中等 | GEO, CLIENT |

### entity/cog\gigantes（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogGigantesAi.java | 112 | 机械 | — |
| CogGigantesEntity.java | 237 | 中等 | PART, GEO, SCG, SYNC |
| CogGigantesRenderer.java | 16 | 中等 | GEO, CLIENT |
| SummonCarriers.java | 137 | 机械 | SCG |

### entity/cog\juggernaut（9 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BurnNearby.java | 55 | 机械 | — |
| CogJuggernautAi.java | 103 | 机械 | — |
| CogJuggernautEntity.java | 342 | 中等 | GEO, SCG, CLIENT, SYNC |
| CogJuggernautRenderer.java | 81 | 重写 | GEO, SCG, CLIENT, RENDERREC |
| DynamicWeaponSwitching.java | 44 | 机械 | SCG |
| JetBootsAbility.java | 137 | 机械 | — |
| JetBootsCheckRelocate.java | 120 | 机械 | — |
| RocketBarrageAbility.java | 104 | 机械 | SCG |
| RocketBarrageProjectileEntity.java | 101 | 机械 | SCG |

### entity/cog\trenchbleeder（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogTrenchbleederAi.java | 113 | 机械 | — |
| CogTrenchbleederEntity.java | 255 | 中等 | PART, GEO, SCG |
| CogTrenchbleederRenderer.java | 14 | 中等 | GEO, CLIENT |
| CogTrenchbleederShootTarget.java | 73 | 机械 | — |

### entity/cog\venator（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogVenatorAi.java | 91 | 机械 | — |
| CogVenatorEntity.java | 207 | 中等 | PART, GEO, SCG |
| CogVenatorRenderer.java | 15 | 中等 | GEO, CLIENT |

### entity/cog\vulture（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogVultureAi.java | 66 | 机械 | — |
| CogVultureEntity.java | 204 | 中等 | PART, GEO, SCG |
| CogVultureRenderer.java | 24 | 中等 | GEO, CLIENT |

### entity/common（10 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| EquippedEntity.java | 63 | 中等 | NBT, SCG, FINALIZE |
| FlyDirectMoveControl.java | 79 | 机械 | — |
| Gunner.java | 25 | 机械 | — |
| GunnerEntity.java | 100 | 中等 | SCG, SYNC |
| HeadShotHandler.java | 13 | 机械 | — |
| MobUtil.java | 113 | 中等 | NBT, SCG |
| OffsetRotatedHeadshotBox.java | 82 | 机械 | SCG |
| Stunnable.java | 45 | 机械 | — |
| WeakPointBox.java | 35 | 机械 | SCG |
| WeakPointBoxManager.java | 37 | 机械 | — |

### entity/common\brain（35 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AccuracyBuildup.java | 38 | 机械 | — |
| AimWhenNotWalking.java | 76 | 机械 | — |
| AimWhenTargetVisible.java | 75 | 机械 | — |
| AlertNearbyFactionMobs.java | 58 | 机械 | — |
| ApproachTargetIfCannotAim.java | 74 | 机械 | — |
| AttackLastHurtIfNear.java | 64 | 机械 | — |
| AvoidTargetIfClose.java | 38 | 机械 | — |
| BrainCommons.java | 158 | 机械 | — |
| BrainUtils.java | 102 | 机械 | — |
| BreakBlocksToTarget.java | 107 | 机械 | — |
| CheckHeadshotStun.java | 54 | 机械 | — |
| CheckShockCellOrGrenadeStun.java | 59 | 机械 | SCG |
| CheckShouldAlert.java | 85 | 机械 | — |
| ConditionalBehavior.java | 108 | 机械 | — |
| CustomGunSensor.java | 48 | 机械 | — |
| DelayedMeleeAttack.java | 96 | 机械 | — |
| DelayedMeleeAttackWithEffects.java | 46 | 机械 | — |
| FlareSummon.java | 85 | 机械 | — |
| FlightDirectStroll.java | 49 | 机械 | — |
| FlyCloseToTarget.java | 27 | 机械 | — |
| FlyDirectTarget.java | 53 | 机械 | — |
| FlyDirectToTargetSink.java | 53 | 机械 | — |
| GetCloseToTarget.java | 30 | 机械 | — |
| HandleStunnedVisuals.java | 51 | 机械 | — |
| HeldGunSensor.java | 52 | 机械 | SCG |
| PatchedEntityTracker.java | 31 | 机械 | — |
| PutStunnedMobEffect.java | 49 | 机械 | — |
| RunOneOrdered.java | 38 | 机械 | — |
| SetToSprint.java | 33 | 机械 | — |
| ShootTarget.java | 77 | 机械 | — |
| ShootTargetAnimated.java | 90 | 中等 | GEO |
| SlamAttack.java | 34 | 机械 | — |
| VarRangePlayerSensor.java | 69 | 机械 | — |
| WalkUpToIdealRange.java | 68 | 机械 | — |
| package-info.java | 10 | 机械 | — |

### entity/common\client（15 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BaseEntityRenderer.java | 88 | 中等 | GEO, CLIENT |
| BoneGunFlashGeoLayer.java | 65 | 中等 | GEO, CLIENT |
| ExpandedAnimationController.java | 79 | 中等 | GEO, CLIENT |
| GunFlashHandler.java | 49 | 机械 | CLIENT |
| GunGeoLayer.java | 68 | 中等 | GEO, CLIENT |
| GunHoldingMobRenderer.java | 23 | 中等 | GEO, CLIENT |
| GunnerRenderer.java | 146 | 中等 | GEO, CLIENT |
| HeldGunFlashGeoLayer.java | 78 | 中等 | GEO, SCG, CLIENT |
| HeldGunGeoLayer.java | 36 | 中等 | GEO, CLIENT |
| HeldItemGeoLayer.java | 87 | 中等 | GEO, CLIENT |
| ItemHoldingMobRenderer.java | 18 | 中等 | GEO, CLIENT |
| MobRenderUtils.java | 62 | 机械 | SCG, CLIENT |
| PosOffsetGunFlashGeoLayer.java | 68 | 中等 | GEO, SCG, CLIENT |
| TransformableGunHoldingMobRenderer.java | 40 | 中等 | GEO, CLIENT |
| TransformableItemHoldingMobRenderer.java | 36 | 中等 | GEO, CLIENT |

### entity/common\goal（10 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AlertFactionGoal.java | 80 | 中等 | GEO |
| AnimatedGunAttackGoal.java | 31 | 中等 | GEO, SCG |
| BreakBlocksGoal.java | 110 | 机械 | — |
| FlareSummonGoal.java | 107 | 中等 | GEO, SCG, FINALIZE |
| HurtByNonFactionGoal.java | 26 | 机械 | — |
| ItemEffectMeleeAttackGoal.java | 29 | 机械 | — |
| MobHurtByNonFactionGoal.java | 124 | 机械 | — |
| StunnedGoal.java | 83 | 机械 | — |
| StunnedWithVisualGoal.java | 71 | 机械 | — |
| TridentAttackGoal.java | 43 | 机械 | — |

### entity/common\gun（14 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AttachedMountedGun.java | 57 | 机械 | — |
| CustomGunHolder.java | 6 | 机械 | — |
| CustomScorchedSimGun.java | 149 | 机械 | SCG |
| GunDamageMult.java | 7 | 机械 | — |
| HeadAttachedMountedGun.java | 40 | 机械 | — |
| HeldScorchedSimGun.java | 47 | 机械 | SCG |
| IdentityTriggerSampler.java | 20 | 机械 | — |
| IntervalTriggerSampler.java | 52 | 机械 | — |
| MarkovTriggerSampler.java | 71 | 机械 | — |
| MountedGun.java | 77 | 机械 | — |
| ProjectileFactory.java | 14 | 机械 | SCG |
| ScorchedSimGun.java | 331 | 机械 | SCG |
| SimulatedGun.java | 55 | 机械 | — |
| TriggerStateSampler.java | 13 | 机械 | — |

### entity/common\part（6 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| DebugRotatedPartEntity.java | 63 | 中等 | PART, NBT, SYNC |
| RotatedBulletProofPartEntity.java | 26 | 中等 | PART |
| RotatedDamageMultPartEntity.java | 29 | 中等 | PART |
| RotatedSegmentPartEntity.java | 128 | 中等 | PART, NBT, SCG, SYNC |
| RotatedWeakPointPartEntity.java | 16 | 中等 | PART |
| RotatedWeakPointWithZPartEntity.java | 63 | 中等 | PART |

### entity/common\raid_summoner（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| RaidSummonerEntity.java | 160 | 中等 | GEO |

### entity/fac（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ApproachTargetGoal.java | 63 | 机械 | — |
| FACEntities.java | 267 | 中等 | GEO, SCG, CLIENT, SPAWNPLACE |

### entity/fac\bluecoat（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacBluecoatEntity.java | 135 | 中等 | NBT, GEO, SCG, FINALIZE |

### entity/fac\commissar（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacCommissarAi.java | 115 | 机械 | — |
| FacCommissarEntity.java | 210 | 中等 | NBT, GEO |
| FacCommissarRenderer.java | 40 | 中等 | GEO, CLIENT |

### entity/fac\lion（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacLionAi.java | 75 | 机械 | — |
| FacLionEntity.java | 234 | 中等 | PART, GEO, SYNC |
| WeaponRangeOverride.java | 45 | 机械 | — |

### entity/fac\shovel_knight（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacShovelKnightAi.java | 88 | 机械 | — |
| FacShovelKnightEntity.java | 146 | 中等 | GEO |

### entity/fac\tank（6 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacTankAi.java | 105 | 机械 | — |
| FacTankEntity.java | 323 | 中等 | GEO, SCG |
| FacTankRenderer.java | 77 | 中等 | GEO, CLIENT |
| TankCannonFire.java | 90 | 机械 | — |
| TankCannonProjectile.java | 211 | 中等 | SCG, SYNC |
| TankCannonProjectileRenderer.java | 23 | 机械 | SCG, CLIENT |

### entity/fac\tank_buster（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacTankBusterAi.java | 66 | 机械 | — |
| FacTankBusterEntity.java | 144 | 中等 | GEO |
| FacTankBusterEyeSlitPartEntity.java | 23 | 中等 | PART |

### entity/fac\trench_goblin（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacTrenchGoblinAi.java | 58 | 机械 | — |
| FacTrenchGoblinEntity.java | 140 | 中等 | GEO |

### entity/fac\trench_sniper（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacTrenchSniperAi.java | 72 | 机械 | — |
| FacTrenchSniperEntity.java | 148 | 中等 | NBT, GEO, SCG, FINALIZE |

### entity/fac\trencher（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacTrencherEntity.java | 137 | 中等 | NBT, GEO, SCG, FINALIZE |

### entity/fac\walker（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FacWalkerAi.java | 105 | 机械 | — |
| FacWalkerEntity.java | 228 | 中等 | GEO, SCG |
| FacWalkerRenderer.java | 78 | 中等 | GEO, CLIENT |

### entity/neutral（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| NeutralEntities.java | 543 | 中等 | GEO, SCG, CLIENT, SPAWNPLACE |

### entity/neutral\end\end_dweller（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| EndDwellerEntity.java | 233 | 中等 | NBT, GEO, SYNC, FINALIZE |
| EndDwellerRenderer.java | 48 | 中等 | GEO, CLIENT |

### entity/neutral\end\end_pod（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| EndPodEntity.java | 114 | 中等 | GEO |
| EndPodRenderer.java | 28 | 中等 | GEO, CLIENT |

### entity/neutral\end\end_scorpion（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| EndScorpionEntity.java | 231 | 中等 | GEO, SYNC |
| EndScorpionMeleeAttackGoal.java | 195 | 机械 | — |
| EndScorpionRenderer.java | 60 | 重写 | GEO, CLIENT, RENDERREC |
| EndScorpionStingAttackGoal.java | 73 | 机械 | SCG |

### entity/neutral\end\end_stone_crab（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| EndStoneCrabEntity.java | 232 | 中等 | PART, GEO, SYNC |
| EndStoneCrabMeleeAttackGoal.java | 186 | 机械 | — |

### entity/neutral\nether\head_hunter（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| HeadHunterAttackGoal.java | 60 | 机械 | — |
| HeadHunterEntity.java | 157 | 中等 | GEO, SCG, SYNC |
| HeadHunterRunGoal.java | 41 | 机械 | — |

### entity/neutral\nether\netherite_eater（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| NetheriteEaterAttackGoal.java | 60 | 机械 | SCG |
| NetheriteEaterEntity.java | 176 | 中等 | GEO, SYNC |
| NetheriteEaterFireBreathGoal.java | 93 | 机械 | SCG |
| NetheriteEaterRunGoal.java | 41 | 机械 | — |

### entity/neutral\nether\nitro_beetle（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| NitroBeetleEntity.java | 228 | 中等 | GEO, SYNC |
| NitroBeetleMeleeAttackGoal.java | 179 | 机械 | — |

### entity/neutral\overworld\ammo_goblin（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AmmoGoblinEntity.java | 219 | 中等 | NBT, GEO, SYNC |

### entity/neutral\overworld\anomaly（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AnomalyAttackGoal.java | 184 | 机械 | — |
| AnomalyEntity.java | 294 | 中等 | GEO, SYNC |
| AnomalyRenderer.java | 93 | 中等 | GEO, CLIENT |

### entity/neutral\overworld\big_lump（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BigLumpEntity.java | 229 | 中等 | NBT, GEO, SCG, SYNC |
| BigLumpGunAttackGoal.java | 83 | 机械 | — |
| BigLumpMeleeAttackGoal.java | 49 | 机械 | — |

### entity/neutral\overworld\inflicted_boar（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| InflictedBoarAttackGoal.java | 48 | 机械 | SCG |
| InflictedBoarEntity.java | 109 | 中等 | GEO |

### entity/neutral\overworld\inflicted_wolf（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| InflictedWolfAttackGoal.java | 48 | 机械 | SCG |
| InflictedWolfEntity.java | 113 | 中等 | GEO |

### entity/neutral\overworld\mutant_bat（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| MutantBatEntity.java | 227 | 中等 | GEO, SYNC |
| MutantBatMeleeAttackGoal.java | 49 | 机械 | — |
| MutantBatRunGoal.java | 40 | 机械 | — |
| MutantBatScreamAttackGoal.java | 102 | 机械 | — |

### entity/piglin（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| PiglinEntities.java | 147 | 中等 | GEO, SCG, CLIENT |

### entity/piglin\common（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| Aggro.java | 47 | 机械 | — |
| AggroableMob.java | 10 | 机械 | — |
| StunnableHog.java | 36 | 机械 | — |

### entity/piglin\pigdread（6 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| DelayedMeleeAttackWithEffects.java | 49 | 机械 | — |
| MountedShotgunShoot.java | 83 | 机械 | — |
| PigdreadAi.java | 140 | 机械 | SCG |
| PigdreadEntity.java | 279 | 中等 | GEO, SCG, SYNC |
| SpeedBuff.java | 42 | 机械 | — |
| Summon.java | 42 | 机械 | — |

### entity/piglin\pigkan（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| PigkanAi.java | 88 | 机械 | — |
| PigkanDelayedMeleeAttack.java | 114 | 机械 | — |
| PigkanEntity.java | 259 | 中等 | GEO, SCG, SYNC |

### entity/piglin\piglin_blaze_kopter（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| PiglinBlazeKopterAi.java | 76 | 机械 | — |
| PiglinBlazeKopterEntity.java | 272 | 中等 | GEO, SCG, SYNC |
| PiglinBlazeKopterProjectile.java | 67 | 机械 | SCG |
| PiglinBlazeKopterRenderer.java | 21 | 中等 | GEO, CLIENT |

### entity/piglin\piglin_hog_rider（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ChargeEnemy.java | 70 | 机械 | — |
| PiglinHogRiderAi.java | 118 | 机械 | — |
| PiglinHogRiderEntity.java | 404 | 中等 | PART, NBT, GEO, SCG, SYNC, FINALIZE |
| PiglinHogRiderRenderer.java | 58 | 重写 | GEO, CLIENT, RENDERREC |

### entity/piglin\piglin_kiln_hog（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BellyCannonShoot.java | 83 | 机械 | — |
| PiglinKilnHogAi.java | 120 | 机械 | — |
| PiglinKilnHogEntity.java | 357 | 中等 | GEO, SCG, SYNC |
| PiglinKilnHogMountedShotgunGunShoot.java | 81 | 机械 | — |
| PiglinKilnHogRenderer.java | 34 | 重写 | GEO, CLIENT, RENDERREC |

### entity/piglin\piglin_nob（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| PiglinNobAi.java | 92 | 机械 | — |
| PiglinNobEntity.java | 202 | 中等 | GEO |
| PiglinNobRenderer.java | 47 | 中等 | GEO, CLIENT |
| PiglinNobShootTarget.java | 97 | 中等 | GEO |

### entity/piglin\piglin_strider_rider（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| LavaWalkMoveControl.java | 110 | 机械 | — |
| PiglinStriderRiderAi.java | 65 | 机械 | — |
| PiglinStriderRiderEntity.java | 210 | 中等 | GEO, SCG, SYNC |

### entity/piglin\piglin_warboss（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ChargeAtTarget.java | 70 | 机械 | — |
| MountedShotgunShoot.java | 81 | 机械 | — |
| PiglinWarbossAi.java | 172 | 机械 | SCG |
| PiglinWarbossEntity.java | 361 | 中等 | GEO, SCG, SYNC |
| Scream.java | 73 | 机械 | — |

### entity/projectile（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ArmoredWhaleProjectileEntity.java | 40 | 机械 | SCG |
| BigLumpProjectileEntity.java | 16 | 机械 | — |
| FireProjectile.java | 68 | 机械 | SCG |
| SoulFireBallRenderer.java | 69 | 机械 | CLIENT |
| SoulFireball.java | 168 | 机械 | SCG, CLIENT |

### entity/projectile\ink_projectile（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| InkProjectileEntity.java | 87 | 中等 | GEO |
| InkProjectileModel.java | 18 | 中等 | GEO |

### entity/projectile\net（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| NetEntity.java | 80 | 中等 | GEO |
| NetEntityRenderer.java | 37 | 中等 | GEO, CLIENT |

### entity/projectile\piglin_kiln_hog_magma_ball（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| PiglinKilnHogMagmaBall.java | 62 | 中等 | GEO |

### entity/rrc（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| RRCEntities.java | 194 | 中等 | GEO, SCG, CLIENT |

### entity/rrc\arc_psycho（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ArcPsychoEntity.java | 108 | 中等 | GEO |
| ArcPsychoEntityAttackGoal.java | 77 | 机械 | SCG |
| ArcPsychoEntityFloatGoal.java | 90 | 机械 | — |

### entity/rrc\copper_knight（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CopperKnightEntity.java | 132 | 中等 | GEO, FINALIZE |
| CopperKnightKickGoal.java | 52 | 机械 | — |

### entity/rrc\drone（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ClawAttackGoal.java | 184 | 机械 | — |
| DroneEntity.java | 273 | 中等 | PART, GEO, SYNC |
| MountedGunAttackGoal.java | 99 | 机械 | SCG |

### entity/rrc\flaminghead（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FireSpinAttackGoal.java | 77 | 机械 | — |
| FlamingHeadEntity.java | 482 | 中等 | GEO, SYNC |
| FlamingHeadRenderer.java | 62 | 中等 | GEO, CLIENT |
| RammingAttackGoal.java | 112 | 机械 | — |

### entity/rrc\gargoyle（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| GargoyleEntity.java | 270 | 中等 | PART, NBT, GEO, SCG, SYNC, FINALIZE |
| GargoyleMoveUpGoal.java | 51 | 机械 | — |

### entity/rrc\oppressor（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| OppressorEntity.java | 222 | 中等 | NBT, GEO |

### entity/rrc\scout（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ScoutEntity.java | 109 | 中等 | GEO |
| ScoutRenderer.java | 53 | 中等 | GEO, CLIENT |

### entity/rrc\scrapguard（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ScrapGuardAttackGoal.java | 89 | 中等 | GEO, SCG |
| ScrapGuardEntity.java | 134 | 中等 | GEO, SCG |

### entity/rrc\spring_junkie（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AttackAndExplodeGoal.java | 180 | 机械 | SCG |
| SpringJunkieEntity.java | 121 | 中等 | GEO, SYNC |

### entity/rrc\tallman（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| TallmanEntity.java | 149 | 中等 | GEO, FINALIZE |

### entity/turret（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| TurretAim.java | 40 | 机械 | — |
| TurretManningHandler.java | 22 | 机械 | — |
| TurretSeatEntity.java | 124 | 中等 | NBT, NETWORKHOOKS, SYNC, PACKET |

### entity/whaler（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| SwimWalkMoveControl.java | 90 | 机械 | — |
| WhalerEntities.java | 185 | 中等 | GEO, SCG, CLIENT |

### entity/whaler\armoredwhale（10 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ArmoredWhaleAi.java | 144 | 机械 | — |
| ArmoredWhaleBoneGunFlashGeoLayer.java | 64 | 中等 | GEO, CLIENT |
| ArmoredWhaleEntity.java | 307 | 中等 | PART, GEO, SCG, SYNC |
| ArmoredWhaleRenderer.java | 20 | 中等 | GEO, CLIENT |
| ArmoredWhaleShootTarget.java | 98 | 机械 | — |
| BiteAttack.java | 99 | 机械 | — |
| MountedGunProjectileEntity.java | 73 | 中等 | PART, SCG |
| Slam.java | 145 | 机械 | — |
| SplashWaterAndGrenades.java | 69 | 机械 | SCG |
| SummonInfantry.java | 42 | 机械 | — |

### entity/whaler\fishfolk（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FishFolkAi.java | 66 | 机械 | — |
| FishFolkEntity.java | 209 | 中等 | NBT, GEO, SYNC, FINALIZE |
| FishFolkModel.java | 17 | 中等 | GEO |

### entity/whaler\guardian_statue（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BeamLaserAttackGoal.java | 86 | 机械 | — |
| GuardianLaserAttackGoal.java | 75 | 机械 | — |
| GuardianStatueEntity.java | 417 | 中等 | PART, NBT, GEO, SYNC, FINALIZE |
| GuardianStatueRenderer.java | 261 | 重写 | GEO, CLIENT, RENDERREC |
| SlideToTargetGoal.java | 40 | 机械 | — |

### entity/whaler\lion_fish（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BiteAttack.java | 35 | 机械 | — |
| LionFishAi.java | 76 | 机械 | — |
| LionFishEntity.java | 219 | 中等 | PART, GEO, SYNC |
| SplashPoison.java | 96 | 机械 | — |

### entity/whaler\pufficus（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| PufficusAi.java | 106 | 机械 | — |
| PufficusEntity.java | 197 | 中等 | GEO |
| ThrowNet.java | 104 | 机械 | — |

### entity/whaler\salmonsaur（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| SalmonsaurAi.java | 65 | 机械 | — |
| SalmonsaurEntity.java | 200 | 中等 | PART, GEO |
| SalmonsaurRenderer.java | 36 | 中等 | GEO, CLIENT |

### entity/whaler\tentacliator（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| InkAttack.java | 107 | 中等 | GEO |
| TentacliatorAi.java | 72 | 机械 | — |
| TentacliatorEntity.java | 208 | 中等 | NBT, GEO, SYNC, FINALIZE |
| TentacliatorModel.java | 17 | 中等 | GEO |
| TentacliatorShootTarget.java | 95 | 中等 | GEO |

### entity/whaler\turtleman（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| TurtlemanAi.java | 108 | 机械 | — |
| TurtlemanEntity.java | 338 | 中等 | PART, GEO, SCG, SYNC |
| TurtlemanRenderer.java | 16 | 中等 | GEO, CLIENT |

### entity/wreckers（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ApproachTargetGoal.java | 62 | 机械 | — |
| WreckersEntities.java | 157 | 中等 | GEO, SCG, CLIENT |

### entity/wreckers\wrecker_biker（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ChargeTargetGoal.java | 144 | 机械 | SCG |
| WreckerBikerEntity.java | 206 | 中等 | NBT, GEO, SYNC, FINALIZE |

### entity/wreckers\wrecker_blue（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerBlueEntity.java | 174 | 中等 | GEO |
| WreckerBlueMeleeGoal.java | 46 | 机械 | — |

### entity/wreckers\wrecker_dozer（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerDozerChargeGoal.java | 130 | 机械 | — |
| WreckerDozerEntity.java | 313 | 中等 | GEO, SCG, SYNC, FINALIZE |
| WreckerDozerRenderer.java | 43 | 中等 | GEO, CLIENT |
| WreckerDozerSummonGoal.java | 66 | 中等 | FINALIZE |

### entity/wreckers\wrecker_green（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerGreenEntity.java | 179 | 中等 | GEO, SYNC |
| WreckerGreenRenderer.java | 28 | 中等 | GEO, CLIENT |

### entity/wreckers\wrecker_helicube（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| FlyCloseToTargetGoal.java | 59 | 机械 | — |
| WreckerHelicubeEntity.java | 387 | 中等 | NBT, GEO, SCG, SYNC, FINALIZE |
| WreckerHelicubeRenderer.java | 73 | 中等 | GEO, CLIENT |

### entity/wreckers\wrecker_jumbo（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerJumboAi.java | 92 | 机械 | — |
| WreckerJumboEntity.java | 190 | 中等 | PART, GEO |

### entity/wreckers\wrecker_red（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerRedEntity.java | 162 | 中等 | GEO, SYNC |

### entity/wreckers\wrecker_turret_gunner（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WreckerTurretGunnerEntity.java | 184 | 中等 | NBT, GEO, SCG, SYNC, FINALIZE |
| WreckerTurretGunnerRenderer.java | 23 | 中等 | GEO, CLIENT |

### item（18 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ArmorPiercing.java | 5 | 机械 | — |
| ArmorPiercingHandler.java | 34 | 机械 | — |
| AtlanticMaceItem.java | 62 | 中等 | GEO, CLIENTEXT |
| CavalrySaberItem.java | 86 | 机械 | SCG |
| HoldAttack.java | 15 | 机械 | — |
| HoldAttackHandler.java | 69 | 机械 | CLIENT |
| HurtEffects.java | 10 | 机械 | — |
| MedalItem.java | 147 | 机械 | CLIENT |
| ModItems.java | 628 | 机械 | SCG |
| MultiUseHealingItem.java | 119 | 机械 | SCG |
| NetheriteChainAxeItem.java | 117 | 中等 | GEO, SCG, CLIENTEXT |
| SimpleCustomRenderer.java | 21 | 中等 | CLIENT, CLIENTEXT |
| SpearShovelItem.java | 45 | 中等 | GEO, CLIENTEXT |
| SuperRaidFlareItem.java | 35 | 机械 | SCG |
| WreckerRpgItem.java | 85 | 中等 | NBT, GEO, SCG, CLIENTEXT |
| WreckerSledgeHammerItem.java | 38 | 机械 | — |
| WreckingToolItem.java | 150 | 中等 | GEO, CLIENT, CLIENTEXT |
| WreckingToolItemHandler.java | 16 | 机械 | — |

### item/armor（16 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ArmorExtraModiferHandler.java | 174 | 中等 | NBT, SCG |
| ArmorSet.java | 157 | 机械 | — |
| ArmorSetPart.java | 8 | 机械 | — |
| ArmorSetPartItem.java | 48 | 机械 | CLIENT |
| ArmorSetPartWithExtrasItem.java | 47 | 机械 | — |
| ArmorSets.java | 62 | 中等 | NBT, SCG |
| BaseArmorSetPartRenderer.java | 47 | 中等 | GEO, CLIENT |
| GeoArmorSetPartItem.java | 67 | 中等 | GEO, CLIENT, CLIENTEXT |
| GoldenIdolArmorSet.java | 36 | 机械 | — |
| JuggernautChestplateItem.java | 48 | 中等 | GEO, CLIENT, CLIENTEXT |
| JuggernautChestplateModel.java | 32 | 中等 | GEO |
| ModArmorMaterials.java | 222 | 机械 | SCG |
| PioneerChestplateItem.java | 38 | 机械 | — |
| RitualArmorSet.java | 51 | 中等 | NBT |
| RitualChestplateItem.java | 68 | 机械 | CLIENT |
| WreckerChestplateItem.java | 74 | 中等 | GEO, CLIENT |

### item/curios（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| MedKitRenderer.java | 43 | 机械 | CLIENT |
| MediPackRenderer.java | 36 | 机械 | CLIENT |

### item/custom（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| BandageItem.java | 169 | 机械 | SCG |
| WreckerTurretItem.java | 12 | 机械 | — |

### mixin（16 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AIGunEventMixin.java | 46 | 重写 | MIXIN, SCG |
| EntityMixin.java | 26 | 重写 | MIXIN |
| GunAttackGoalAccessor.java | 24 | 重写 | MIXIN, SCG |
| GunnerMobSpawnerMixin.java | 22 | 重写 | MIXIN, SCG |
| LightningProjectileEntityMixin.java | 40 | 重写 | MIXIN, PART, SCG |
| LivingEntityMixin.java | 123 | 重写 | MIXIN, SCG |
| MineUnitBlockEntityMixin.java | 26 | 重写 | MIXIN, SCG |
| ModCommandsMixin.java | 44 | 重写 | MIXIN, SCG |
| OceanMonumentEntryRoomMixin.java | 27 | 重写 | MIXIN |
| ProjectileEntityMixin.java | 171 | 重写 | MIXIN, PART, SCG |
| RaidFlareEntityMixin.java | 40 | 重写 | MIXIN, SCG |
| RaidManagerMixin.java | 55 | 重写 | MIXIN, SCG |
| RaidSaveDataAccessor.java | 22 | 重写 | MIXIN, SCG |
| RecoilHandlerMixin.java | 41 | 重写 | MIXIN, ENCH, SCG, CLIENT |
| ResourceOrTagKeyArgumentMixin.java | 50 | 重写 | MIXIN |
| SignalBeaconEntityMixin.java | 63 | 重写 | MIXIN, SCG |

### network（4 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| GunFlashMessage.java | 61 | 重写 | NET |
| HoldAttackMessage.java | 38 | 重写 | NET |
| SCGEPacketHandler.java | 37 | 重写 | NET |
| TurretTriggerMessage.java | 43 | 重写 | NET |

### particle（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ModParticleTypes.java | 47 | 机械 | — |

### raid（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| NightRaidManager.java | 6 | 机械 | — |
| WaveRaidData.java | 242 | 机械 | SCG |
| WaveRaidManager.java | 236 | 中等 | NBT |
| WaveRaidState.java | 259 | 中等 | NBT |
| WaveRaidUtil.java | 159 | 机械 | — |

### sounds（11 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| AsgharianSounds.java | 81 | 机械 | — |
| COGSounds.java | 58 | 机械 | — |
| FACSounds.java | 98 | 机械 | — |
| InterruptibleVoice.java | 11 | 机械 | — |
| ModSounds.java | 25 | 机械 | — |
| NeutralSounds.java | 97 | 机械 | — |
| PiglinSounds.java | 110 | 机械 | — |
| RRCSounds.java | 102 | 机械 | — |
| WhalerSounds.java | 127 | 机械 | — |
| WreckerDeathSounds.java | 36 | 机械 | — |
| WreckersSounds.java | 78 | 机械 | — |

### tag（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| Tags.java | 12 | 机械 | — |

### worldgen（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ModConfiguredFeatures.java | 89 | 重写 | WORLDGEN |
| ModPlacedFeatures.java | 65 | 重写 | WORLDGEN |

### worldgen/biome（3 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ModBiomes.java | 82 | 重写 | WORLDGEN |
| ModOverworldRegion.java | 72 | 重写 | WORLDGEN |
| ModTerrablender.java | 13 | 重写 | WORLDGEN |

### worldgen/biome\surface（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ModSurfaceRules.java | 40 | 重写 | WORLDGEN |

### worldgen/structure（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| ModStructureProcessors.java | 23 | 重写 | WORLDGEN |
| ModStructures.java | 53 | 重写 | WORLDGEN, STRUCT |

### worldgen/structure\processors（2 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CogFactoryProcessor.java | 31 | 重写 | WORLDGEN |
| WarshipProcessor.java | 27 | 重写 | WORLDGEN |

### worldgen/structure\structures（5 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| CampingSiteStructure.java | 69 | 重写 | WORLDGEN, STRUCT |
| DestroyedMinesStructure.java | 64 | 重写 | WORLDGEN, STRUCT |
| MineTrenchStructure.java | 65 | 重写 | WORLDGEN, STRUCT |
| TrenchesAnchorStructure.java | 102 | 重写 | WORLDGEN, STRUCT |
| WaterAvoidingStructure.java | 100 | 重写 | WORLDGEN, STRUCT |

### worldgen/tree（1 文件）

| 文件 | 行 | 分级 | 标记 |
|---|---:|---|---|
| WarzoneSpruceTreeGrower.java | 17 | 重写 | WORLDGEN |
