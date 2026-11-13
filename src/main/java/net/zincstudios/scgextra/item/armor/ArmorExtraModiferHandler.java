package net.zincstudios.scgextra.item.armor;

import net.minecraft.resources.ResourceLocation;

import net.zincstudios.scgextra.attributes.SCGEAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.ribs.scguns.attributes.SCAttributes;
import top.ribs.scguns.init.ModEffects;

public class ArmorExtraModiferHandler {
    private static final Map<Holder<Attribute>, Map<Holder<Attribute>, AttributeModifier>> FULL_SET_BONUSES = Map.<Holder<Attribute>, Map<Holder<Attribute>, AttributeModifier>>of(
        SCGEAttributes.OPPRESSOR, Map.<Holder<Attribute>, AttributeModifier>of(SCGEAttributes.UNDEAD_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:oppressor_full_armor_add_undead_damage_mult"), 10, AttributeModifier.Operation.ADD_VALUE)),
        SCGEAttributes.COMMISSAR, Map.of(SCAttributes.RELOAD_SPEED, new AttributeModifier(ResourceLocation.parse("scgextra:commissar_full_armor_reload_speed"), 0.2, AttributeModifier.Operation.ADD_VALUE)),
        SCGEAttributes.WRECEKR, Map.of(
            SCGEAttributes.GENERAL_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:wrecker_full_armor_damage_mult"), 20, AttributeModifier.Operation.ADD_VALUE),
            SCGEAttributes.BULLET_DAMAGE_TAKEN_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:wrecker_full_armor_additional_bullet_damage_taken"), 20, AttributeModifier.Operation.ADD_VALUE)
        ),
        SCGEAttributes.GOLDEN_IDOL, Map.<Holder<Attribute>, AttributeModifier>of(SCGEAttributes.DEBUFF_ZONE, new AttributeModifier(ResourceLocation.parse("scgextra:golden_idol_full_armor_add_debuff_zone"), 1.0, AttributeModifier.Operation.ADD_VALUE)),
        SCGEAttributes.JUGGERNAUT, Map.of(
            SCGEAttributes.RECOIL_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:juggernaut_full_armor_reduce_recoil"), -10, AttributeModifier.Operation.ADD_VALUE),
            Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.parse("scgextra:juggernaut_full_armor_reduce_speed"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        ),
        SCGEAttributes.PIONEER, 
        Map.of(
            SCGEAttributes.BULLET_ADDITIONAL_CRIT_CHANCE, new AttributeModifier(ResourceLocation.parse("scgextra:pioneer_full_armor_additional_crit_chance_mult"), 10, AttributeModifier.Operation.ADD_VALUE),
            SCGEAttributes.BULLET_ADDITIONAL_CRIT_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:pioneer_full_armor_additional_crit_damage_mult"), 10, AttributeModifier.Operation.ADD_VALUE)
        )
    );
    private static final Map<Holder<Attribute>, Map<Holder<Attribute>, AttributeModifier>> HALF_SET_BONUSES = Map.<Holder<Attribute>, Map<Holder<Attribute>, AttributeModifier>>of(
        SCGEAttributes.OPPRESSOR, Map.<Holder<Attribute>, AttributeModifier>of(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ResourceLocation.parse("scgextra:oppressor_half_armor_add_knockback_resistance"), 1, AttributeModifier.Operation.ADD_VALUE)),
        SCGEAttributes.COMMISSAR, Map.<Holder<Attribute>, AttributeModifier>of(SCGEAttributes.BULLET_DAMAGE_TAKEN_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:comissar_half_armor_reduce_bullet_damage"), -10, AttributeModifier.Operation.ADD_VALUE))
    );
    public static final List<Holder<MobEffect>> MOB_EFFECTS = List.of(
        MobEffects.POISON,
        MobEffects.WITHER,
        MobEffects.BLINDNESS,
        MobEffects.CONFUSION,
        MobEffects.MOVEMENT_SLOWDOWN,
        ModEffects.LACERATED,
        ModEffects.SULFUR_POISONING
    );

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        
        if (event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();

        if(player.hasEffect(MobEffects.LEVITATION) && player.getAttribute(SCGEAttributes.PIONEER)!=null && player.getAttribute(SCGEAttributes.PIONEER).getValue()>=1.0){
            player.removeEffect(MobEffects.LEVITATION);
        }

        if(player.hasEffect(ModEffects.LACERATED) && player.getAttribute(SCGEAttributes.JUGGERNAUT)!=null && player.getAttribute(SCGEAttributes.JUGGERNAUT).getValue()>=1.0){
            player.removeEffect(ModEffects.LACERATED);
        }

        if(player.isOnFire() && player.getAttribute(SCGEAttributes.GOLDEN_IDOL)!=null && player.getAttribute(SCGEAttributes.GOLDEN_IDOL).getValue()>=1.0){
            player.setRemainingFireTicks(0);
        }
        
        if(player.getAttributeValue(SCGEAttributes.ENLIGHTENED)>=1.0){
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
        }

        if (event.getEntity().tickCount % 10 != 0) return;


        for(Holder<Attribute> main : FULL_SET_BONUSES.keySet()){
            if(player.getAttributeValue(main)>=2){
                for(Holder<Attribute> key : FULL_SET_BONUSES.get(main).keySet()){
                    if(!player.getAttribute(key).hasModifier(FULL_SET_BONUSES.get(main).get(key).id())){
                        player.getAttribute(key).addTransientModifier(FULL_SET_BONUSES.get(main).get(key));
                    }
                }
            }else{
                for(Holder<Attribute> key : FULL_SET_BONUSES.get(main).keySet()){
                    if(player.getAttribute(key).hasModifier(FULL_SET_BONUSES.get(main).get(key).id())){
                        player.getAttribute(key).removeModifier(FULL_SET_BONUSES.get(main).get(key).id());
                    }
                }
            }
        }
        for(Holder<Attribute> main : HALF_SET_BONUSES.keySet()){
            if(player.getAttributeValue(main)>=1){
                for(Holder<Attribute> key : HALF_SET_BONUSES.get(main).keySet()){
                    if(!player.getAttribute(key).hasModifier(HALF_SET_BONUSES.get(main).get(key).id())){
                        player.getAttribute(key).addTransientModifier(HALF_SET_BONUSES.get(main).get(key));
                    }
                }
            }else{
                for(Holder<Attribute> key : HALF_SET_BONUSES.get(main).keySet()){
                    if(player.getAttribute(key).hasModifier(HALF_SET_BONUSES.get(main).get(key).id())){
                        player.getAttribute(key).removeModifier(HALF_SET_BONUSES.get(main).get(key).id());
                    }
                }
            }
        }

        if(player.getAttribute(SCGEAttributes.LEVIATHAN)!=null && player.getAttributeValue(SCGEAttributes.LEVIATHAN)>=1.0){
            CompoundTag tag = player.getPersistentData();
            if(player.level().getGameTime()>tag.getLong("leviathanClenseCooldownEnd")){
                for (Holder<MobEffect> entry : MOB_EFFECTS) {
                    if(player.hasEffect(entry)){
                        player.removeEffect(entry);
                        tag.putLong("leviathanClenseCooldownEnd", player.level().getGameTime() + 200);
                    }
                }
            }
        }

        if(player.getAttribute(SCGEAttributes.GOLDEN_IDOL)!=null && player.getAttribute(SCGEAttributes.GOLDEN_IDOL).getValue()>=1.0){
            List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5),  e->!(e instanceof Enemy));
            for(LivingEntity le : list){
                le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100));
                le.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100));
            }
        }
        if(player.getAttribute(SCGEAttributes.DEBUFF_ZONE)!=null && player.getAttribute(SCGEAttributes.DEBUFF_ZONE).getValue()>=1.0){
            List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(10), e->e instanceof Enemy);
            for(LivingEntity le : list){
                le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            }
        }
    }
    public static void onTooltip(ItemTooltipEvent event) {
        if(event.getItemStack().getItem() instanceof ArmorSetPartWithExtrasItem ai){
            String name = "+0.5 "+ai.armorTypeName;
            //WHY IS THIS SO HARD!?
            Pattern pattern = Pattern.compile("([-+](?:[0-9]+\\.[0-9]+|[0-9]+)) (Bullet Damage Taken Multiplier|Additional Crit Chance|Bullet Gravity Multiplier|Undead Damage Multiplier|Magic Damage Taken|General Damage Multiplier|Additional Crit Damage Multiplier|Recoil Multiplier)", Pattern.CASE_INSENSITIVE);
            List<Component> list = new ArrayList<>();

            for (Component c : event.getToolTip()) {                
                if (pattern.matcher(c.getString()).find()) {
                    list.add(Component.literal(pattern.matcher(c.getString()).replaceAll("$1% $2")).withStyle(c.getStyle()));
                } else {
                    //can't bother with regex anymore
                    if(c.getString().toLowerCase().contains("firearm reload speed")){
                        String[] arr = c.getString().split(" ");
                        String sign = arr[0].substring(0, 1);
                        float num = Float.valueOf(arr[0].substring(1));
                        list.add(Component.literal(sign+""+(num*100)+"% "+arr[1]+" "+arr[2]+" "+arr[3]).withStyle(c.getStyle()));
                    }else{
                        list.add(c);
                    }
                }
            }

            event.getToolTip().clear();
            event.getToolTip().addAll(list);

            event.getToolTip().removeIf(e->{
                if(e.getString().toLowerCase().contains(name.toLowerCase()))return true;
                return false;
            });
        }
    }
}
