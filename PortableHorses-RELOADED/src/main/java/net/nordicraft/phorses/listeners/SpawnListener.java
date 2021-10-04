package net.nordicraft.phorses.listeners;

import me.iiSnipez.CombatLog.CombatLog;
import net.nordicraft.phorses.PortableHorses;
import net.nordicraft.phorses.api.NMSHandler;
import net.nordicraft.phorses.utils.Storage;
import net.nordicraft.phorses.utils.Styler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class SpawnListener implements Listener {

    private final NMSHandler handler;
    private final Storage c;
    private final Styler s;
    private final CombatLog combatLog;
    final static HashMap<String, UUID> TAKERS = new HashMap<>();
    final static List<String> PROCESSED = new ArrayList<>();

    static{
        Bukkit.getScheduler().runTaskTimer(PortableHorses.instance(), new Runnable() {
            @Override
            public void run() {
                TAKERS.clear();
            }
        }, 20L, 20L);
    }

    public SpawnListener(NMSHandler handler, Storage storage, Styler s){
        this.handler = handler;
        this.c = storage;
        this.s = s;
        if(c.COMBATLOG_INTEGRATION)
            combatLog = (CombatLog) Bukkit.getPluginManager().getPlugin("CombatLog");
        else
            combatLog = null;
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(PlayerInteractEvent e){
        if(PROCESSED.contains(e.getPlayer().getName())){
            return;
        }
        final String name = e.getPlayer().getName();
        PROCESSED.add(name);
        Bukkit.getScheduler().runTaskLater(PortableHorses.instance(), new Runnable() {
            @Override
            public void run() {
                PROCESSED.remove(name);
            }
        }, 1L);
        if (e.getClickedBlock() == null) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack saddle = e.getPlayer().getItemInHand();
        if (saddle == null) {
            return;
        }
        if (!saddle.getType().equals(Material.SADDLE)) {
            return;
        }
        if (!handler.isPHorse(saddle)) {
            return;
        }
        if(!e.getPlayer().hasPermission("portablehorses.use")){
            if(!c.MESSAGES_CANT_USE_NO_PERM.isEmpty()){
                e.getPlayer().sendMessage(c.MESSAGES_CANT_USE_NO_PERM);
            }
            return;
        }
        if (combatLog != null) {
            if (combatLog.taggedPlayers.containsKey(e.getPlayer().getName())) {
                e.setCancelled(true);
                if (!c.MESSAGES_CANT_SPAWN_COMBATLOGGED.isEmpty()) {
                    e.getPlayer().sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_SPAWN_COMBATLOGGED);
                }
                return;
            }
        }
        e.setUseItemInHand(Event.Result.DENY);
        e.setUseInteractedBlock(Event.Result.DENY);
        Player p = e.getPlayer();
        Location spawn = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();



        if(!PortableHorses.newHorseSystem()) {
            Horse h;

            if(p.hasPermission("portablehorses.use.ignore-protections")){
                h = (Horse) handler.forceSpawn(handler.getEntityType(saddle), spawn);
            }else{
                h = (Horse) handler.spawn(handler.getEntityType(saddle), spawn);
            }
            
            if (!h.isValid()) {
                if (c.MESSAGES_CANT_PLACE.isEmpty()) {
                    return;
                }
                p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_PLACE);
                return;
            }
            handler.spawnFromSaddle(saddle, h);
            ItemStack sdlToGive = new ItemStack(Material.SADDLE);
            if (c.USE_SPECIAL_SADDLE) {
                sdlToGive = PortableHorses.getSpSaddle();
            }
            if (!c.AUTO_SADDLE && c.GIVE_SADDLE) {
                h.getInventory().setSaddle(null);
                if (p.getInventory().firstEmpty() == -1) {
                    switch (c.AUTO_SADDLE_FALLBACK_OPTION) {
                        case "FIRSTITEM":
                            p.getInventory().setItem(35, sdlToGive);
                            p.sendMessage(c.MESSAGES_AUTO_SADDLE_FIRST_ITEM_REPLACED);
                            p.updateInventory();
                            break;
                        case "GROUND":
                            p.getWorld().dropItem(p.getEyeLocation(), sdlToGive);
                            p.sendMessage(c.MESSAGES_AUTO_SADDLE_SADDLE_DROPPED);
                            break;
                        default:
                            h.getInventory().setSaddle(sdlToGive);
                    }
                } else {
                    p.getInventory().addItem(sdlToGive);
                    p.updateInventory();
                }
            } else {
                if (c.AUTO_SADDLE) {
                    h.getInventory().setSaddle(sdlToGive);
                }
            }
            saddle.setAmount(saddle.getAmount() - 1);
            if (saddle.getAmount() == 0)
                p.setItemInHand(null);
            else
                p.setItemInHand(saddle);
            if (!c.MESSAGES_HORSE_SPAWNED.isEmpty())
                e.getPlayer().sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_SPAWNED);
            if (c.AUTO_MOUNT) {
                h.setPassenger(e.getPlayer());
            }
        }else{
            AbstractHorse h;
            if(p.hasPermission("portablehorses.use.ignore-protections")){
                h = (AbstractHorse) handler.forceSpawn(handler.getEntityType(saddle), spawn);
            }else{
                h = (AbstractHorse) handler.spawn(handler.getEntityType(saddle), spawn);
            }

            if (h == null || !h.isValid()) {
                if (c.MESSAGES_CANT_PLACE.isEmpty()) {
                    return;
                }
                p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_PLACE);
                return;
            }
            handler.spawnFromSaddle(saddle, h);
            ItemStack sdlToGive = new ItemStack(Material.SADDLE);
            if (c.USE_SPECIAL_SADDLE) {
                sdlToGive = PortableHorses.getSpSaddle();
            }
            if (!c.AUTO_SADDLE && c.GIVE_SADDLE) {
                h.getInventory().setItem(0, null);
                if (p.getInventory().firstEmpty() == -1) {
                    switch (c.AUTO_SADDLE_FALLBACK_OPTION) {
                        case "FIRSTITEM":
                            p.getInventory().setItem(35, sdlToGive);
                            p.sendMessage(c.MESSAGES_AUTO_SADDLE_FIRST_ITEM_REPLACED);
                            p.updateInventory();
                            break;
                        case "GROUND":
                            p.getWorld().dropItem(p.getEyeLocation(), sdlToGive);
                            p.sendMessage(c.MESSAGES_AUTO_SADDLE_SADDLE_DROPPED);
                            break;
                        default:
                            h.getInventory().setItem(0, sdlToGive);
                    }
                } else {
                    p.getInventory().addItem(sdlToGive);
                    p.updateInventory();
                }
            } else {
                if (c.AUTO_SADDLE) {
                    h.getInventory().setItem(0, sdlToGive);
                }
            }
            saddle.setAmount(saddle.getAmount() - 1);
            if (saddle.getAmount() == 0)
                p.setItemInHand(null);
            else
                p.setItemInHand(saddle);
            if (!c.MESSAGES_HORSE_SPAWNED.isEmpty())
                e.getPlayer().sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_SPAWNED);
            if (c.AUTO_MOUNT) {
                h.setPassenger(e.getPlayer());
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHorseDeath(EntityDeathEvent e){
        if(!PortableHorses.newHorseSystem()) {
            if (!(e.getEntity() instanceof Horse)) {
                return;
            }
            Horse h = (Horse) e.getEntity();
            ItemStack saddle = h.getInventory().getSaddle();
            if (saddle == null) {
                return;
            }
            if (c.DROP_NORMAL_SADDLE) {
                return;
            }
            List<ItemStack> drops = e.getDrops();
            drops.remove(saddle);
            saddle = handler.transferTag(h, saddle);
            saddle = s.styleSaddle(h, saddle);
            drops.add(saddle);
            return;
        }
        if (!(e.getEntity() instanceof AbstractHorse) || (e.getEntity() instanceof Llama)) {
            return;
        }
        AbstractHorse h = (AbstractHorse) e.getEntity();
        ItemStack saddle = h.getInventory().getItem(0);
        if (saddle == null) {
            return;
        }
        if (c.DROP_NORMAL_SADDLE) {
            return;
        }
        List<ItemStack> drops = e.getDrops();
        drops.remove(saddle);
        saddle = handler.transferTag(h, saddle);
        saddle = s.styleSaddleNew(h, saddle);
        drops.add(saddle);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDismount(EntityDismountEvent e) {
        if (!c.AUTO_TAKE_ON_DISMOUNT || e.getDismounted().isDead() || !e.getDismounted().isValid()) {
            return;
        }
        if(!e.getEntity().getType().equals(EntityType.PLAYER)){
            return;
        }

        Player p = (Player) e.getEntity();
        if(!p.isValid()){
            return;
        }

        if (!PortableHorses.newHorseSystem()){
            if (!e.getDismounted().getType().equals(EntityType.HORSE)) {
                return;
            }
            Horse h = (Horse) e.getDismounted();
            if (TAKERS.remove(p.getName()) != null) {
                return;
            }
            if (!p.hasPermission("portablehorses.use")) {
                return;
            }
            if (h.getOwner() != null)
                if (!h.getOwner().getUniqueId().equals(p.getUniqueId()) && !p.hasPermission("portablehorses.use.ignore-ownership")) {
                    if(c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty()){
                        return;
                    }
                    p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    return;
                }
            if (h.getInventory().getSaddle() == null) {
                return;
            }
            ItemStack saddle = h.getInventory().getSaddle();
            if (!handler.isPHorse(saddle) && (!handler.isSpecialSaddle(saddle) && c.USE_SPECIAL_SADDLE)) {
                return;
            }
            HorseInventory horseinv = h.getInventory();
            if (!c.ALLOW_RECURSIVE_PHORSE && h.isCarryingChest())
                for (int i = 2; i <= 16; ++i) {
                    ItemStack content = horseinv.getItem(i);
                    if (content != null)
                        if (content.getType().equals(Material.SADDLE)) {
                            if (handler.isPHorse(content)) {
                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                return;
                            }
                        }
                }
            h.setOwner(p);
            saddle = handler.transferTag(h, saddle);
            saddle = s.styleSaddle(h, saddle);
            h.remove();
            if (p.getInventory().firstEmpty() == -1) {
                p.getWorld().dropItem(p.getEyeLocation().add(0, -1, 0), saddle);
                if (c.MESSAGES_HORSE_TAKEN.isEmpty()) {
                    return;
                }
                p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);
                return;
            }
            p.getInventory().addItem(saddle);
            if (c.MESSAGES_HORSE_TAKEN.isEmpty()) {
                return;
            }
            p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);
            return;
        }



        if (!(e.getDismounted() instanceof AbstractHorse) || e.getDismounted() instanceof Llama) {
            return;
        }
        AbstractHorse h = (AbstractHorse) e.getDismounted();
        if (TAKERS.remove(p.getName()) != null) {
            return;
        }
        if (!p.hasPermission("portablehorses.use")) {
            return;
        }
        if (h.getOwner() != null) {
            if (!h.getOwner().getUniqueId().equals(p.getUniqueId()) && !p.hasPermission("portablehorses.use.ignore-ownership")) {
                if(c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty()){
                    return;
                }
                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                return;
            }
        }
        if (h.getInventory().getItem(0) == null) {
            return;
        }
        ItemStack saddle = h.getInventory().getItem(0);
        if (!handler.isPHorse(saddle) && (!handler.isSpecialSaddle(saddle) && c.USE_SPECIAL_SADDLE)) {
            return;
        }
        if (!c.ALLOW_RECURSIVE_PHORSE && h.getInventory().getSize()>=16)
            for (int i = 2; i <= 16; ++i) {
                ItemStack content = h.getInventory().getItem(i);
                if (content != null)
                    if (content.getType().equals(Material.SADDLE)) {
                        if (handler.isPHorse(content)) {
                            if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                            return;
                        }
                    }
            }

        h.setOwner(p);
        saddle = handler.transferTag(h, saddle);
        saddle = s.styleSaddleNew(h, saddle);
        h.remove();
        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItem(p.getEyeLocation().add(0, -1, 0), saddle);
            if (c.MESSAGES_HORSE_TAKEN.isEmpty()) {
                return;
            }
            p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);
            return;
        }
        p.getInventory().addItem(saddle);
        if (c.MESSAGES_HORSE_TAKEN.isEmpty()) {
            return;
        }
        p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);
    }



}
