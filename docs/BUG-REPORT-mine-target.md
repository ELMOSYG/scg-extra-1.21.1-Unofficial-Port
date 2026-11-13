# Bug report draft — mine explosion makes FAC mobs shoot at a dead/absent target

This is a 1.20.1 issue in the original SCG Extra 4.0.0 (reproduced on the 1.21 port as
well), not a port-specific regression.

---

## Issue text (paste as GitHub issue / Discord report)

**Title:** `scguns:mine_unit` explosion makes FAC faction mobs keep firing at the mine
after it has already exploded

**Environment**
- Minecraft 1.20.1, Forge 47.4.x
- SCG Extra 4.0.0
- Scorched Guns (the version bundled/required by SCG Extra 4.0.0)

**Steps to reproduce**
1. Generate a `scgextra:trenches` structure (it contains `scguns:mine_unit` blocks).
2. Lure a FAC faction mob next to a mine — easiest is a shooter such as
   `scgextra:fac_trencher`, `scgextra:fac_trench_sniper`, `scgextra:fac_bluecoat`
   or `scgextra:fac_commissar`.
3. Let the mine trigger and explode on the mob.

**Expected**
The mob takes explosion damage, maybe briefly retaliates at the player/placer who armed
the mine, then returns to normal AI.

**Actual**
The FAC mob immediately starts firing its gun at the mine's position and keeps firing.
The mine entity is already gone, so it is shooting at nothing. The gunfire sound loops
continuously and is extremely loud/annoying; the mob also stays locked in place instead
of chasing the player.

**Impact**
- Constant gunfire noise around any trenches/mine field.
- FAC shooters stop fighting players and waste ammo at an empty block position.
- Easy to reproduce in normal survival, no commands or special setup needed.

**Suspected cause (for the author, not required for the report)**
The mine explosion's damage is attributed to an entity that is no longer valid (the mine
itself or a placeholder/owner entity). FAC mobs use the brain-based gun AI:
`AttackLastHurtIfNear` copies `HURT_BY_ENTITY` into `ATTACK_TARGET`, and the shoot
behaviors then keep firing at that target. If the target is removed/not in the level,
`StopAttackingIfTargetInvalid` does not clear it (or the target object still reports
`isAlive()`), so the mob keeps shooting at the last known position.

Possible fixes on the SCG Extra side:
1. In `AttackLastHurtIfNear` / the FAC targeting predicate, ignore `HURT_BY_ENTITY` when:
   - the entity `isRemoved()` or not in the same level/dimension, or
   - the damage source is an explosion with no valid `LivingEntity` attacker
     (`damageSource.is(DamageTypeTags.IS_EXPLOSION)`).
2. Clear `ATTACK_TARGET` when the target is not in the world (extend the existing
   `StopAttackingIfTargetInvalid` predicate with `target.level() == mob.level()` and
   `!target.isRemoved()`).
3. If the mine is supposed to be blamed on the player who placed it, make the explosion
   use `placerUUID` as the damage source instead of a temporary/placeholder entity.

**Additional note**
On 1.21, `ItemEntity#getControllingPassenger()` always returns `null`, so any code path
that expects a `LivingEntity` owner from it will also need a null-safe fallback. The
1.20.1 report above stands on its own, but the same symptom appears on the 1.21 port
because the explosion ends up with an invalid attacker entity.

---

## Short version (for Discord / a quick message)

> `scguns:mine_unit` explosions make FAC shooters (trencher / trench sniper / bluecoat /
> commissar) keep firing at the mine after it exploded. They lock onto a target that is
> already gone, spam gunfire sounds forever and stop chasing the player. Reproduce:
> generate `scgextra:trenches`, lure a FAC mob onto a mine. Looks like the explosion's
> damage source leaves an invalid `HURT_BY_ENTITY`/`ATTACK_TARGET` that the brain gun AI
> never clears. `StopAttackingIfTargetInvalid` should also reject removed/out-of-level
> targets, or the mine explosion should be blamed on the placer player instead.
