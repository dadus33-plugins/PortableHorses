package net.nordicraft.phorses.listeners;

import net.nordicraft.phorses.PortableHorses;
import net.nordicraft.phorses.api.NMSHandler;
import net.nordicraft.phorses.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

public class PlayerDeathListener implements Listener {

    private final NMSHandler handler;
    private final Storage c;
    private final PortableHorses plugin;

    public PlayerDeathListener(NMSHandler handler, Storage config, PortableHorses plugin){
        this.handler = handler;
        this.c = config;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof LivingEntity) || e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            return;
        }

        Player player = (Player) e.getEntity();
        LivingEntity damager = (LivingEntity) e.getDamager();

        if(player.getHealth() - e.getFinalDamage() <= 0){
            if(!handler.isPHorse(damager.getEquipment().getItemInMainHand())) {
                return;
            }
            ItemStack original = damager.getEquipment().getItemInMainHand();
            ItemStack fake = fakeSaddle(original);

            ((LivingEntity) e.getDamager()).getEquipment().setItemInHand(fake);

            plugin.getDeathItemPacketListener().getPlayerWatchList().add(damager.getUniqueId());

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                ((LivingEntity) e.getDamager()).getEquipment().setItemInMainHand(original);
            } , 2);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.getDeathItemPacketListener().getPlayerWatchList().remove(damager.getUniqueId());
            }, 3);
        }
    }


    private ItemStack fakeSaddle(ItemStack original){
        ItemStack fake = new ItemStack(original.getType(), original.getAmount());
        ItemMeta fakeMeta = fake.getItemMeta();

        ItemMeta originalMeta = original.getItemMeta();

        if(originalMeta.hasDisplayName()) {
            fakeMeta.setDisplayName(originalMeta.getDisplayName());
        }

        if(originalMeta.hasLocalizedName()) {
            fakeMeta.setLocalizedName(original.getItemMeta().getLocalizedName());
        }

        if(originalMeta.hasEnchants()){
            for(Map.Entry<Enchantment, Integer> entry : originalMeta.getEnchants().entrySet()){
                fakeMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }

        if(originalMeta.hasLore()) {
            fakeMeta.setLore(new ArrayList<>(originalMeta.getLore()));
        }

        if(originalMeta.getItemFlags().size() > 0) {
            fakeMeta.addItemFlags(originalMeta.getItemFlags().toArray(new ItemFlag[originalMeta.getItemFlags().size()]));
        }

        fake.setItemMeta(fakeMeta);
        return fake;
    }

}
