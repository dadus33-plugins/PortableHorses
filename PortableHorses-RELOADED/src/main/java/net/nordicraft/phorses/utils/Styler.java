package net.nordicraft.phorses.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.nordicraft.phorses.PortableHorses;

@SuppressWarnings("deprecation")
public class Styler {

    private Storage c;
    private PortableHorses plugin;

    public Styler(Storage storage, PortableHorses plugin){
        this.c = storage;
        this.plugin = plugin;
    }

    public ItemStack styleSaddle(Horse h, ItemStack saddle){
        ItemMeta saddleMeta = saddle.getItemMeta();
        String displayName = c.DISPLAY_NAME.replaceAll(Pattern.quote("%name%"), h.getName()!=null ? h.getName() : translateVariant(h.getVariant()));
        List<String> lore = new ArrayList<String>();
        lore.addAll(c.DATA);

        if(c.SHOW_COLOR && (h instanceof Horse)) {
            List<String> color = new ArrayList<String>();
            for (String s : c.COLOR) {
                color.add(s.replaceAll(Pattern.quote("%color%"), translateColor(h.getColor())));
            }
            lore.addAll(color);
        }

        if(c.SHOW_STYLE) {
            List<String> type = new ArrayList<String>();
            for (String s : c.STYLE) {
                type.add(s.replaceAll(Pattern.quote("%style%"), translateStyle(h.getStyle())));
            }
            lore.addAll(type);
        }

        if(c.SHOW_VARIANT) {
            List<String> variant = new ArrayList<String>();
            for (String s : c.VARIANT) {
                variant.add(s.replaceAll(Pattern.quote("%variant%"), translateVariant(h.getVariant())));
            }
            lore.addAll(variant);
        }

        if(c.SHOW_JUMP_STRENGTH) {
            List<String> jump = new ArrayList<String>();
            String js = String.valueOf(h.getJumpStrength());
            js = js.length()>5 ? js.substring(0, 4) : js;
            for (String s : c.JUMP) {
                jump.add(s.replaceAll(Pattern.quote("%jump-strength%"), js));
            }
            lore.addAll(jump);
        }

        if(c.SHOW_HEALTH) {
            List<String> health = new ArrayList<String>();
            double chealth = h.getHealth() > 0.5D ? h.getHealth() : 1.0D;
            double mhealth = EntityUtils.getMaxHealth(h);
            for (String s : c.HEALTH) {
                health.add(s.replaceAll(Pattern.quote("%current-health%"), String.valueOf(chealth)).
                        replaceAll(Pattern.quote("%max-health%"), String.valueOf(mhealth)));
            }
            lore.addAll(health);
        }

        if(c.SHOW_HAS_CHEST){
            lore.add(c.HAS_CHEST);
        }

        if(c.PHORSE_ENCHANTED){
            saddleMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            saddleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(c.SHOW_SPEED){
            List<String> speed = new ArrayList<String>();
            double hspeed = plugin.getHandler().getSpeedOfHorse(h);
            String spd = String.format("%.2f", hspeed);
            for (String s : c.SPEED) {
                speed.add(s.replaceAll(Pattern.quote("%movement-speed%"), spd));
            }
            lore.addAll(speed);
        }

        saddleMeta.setLore(lore);
        saddleMeta.setDisplayName(displayName);
        saddleMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        saddle.setItemMeta(saddleMeta);
        return saddle;
    }

    public ItemStack styleSaddleNew(AbstractHorse h, ItemStack saddle){
        ItemMeta saddleMeta = saddle.getItemMeta();
        String displayName = c.DISPLAY_NAME.replaceAll(Pattern.quote("%name%"), h.getName()!=null ? h.getName() : translateVariant(h.getVariant()));
        List<String> lore = new ArrayList<String>();
        lore.addAll(c.DATA);

        if(c.SHOW_COLOR && (h instanceof Horse)) {
            Horse ch = (Horse)h;
            List<String> color = new ArrayList<String>();
            for (String s : c.COLOR) {
                color.add(s.replaceAll(Pattern.quote("%color%"), translateColor(ch.getColor())));
            }
            lore.addAll(color);
        }

        if(c.SHOW_STYLE && (h instanceof Horse)) {
            Horse ch = (Horse)h;
            List<String> type = new ArrayList<String>();
            for (String s : c.STYLE) {
                type.add(s.replaceAll(Pattern.quote("%style%"), translateStyle(ch.getStyle())));
            }
            lore.addAll(type);
        }

        if(c.SHOW_VARIANT) {
            List<String> variant = new ArrayList<String>();
            for (String s : c.VARIANT) {
                variant.add(s.replaceAll(Pattern.quote("%variant%"), translateVariant(h.getVariant())));
            }
            lore.addAll(variant);
        }

        if(c.SHOW_JUMP_STRENGTH) {
            List<String> jump = new ArrayList<String>();
            String js = String.valueOf(h.getJumpStrength());
            js = js.length()>5 ? js.substring(0, 4) : js;
            for (String s : c.JUMP) {
                jump.add(s.replaceAll(Pattern.quote("%jump-strength%"), js));
            }
            lore.addAll(jump);
        }

        if(c.SHOW_HEALTH) {
            List<String> health = new ArrayList<String>();
            double chealth = h.getHealth()>0.5D ? h.getHealth() : 1.0D;
            double mhealth = EntityUtils.getMaxHealth(h);
            for (String s : c.HEALTH) {
                health.add(s.replaceAll(Pattern.quote("%current-health%"), String.valueOf(chealth)).
                        replaceAll(Pattern.quote("%max-health%"), String.valueOf(mhealth)));
            }
            lore.addAll(health);
        }

        if(c.SHOW_HAS_CHEST){
            lore.add(c.HAS_CHEST);
        }

        if(c.PHORSE_ENCHANTED){
            saddleMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            saddleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(c.SHOW_SPEED){
            List<String> speed = new ArrayList<String>();
            double hspeed = plugin.getHandler().getSpeedOfHorse(h);
            String spd = String.format("%.2f", hspeed);
            for (String s : c.SPEED) {
                speed.add(s.replaceAll(Pattern.quote("%movement-speed%"), spd));
            }
            lore.addAll(speed);
        }

        saddleMeta.setLore(lore);
        saddleMeta.setDisplayName(displayName);
        saddleMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        saddle.setItemMeta(saddleMeta);
        return saddle;
    }




    public ItemStack makeSpecialSaddle(){
        ItemStack result = new ItemStack(Material.SADDLE);
        result = plugin.getHandler().setSpecialSaddle(result);
        ItemMeta resultMeta = result.getItemMeta();
        resultMeta.setDisplayName(c.SPECIAL_NAME);
        resultMeta.setLore(c.SPECIAL_LORE);
        if(c.SPECIAL_ENCHANTED){
            resultMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            resultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        result.setItemMeta(resultMeta);
        return result;
    }



    private String translateColor(Horse.Color col){
        switch(col){
            case BLACK: return c.COLOR_BLACK;
            case BROWN: return c.COLOR_BROWN;
            case WHITE: return c.COLOR_WHITE;
            case GRAY: return c.COLOR_GRAY;
            case CHESTNUT: return c.COLOR_CHESTNUT;
            case CREAMY: return c.COLOR_CREAMY;
            default: return c.COLOR_DARK_BROWN;
        }
    }

    private String translateStyle(Horse.Style sty){
        switch(sty){
            case BLACK_DOTS: return c.STYLE_BLACK_DOTS;
            case WHITE: return c.STYLE_WHITE;
            case NONE: return c.STYLE_NONE;
            case WHITEFIELD: return c.STYLE_WHITEFIELD;
            default: return c.STYLE_WHITE_DOTS;
        }
    }

    private String translateVariant(Horse.Variant var){
        switch(var){
            case DONKEY: return c.VARIANT_DONKEY;
            case HORSE: return c.VARIANT_HORSE;
            case UNDEAD_HORSE: return c.VARIANT_UNDEAD_HORSE;
            case MULE: return c.VARIANT_MULE;
            default: return c.VARIANT_SKELETON_HORSE;
        }
    }


}
