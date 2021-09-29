package net.nordicraft.phorses.listeners.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.nordicraft.phorses.api.NMSHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemUpdatePacketListener extends PacketAdapter {
    private NMSHandler handler;


    private List<UUID> playerWatchList = new ArrayList<>();


    public ItemUpdatePacketListener(Plugin plugin, NMSHandler handler) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.handler = handler;
    }



    @Override
    public void onPacketSending(PacketEvent e){
        ItemStack content = e.getPacket().getItemModifier().readSafely(0);
        if(handler.isFakeSaddle(content)){
            e.setCancelled(true);
            return;
        }


        if(e.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT){
            LivingEntity entity = (LivingEntity) e.getPacket().getEntityModifier(e.getPlayer().getWorld()).readSafely(0);
            if(e.getPacket().getItemSlots().readSafely(0) == EnumWrappers.ItemSlot.MAINHAND &&
                playerWatchList.contains(entity.getUniqueId()) && handler.isPHorse(content)){
                e.setCancelled(true);
            }
            return;
        }


        if(e.getPacketType() == PacketType.Play.Server.SET_SLOT){
            Player affected = e.getPlayer();
            if(e.getPacket().getIntegers().readSafely(0) == 0){
                int bukkitSlotIndex = e.getPacket().getIntegers().readSafely(1) - 36;
                if(affected.getInventory().getHeldItemSlot() == bukkitSlotIndex){
                    if(playerWatchList.contains(affected.getUniqueId()) && handler.isPHorse(content)){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    private boolean checkVisualIdentity(ItemStack first, ItemStack second){
        if(first.getType() != second.getType()){
            return false;
        }

        if(first.getAmount() != second.getAmount()){
            return false;
        }

        if(first.hasItemMeta() != second.hasItemMeta()){
            return false;
        }

        if(!first.getData().equals(second.getData())){
            return false;
        }

        if(first.getDurability() != second.getDurability()){
            return false;
        }

        if(!first.getEnchantments().equals(second.getEnchantments())){
            return false;
        }

        if(!first.hasItemMeta()){
            return true;
        }

        if(!first.getItemMeta().getItemFlags().equals(second.getItemMeta().getItemFlags())){
            return false;
        }

        if(first.getItemMeta().hasLore() != second.getItemMeta().hasLore()){
            return false;
        }

        if(first.getItemMeta().hasLore()){
            if(!first.getItemMeta().getLore().equals(second.getItemMeta().getLore())){
                return false;
            }
        }

        if(first.getItemMeta().hasDisplayName() != second.getItemMeta().hasDisplayName()){
            return false;
        }

        if(first.getItemMeta().hasDisplayName()){
            if(!first.getItemMeta().getDisplayName().equals(second.getItemMeta().getDisplayName())){
                return false;
            }
        }

        if(first.getItemMeta().isUnbreakable() != second.getItemMeta().isUnbreakable()){
            return false;
        }

        return true;
    }


    public List<UUID> getPlayerWatchList(){
        return this.playerWatchList;
    }
}
