package net.nordicraft.phorses.listeners;


import net.nordicraft.phorses.PortableHorses;
import net.nordicraft.phorses.api.NMSHandler;
import net.nordicraft.phorses.utils.Storage;
import net.nordicraft.phorses.utils.Styler;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RightClickListener implements Listener {
    final NMSHandler handler;
    final Storage c;
    final Styler styler;

    public RightClickListener(NMSHandler handler, Storage storage, Styler styler){
        this.handler = handler;
        this.c = storage;
        this.styler = styler;
    }



    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onRightClick(PlayerInteractEntityEvent e){
        if(!PortableHorses.newHorseSystem()) {
            ItemStack hand = e.getPlayer().getItemInHand();
            if (hand == null || !(e.getRightClicked() instanceof Horse)) {
                return;
            }
            if (!hand.getType().equals(Material.SADDLE)) {
                return;
            }

            Player p = e.getPlayer();
            if (handler.isPHorse(hand) && !c.RIGHT_CLICK_ALLOW_PHORSE_OVERRIDE) {
                e.setCancelled(true);
                if (!c.MESSAGES_CANT_OVERRIDE_PHORSE.isEmpty())
                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_OVERRIDE_PHORSE);
                return;
            }

            if (c.USE_SPECIAL_SADDLE && !handler.isSpecialSaddle(hand)) {
                return;
            }

            if(!p.hasPermission("portablehorses.use")){
                if(!c.MESSAGES_CANT_USE_NO_PERM.isEmpty()){
                    p.sendMessage(c.MESSAGES_CANT_USE_NO_PERM);
                }
                return;
            }

            Horse h = (Horse) e.getRightClicked();
            AnimalTamer owner = h.getOwner();
            if (owner != null) {
                if (!c.RIGHTCLICK_ALLOW_STRANGER_TAKE && !h.getOwner().getUniqueId().equals(p.getUniqueId())) {
                    if (!c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty())
                        p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    e.setCancelled(true);
                    return;
                }
            } else {
                if (!c.RIGHTCLICK_ALLOW_STRANGER_TAKE) {
                    if (!c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty())
                        p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    e.setCancelled(true);
                    return;
                }
            }
            e.setCancelled(true);
            HorseInventory horseinv = h.getInventory();


            if (h.isCarryingChest() && !c.STORE_INVENTORY) {
                for (int i = 2; i <= 16; ++i) {
                    ItemStack content = horseinv.getItem(i);
                    if (content != null)
                        h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                }
                horseinv.clear();
            }
            if (!c.ALLOW_RECURSIVE_PHORSE && h.isCarryingChest())
                for (int i = 2; i <= 16; ++i) {
                    ItemStack content = horseinv.getItem(i);
                    if (content != null)
                        if (content.getType().equals(Material.SADDLE)) {
                            if (handler.isPHorse(content)) {
                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                e.setCancelled(true);
                                return;
                            }
                        }
                }

            if (!c.STORE_ARMOR && horseinv.getArmor() != null) {
                h.getWorld().dropItemNaturally(h.getEyeLocation(), horseinv.getArmor());
                h.getInventory().setArmor(null);
            }

            if (!c.USE_SPECIAL_SADDLE)
                horseinv.setSaddle(new ItemStack(Material.SADDLE));
            else
                horseinv.setSaddle(PortableHorses.getSpSaddle());

            hand = handler.transferTag(h, hand);
            hand = styler.styleSaddle(h, hand);
            p.setItemInHand(hand);
            if (!c.MESSAGES_HORSE_TAKEN.isEmpty())
                p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);
            SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
            h.remove();
        } else {
            ItemStack hand = e.getPlayer().getItemInHand();
            if (hand == null || !(e.getRightClicked() instanceof AbstractHorse)) {
                return;
            }
            if(e.getRightClicked() instanceof Llama){
                return;
            }

            if (!hand.getType().equals(Material.SADDLE)) {
                return;
            }

            Player p = e.getPlayer();
            boolean alreadyPHorse = handler.isPHorse(hand);
            if (alreadyPHorse && !c.RIGHT_CLICK_ALLOW_PHORSE_OVERRIDE) {
                e.setCancelled(true);
                if (!c.MESSAGES_CANT_OVERRIDE_PHORSE.isEmpty())
                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_OVERRIDE_PHORSE);
                return;
            }

            if (c.USE_SPECIAL_SADDLE && !handler.isSpecialSaddle(hand) && !alreadyPHorse) {
                return;
            }

            AbstractHorse h = (AbstractHorse) e.getRightClicked();
            AnimalTamer owner = h.getOwner();
            if (owner != null) {
                if (!c.RIGHTCLICK_ALLOW_STRANGER_TAKE && !h.getOwner().getUniqueId().equals(p.getUniqueId())) {
                    if (!c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty())
                        p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    e.setCancelled(true);
                    return;
                }
            } else {
                if (!c.RIGHTCLICK_ALLOW_STRANGER_TAKE) {
                    if (!c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty())
                        p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    e.setCancelled(true);
                    return;
                }
            }
            e.setCancelled(true);

            Inventory horseinv = h.getInventory();
            boolean hasChest = horseinv.getSize()>=16;


            if (hasChest && !c.STORE_INVENTORY) {
                for (int i = 2; i <= 16; ++i) {
                    ItemStack content = horseinv.getItem(i);
                    if (content != null)
                        h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                }
                horseinv.clear();
            }
            if (!c.ALLOW_RECURSIVE_PHORSE && hasChest)
                for (int i = 2; i <= 16; ++i) {
                    ItemStack content = horseinv.getItem(i);
                    if (content != null)
                        if (content.getType().equals(Material.SADDLE)) {
                            if (handler.isPHorse(content)) {
                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                e.setCancelled(true);
                                return;
                            }
                        }
                }

            if (!c.STORE_ARMOR && horseinv.getItem(1) != null) {
                h.getWorld().dropItemNaturally(h.getEyeLocation(), horseinv.getItem(1));
                horseinv.setItem(1, null);
            }

            if (!c.USE_SPECIAL_SADDLE)
                horseinv.setItem(0, new ItemStack(Material.SADDLE));
            else
                horseinv.setItem(0, PortableHorses.getSpSaddle());

            hand = handler.transferTag(h, hand);
            hand = styler.styleSaddleNew(h, hand);
            p.setItemInHand(hand);
            if (!c.MESSAGES_HORSE_TAKEN.isEmpty())
                p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);
            SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
            h.remove();
        }

    }
}

