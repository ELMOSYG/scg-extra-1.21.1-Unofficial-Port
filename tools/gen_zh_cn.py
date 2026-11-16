# -*- coding: utf-8 -*-
"""Generate assets/scgextra/lang/zh_cn.json from en_us.json using the glossary
at scgun-0.5.5-1.21.1-neoforge/docs/scgextra-4.0.0-glossary.md."""
import json
import collections
import os
import sys

ROOT = r"E:\mod\scg-extra-1.21"
EN = os.path.join(ROOT, r"src\main\resources\assets\scgextra\lang\en_us.json")
ZH = os.path.join(ROOT, r"src\main\resources\assets\scgextra\lang\zh_cn.json")

en = json.load(open(EN, encoding="utf-8"), object_pairs_hook=collections.OrderedDict)

ENTITY = {
    "fish_folk": "鱼人",
    "turtleman": "龟人",
    "salmonsaur": "鲑鱼骑士",
    "tentacliator": "触手撕裂者",
    "armored_whale": "装甲鲸",
    "pufficus": "河豚兽",
    "guardian_statue": "守卫雕像",
    "lion_fish": "狮鱼",
    "drone": "锈岭无人机",
    "scout": "锈岭侦察兵",
    "spring_junkie": "锈岭弹簧疯子",
    "arc_psycho": "锈岭电弧疯子",
    "tallman": "锈岭高个子",
    "oppressor": "锈岭压迫者",
    "flaming_head": "锈岭燃烧头颅",
    "copper_knight": "锈岭铜骑士",
    "scrap_guard": "锈岭废料卫士",
    "gargoyle": "锈岭石像鬼",
    "fac_trencher": "联邦战壕兵",
    "fac_bluecoat": "联邦蓝衣军",
    "fac_trench_goblin": "联邦战壕哥布林",
    "fac_trench_sniper": "联邦战壕狙击手",
    "fac_shovel_knight": "联邦铲骑士",
    "fac_tank_buster": "联邦反坦克手",
    "fac_lion": "联邦狮",
    "fac_commissar": "联邦政委",
    "fac_walker": "联邦步行机",
    "fac_tank": "联邦坦克",
    "failed_one": "弃造者",
    "asghar_surgeon": "阿斯加里安精英外科医生",
    "asghar_worker": "阿斯加里安工兵",
    "asghar_flamer": "阿斯加里安喷火兵",
    "candle_fiend": "烛焰魔",
    "soul_ripper": "灵魂撕裂者",
    "wrecker_red": "扫荡者·红",
    "wrecker_blue": "扫荡者·蓝",
    "wrecker_green": "扫荡者·绿",
    "wrecker_jumbo": "扫荡者·巨型",
    "wrecker_helicube": "扫荡者·直升方块",
    "wrecker_turret_gunner": "扫荡者炮台",
    "wrecker_dozer": "扫荡者·推土机",
    "wrecker_biker": "扫荡者·摩托手",
    "turret_seat": "炮塔座位",
    "cog_vulture": "COG 秃鹫侦察兵",
    "cog_devastator": "COG 毁灭者",
    "cog_bombardier": "COG 火箭炮手",
    "cog_gigantes": "COG 巨型母舰",
    "cog_venator": "COG 猎手",
    "cog_centipede": "COG 蜈蚣",
    "cog_juggernaut": "COG 主宰",
    "cog_trenchbleeder": "COG 战壕清剿者",
    "inflicted_boar": "感染野猪",
    "inflicted_wolf": "感染狼",
    "ammo_goblin": "弹药小鬼",
    "big_lump": "大肉块",
    "mutant_bat": "变异蝙蝠",
    "anomaly": "异常体",
    "nitro_beetle": "硝化甲虫",
    "head_hunter": "猎头者",
    "netherite_eater": "下界合金吞噬者",
    "end_pod": "末地荚",
    "end_dweller": "末地潜伏者",
    "end_stone_crab": "末地石蟹",
    "end_scorpion": "末地蝎",
    "piglin_nob": "猪灵贵族",
    "piglin_strider_rider": "猪灵炽足兽骑士",
    "piglin_hog_rider": "猪灵疣猪骑士",
    "piglin_blaze_kopter": "猪灵烈焰旋翼机",
    "pigkan": "猪坎",
    "piglin_kiln_hog": "猪灵熔炉疣猪",
    "piglin_kiln_hog_magma_ball": "猪灵熔炉疣猪岩浆球",
    "pigdread": "惧猪",
    "piglin_warboss": "猪灵战酋",
}

# 套装：id -> (名称, 散件(2件), 全套(4件))
SETS = {
    "oppressor": ("压迫者", "提供 100% 击退抗性", "对亡灵伤害 +10%"),
    "commissar": ("政委", "受到的子弹伤害 -10%", "装填速度 +20%"),
    "wrecker": ("破坏者", "攻击或受击时使敌人撕裂", "子弹伤害 +20%，受到的子弹伤害 +20%"),
    "leviathan": ("利维坦", "每 10 秒净化一次减益", "获得水下呼吸与海豚的恩惠"),
    "golden_idol": ("黄金偶像", "为 5 格内的友方与中立生物恢复生命", "使 10 格内的敌对生物虚弱"),
    "enlightened": ("开悟者", "获得生命恢复，且不会被幽匿感测体侦测", "受击时获得治疗 I（3 秒冷却）"),
    "juggernaut": ("重装兵", "免疫撕裂", "后坐力 -10%，移动速度 -10%"),
    "ritual": ("仪式", "受击时获得生命恢复 I（4 秒冷却）", "死亡时复活（冷却 20 分钟）"),
    "pioneer": ("先驱", "免疫漂浮，并获得鞘翅飞行能力", "暴击率 +10%，子弹暴击伤害 +10%"),
}

ITEM = {
    "end_shell": "末地甲壳",
    "atlantic_mace": "大西洋重锤",
    "cavalry_saber": "骑兵军刀",
    "spear_shovel": "矛铲",
    "wrecking_tool": "破拆工具",
    "wrecker_sledge_hammer": "破坏者大锤",
    "wrecker_rpg": "破坏者火箭筒",
    "bandage": "绷带",
    "medkit": "急救箱",
    "medipack": "医疗包",
    "netherite_chain_axe": "下界合金链锯斧",
    "single_use_desc": "一次性物品",
    "medipack.desc": "提升普通绷带的效果",
    "medal_of_survivor": "幸存者勋章",
    "medal_of_iron_will": "钢铁意志勋章",
    "medal_of_defiance": "抗命勋章",
    "medal_of_wonder": "奇迹勋章",
    "medal_of_fiery_rage": "炽怒勋章",
    "medal_of_obedience": "服从勋章",
    "medal_of_cruelty": "残虐勋章",
    "medal_of_enlightenment": "开悟勋章",
    "medal_of_conqueror": "征服者勋章",
    "medal_of_survivor.description": "提供 5 点护甲",
    "medal_of_iron_will.description": "提供 20% 子弹伤害减免",
    "medal_of_defiance.description": "装填速度与移动速度 +20%",
    "medal_of_wonder.description": "获得抗性 I，并免疫中毒与硫中毒",
    "medal_of_fiery_rage.description": "获得力量 II 与生命恢复 I",
    "medal_of_obedience.description": "提供 3 点护甲韧性，后坐力 -30%",
    "medal_of_cruelty.description": "子弹伤害 +10%",
    "medal_of_enlightenment.description": "最大生命值 +8",
    "medal_of_conqueror.description": "暴击率 +20%，子弹重力 -20%",
}

SUPER_FLARE = {
    "antique": "古典", "frontier": "边疆", "copper": "铜", "iron": "铁",
    "wrecker": "扫荡者", "diamond_steel": "钻石钢", "treated_brass": "烈焰黄铜",
    "gold": "黄金", "sculk": "幽匿", "ocean": "海洋",
}

ARMOR_PIECES = {"helmet": "头盔", "chestplate": "胸甲", "leggings": "护腿", "boots": "靴子"}

BLOCK = {
    "barbed_wires": "带刺铁丝网",
    "electrical_wires": "电网",
    "wrecker_turret": "固定炮塔",
    "wrecker_turret.no_ammo": "没有弹药",
    "wrecker_turret.ammo_type": "弹药：%s",
    "wrecker_turret.desc": "右键操作",
    "wrecker_turret.occupied": "已经有其他人在操作这座炮塔。",
    "mob_spawner": "利维坦刷怪笼",
}

EFFECT = {
    "ink_effect": "墨汁",
    "glowing_ink_effect": "发光墨汁",
    "mortal": "凡躯",
    "enlightenment": "开悟",
    "whaler_regen_effect": "利维坦再生",
}

ATTRIBUTE = {
    "recoil_multiplier": "后坐力修正 (%)",
    "bullet_damage_taken_multiplier": "受到的子弹伤害倍率",
    "bullet_additional_crit_chance": "额外暴击率",
    "bullet_gravity_multiplier": "子弹重力倍率",
    "undead_damage_multiplier": "对亡灵伤害倍率",
    "magic_damage_reduction": "受到的魔法伤害",
    "general_damage_mult": "通用伤害倍率",
    "debuff_zone": "减益领域",
    "oppressor": "压迫者",
    "commissar": "政委",
    "wrecker": "破坏者",
    "leviathan": "利维坦",
    "golden_idol": "黄金偶像",
    "enlightened": "开悟者",
    "juggernaut": "重装兵",
    "ritual": "仪式",
    "pioneer": "先驱",
    "bullet_additional_crit_damage_multiplier": "额外暴击伤害倍率",
}

RAID_LABEL = {
    "whaler": "利维坦联合军突袭",
    "rrc": "锈岭突袭",
    "fac": "联邦突袭",
    "asgharian": "阿斯加里安突袭",
    "cog": "COG 突袭",
    "wrecker": "扫荡者突袭",
    "piglin": "猪灵突袭",
}

RAID_ANNOUNCE = {
    "whaler": "利维坦联合军正从深渊中袭来！",
    "rrc": "锈岭的残余部队正在突袭！",
    "fac": "联邦军备联合会的残余势力正在发动袭击！",
    "asgharian": "阿斯加里安军团正在行军！",
    "cog": "COG 营队正在推进！",
    "wrecker": "扫荡者突袭正在进行！",
    "piglin": "猪灵军阀正在突袭！",
}

SUB_ENTITY = {
    "leviathan_syndicate.fish_folk": "鱼人",
    "leviathan_syndicate.tentacliator": "触手撕裂者",
    "leviathan_syndicate.salmonsaur": "鲑鱼骑士",
    "leviathan_syndicate.lion_fish": "狮鱼",
    "leviathan_syndicate.armored_whale": "装甲鲸",
    "leviathan_syndicate.pufficus": "河豚兽",
    "leviathan_syndicate.turtleman": "龟人",
    "leviathan_syndicate.guardian_statue": "守卫雕像",
    "tallman": "锈岭高个子",
    "oppressor": "锈岭压迫者",
    "spring_junkie": "锈岭弹簧疯子",
    "flaming_head": "锈岭燃烧头颅",
    "drone": "锈岭无人机",
    "knight": "锈岭铜骑士",
    "scout": "锈岭侦察兵",
    "arc_psycho": "锈岭电弧疯子",
    "gargoyle": "锈岭石像鬼",
    "cog_devastator": "COG 毁灭者",
    "cog_bombardier": "COG 火箭炮手",
    "cog_general": "COG 统帅",
    "cog_venator": "COG 猎手",
    "cog_gigantes": "COG 巨型母舰",
    "cog_juggernaut": "COG 主宰",
    "cog_centipede": "COG 蜈蚣",
    "cog_trenchbleeder": "COG 战壕清剿者",
    "fac_lion": "联邦狮",
    "fac_trench_sniper": "联邦战壕狙击手",
    "fac_shovel_knight": "联邦铲骑士",
    "fac_walker": "联邦步行机",
    "fac_trench_goblin": "联邦战壕哥布林",
    "fac_commissar": "联邦政委",
    "fac_trencher": "联邦战壕兵",
    "fac_tank_buster": "联邦反坦克手",
    "fac_bluecoat": "联邦蓝衣军",
    "fac_tank": "联邦坦克",
    "wrecker_jumbo": "扫荡者·巨型",
    "wrecker_dozer": "扫荡者·推土机",
    "wrecker_blue": "扫荡者·蓝",
    "wrecker_helicube": "扫荡者·直升方块",
    "wreckers": "扫荡者",
    "wrecking_tool": "破拆工具",
    "wrecker_biker.bike": "扫荡者摩托",
    "wrecker_biker.rider": "扫荡者摩托手",
    "asghar_flamer": "阿斯加里安喷火兵",
    "asghar_worker": "阿斯加里安工兵",
    "asghar_surgeon": "阿斯加里安精英外科医生",
    "soul_ripper": "灵魂撕裂者",
    "candle_fiend": "烛焰魔",
    "end_dweller": "末地潜伏者",
    "end_stone_crab": "末地石蟹",
    "end_scorpion": "末地蝎",
    "netherite_eater": "下界合金吞噬者",
    "mutant_bat": "变异蝙蝠",
    "big_lump": "大肉块",
    "ammo_goblin": "弹药小鬼",
    "end_pod": "末地荚",
    "piglin.piglin_nob": "猪灵贵族",
    "piglin.piglin_hog_rider": "猪灵疣猪骑士",
    "piglin.piglin_strider_rider": "猪灵炽足兽骑士",
    "piglin.piglin_blaze_kopter": "猪灵烈焰旋翼机",
    "piglin.pigkan": "猪坎",
    "piglin.general": "猪灵军阀",
    "piglin.pigdread": "惧猪",
    "piglin.piglin_kiln_hog": "猪灵熔炉疣猪",
    "piglin.piglin_warboss": "猪灵战酋",
}

VERB = {
    "idle": "低吼", "hurt": "受伤", "attack": "攻击", "death": "死亡",
    "walk": "行走", "run": "奔跑", "scream": "尖啸", "laugh": "大笑",
    "stun": "眩晕", "spin": "旋转", "bite": "撕咬", "finslap": "拍鳍",
    "jump": "跃水", "shoot": "射击", "splash": "溅水", "throw_net": "掷网",
    "aggro": "愤怒", "laser": "激光", "moving": "移动", "alert": "警戒",
    "charge": "冲锋", "line": "台词", "scan": "扫描", "fly": "飞行",
    "stomp": "践踏", "step": "脚步", "flare": "发射信号弹", "revive": "复活",
    "lantern_shatter": "灯笼碎裂", "slash": "挥砍", "smash": "重击",
    "warning": "警告", "breath": "吐息", "breed": "繁殖", "sting": "蜇刺",
    "charging": "蓄力", "gun": "开枪", "claw": "利爪", "saw": "锯刃",
    "growl": "咆哮", "melee": "近战", "heavy_hurt": "重伤", "use": "使用",
    "move": "移动", "defeat": "战败", "hurt_heavy": "重伤", "hit": "撞击", "warn": "警告", "idle_while_circling": "盘旋",
}

MACHINES = {
    "drone", "cog_devastator", "cog_bombardier", "cog_general", "cog_venator",
    "cog_gigantes", "cog_juggernaut", "cog_centipede", "cog_trenchbleeder",
    "fac_tank", "fac_walker", "wrecker_dozer", "wrecker_helicube",
    "wrecker_biker.bike",
}

MACHINE_VERB = {
    "idle": "运转声", "hurt": "受损", "death": "损毁", "walk": "行驶",
    "run": "疾驰", "attack": "开火", "charge": "冲锋", "fly": "飞行",
    "scan": "扫描", "gun": "开火", "stomp": "踏击", "step": "履带声",
    "shoot": "开火", "move": "行驶", "defeat": "损毁",
}

SPECIAL = {
    "piglin.general.claw": "利爪：摩擦声",
    "piglin.general.saw": "锯刃：嗡鸣",
    "piglin.general.hurt": "猪灵盔甲：受损",
    "wrecking_tool.use": "破拆工具：使用",
    "wrecker_biker.bike.wrecker_biker_idle_while_circling": "扫荡者摩托：盘旋声",
    "wrecker_dozer.stun_death": "扫荡者·推土机：眩晕中损毁",
    "wrecker_jumbo.line": "扫荡者·巨型：台词",
}


def tr_subtitle(key: str, value: str) -> str:
    path = key[len("scgextra.subtitle."):]
    if path in SPECIAL:
        return SPECIAL[path]
    parts = path.split(".")
    verb = parts[-1]
    entity = ".".join(parts[:-1])
    name = SUB_ENTITY.get(entity)
    if name is None:
        raise KeyError("unknown subtitle entity: " + entity)
    table = MACHINE_VERB if entity in MACHINES else VERB
    action = table.get(verb) or VERB.get(verb)
    if action is None:
        raise KeyError("unknown subtitle verb: " + verb)
    return f"{name}：{action}"


def translate(key: str, value: str) -> str:
    if key.startswith("entity.scgextra."):
        return ENTITY[key[len("entity.scgextra."):]]
    if key.startswith("item.scgextra."):
        rest = key[len("item.scgextra."):]
        if rest.endswith("_spawn_egg"):
            return ENTITY[rest[: -len("_spawn_egg")]] + "刷怪蛋"
        if rest in ITEM:
            return ITEM[rest]
        for set_id, (set_name, _, _) in SETS.items():
            for piece_id, piece_name in ARMOR_PIECES.items():
                if rest == f"{set_id}_{piece_id}":
                    return set_name + piece_name
        if rest.endswith("_super_flare"):
            material = rest[: -len("_super_flare")]
            return SUPER_FLARE[material] + "超级信号弹"
        raise KeyError("untranslated item: " + rest)
    if key.startswith("block.scgextra."):
        return BLOCK[key[len("block.scgextra."):]]
    if key.startswith("effect.scgextra."):
        return EFFECT[key[len("effect.scgextra."):]]
    if key.startswith("attribute.scgextra."):
        return ATTRIBUTE[key[len("attribute.scgextra."):]]
    if key.startswith("armorset.scgextra."):
        rest = key[len("armorset.scgextra."):]
        if rest.endswith(".half_set.description"):
            return SETS[rest[: -len(".half_set.description")]][1]
        if rest.endswith(".description"):
            return SETS[rest[: -len(".description")]][2]
        raise KeyError("untranslated armorset: " + rest)
    if key == "armorset.full_set_bonus.description":
        return "全套效果（4 件）："
    if key == "armorset.half_set_bonus.description":
        return "散件效果（2 件）："
    if key == "armorset.progress":
        return "已装备：%s/4 件"
    if key.startswith("scgextra.subtitle."):
        return tr_subtitle(key, value)
    if key.startswith("scgextra.raid."):
        rest = key[len("scgextra.raid."):]
        if rest.startswith("label.wave_"):
            n = rest[len("label.wave_"):]
            return f"第 {n} 波"
        if rest == "label.last_wave":
            return "最终波"
        if rest.startswith("label."):
            raid = rest[len("label."):]
            base = raid[:-len("_super")] if raid.endswith("_super") else raid
            return RAID_LABEL[base]
        if rest.startswith("announcement."):
            raid = rest[len("announcement."):]
            base = raid[:-len("_super")] if raid.endswith("_super") else raid
            return RAID_ANNOUNCE[base]
        raise KeyError("untranslated raid key: " + rest)
    if key == "itemGroup.scgextra.tab":
        return "SCG 扩展"
    if key == "biome.scgextra.warzone_biome":
        return "战区"
    raise KeyError("untranslated key: " + key)


zh = collections.OrderedDict()
for k, v in en.items():
    zh[k] = translate(k, v)

assert len(zh) == len(en)
assert set(zh) == set(en)

# 占位符一致性
for k in zh:
    if "%s" in en[k]:
        assert "%s" in zh[k], k

with open(ZH, "w", encoding="utf-8") as f:
    json.dump(zh, f, ensure_ascii=False, indent=2)
    f.write("\n")

print("wrote", ZH, "keys", len(zh))
