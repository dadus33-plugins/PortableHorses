package net.nordicraft.phorses.implementations.v1_13_R2;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftDonkey;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftMule;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftSkeletonHorse;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftZombieHorse;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mule;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHorse;
import net.minecraft.server.v1_13_R2.EntityHorseDonkey;
import net.minecraft.server.v1_13_R2.EntityHorseMule;
import net.minecraft.server.v1_13_R2.EntityHorseSkeleton;
import net.minecraft.server.v1_13_R2.EntityHorseZombie;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.nordicraft.phorses.api.NMSHandler;

public class Handler1_13_R2 extends NMSHandler {
		
    @Override
    public ItemStack transferTag(LivingEntity horse, ItemStack saddle) {
        NBTTagCompound saddleTag;
        net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        saddleTag = nmsSaddle.getTag();
        if(saddleTag!=null){
            saddleTag.setBoolean("phorse", true);
        }else{
            saddleTag = new NBTTagCompound();
            saddleTag.setBoolean("phorse", true);
        }
        NBTTagCompound horseTag = new NBTTagCompound();
        saddleTag.setString("entype", horse.getType().name());
        if(horse instanceof Horse){
            EntityHorse nmsHorse = ((CraftHorse)horse).getHandle();
            nmsHorse.b(horseTag);
            if(horse.getHealth()<=0.5D)
                if(horseTag.getFloat("Health")<=0.5F || horseTag.getFloat("HealF")<=0.5F){
                    horseTag.setFloat("Health", 1F);
                    if(horseTag.hasKey("HealF"))
                        horseTag.setFloat("HealF", 1F);
                }
            saddleTag.set("horsetag", horseTag);
            saddleTag.setBoolean("phorse", true);
            saddleTag.setBoolean("iscnameviz", horse.isCustomNameVisible());
            if(horse.getCustomName()!=null)
                saddleTag.setString("cname", horse.getCustomName());
            nmsSaddle.setTag(saddleTag);
            saddle = CraftItemStack.asCraftMirror(nmsSaddle);
            return saddle;
        }
        if(horse instanceof Donkey){
            EntityHorseDonkey nmsHorse = (EntityHorseDonkey) ((CraftDonkey)horse).getHandle();
            nmsHorse.b(horseTag);
            if(horse.getHealth()<=0.5D)
                if(horseTag.getFloat("Health")<=0.5F || horseTag.getFloat("HealF")<=0.5F){
                    horseTag.setFloat("Health", 1F);
                    if(horseTag.hasKey("HealF"))
                        horseTag.setFloat("HealF", 1F);
                }
            saddleTag.set("horsetag", horseTag);
            saddleTag.setBoolean("phorse", true);
            saddleTag.setBoolean("iscnameviz", horse.isCustomNameVisible());
            if(horse.getCustomName()!=null)
                saddleTag.setString("cname", horse.getCustomName());
            nmsSaddle.setTag(saddleTag);
            saddle = CraftItemStack.asCraftMirror(nmsSaddle);
            return saddle;
        }
        if(horse instanceof Mule){
            EntityHorseMule nmsHorse = (EntityHorseMule) ((CraftMule)horse).getHandle();
            nmsHorse.b(horseTag);
            if(horse.getHealth()<=0.5D)
                if(horseTag.getFloat("Health")<=0.5F || horseTag.getFloat("HealF")<=0.5F){
                    horseTag.setFloat("Health", 1F);
                    if(horseTag.hasKey("HealF"))
                        horseTag.setFloat("HealF", 1F);
                }
            saddleTag.set("horsetag", horseTag);
            saddleTag.setBoolean("phorse", true);
            saddleTag.setBoolean("iscnameviz", horse.isCustomNameVisible());
            if(horse.getCustomName()!=null)
                saddleTag.setString("cname", horse.getCustomName());
            nmsSaddle.setTag(saddleTag);
            saddle = CraftItemStack.asCraftMirror(nmsSaddle);
            return saddle;
        }
        if(horse instanceof SkeletonHorse){
            EntityHorseSkeleton nmsHorse = (EntityHorseSkeleton) ((CraftSkeletonHorse)horse).getHandle();
            nmsHorse.b(horseTag);
            if(horse.getHealth()<=0.5D)
                if(horseTag.getFloat("Health")<=0.5F || horseTag.getFloat("HealF")<=0.5F){
                    horseTag.setFloat("Health", 1F);
                    if(horseTag.hasKey("HealF"))
                        horseTag.setFloat("HealF", 1F);
                }
            saddleTag.set("horsetag", horseTag);
            saddleTag.setBoolean("phorse", true);
            saddleTag.setBoolean("iscnameviz", horse.isCustomNameVisible());
            if(horse.getCustomName()!=null)
                saddleTag.setString("cname", horse.getCustomName());
            nmsSaddle.setTag(saddleTag);
            saddle = CraftItemStack.asCraftMirror(nmsSaddle);
            return saddle;
        }
        if(horse instanceof ZombieHorse){
            EntityHorseZombie nmsHorse = (EntityHorseZombie) ((CraftZombieHorse)horse).getHandle();
            nmsHorse.b(horseTag);
            if(horse.getHealth()<=0.5D)
                if(horseTag.getFloat("Health")<=0.5F || horseTag.getFloat("HealF")<=0.5F){
                    horseTag.setFloat("Health", 1F);
                    if(horseTag.hasKey("HealF"))
                        horseTag.setFloat("HealF", 1F);
                }
            saddleTag.set("horsetag", horseTag);
            saddleTag.setBoolean("phorse", true);
            saddleTag.setBoolean("iscnameviz", horse.isCustomNameVisible());
            if(horse.getCustomName()!=null)
                saddleTag.setString("cname", horse.getCustomName());
            nmsSaddle.setTag(saddleTag);
            saddle = CraftItemStack.asCraftMirror(nmsSaddle);
            return saddle;
        }
        return null;
    }


    @Override
    public void spawnFromSaddle(ItemStack saddle, LivingEntity h) {
    	try {
	        final NBTTagCompound saddleTag = CraftItemStack.asNMSCopy(saddle).getTag();
	        final Entity spawned = ((CraftEntity) h).getHandle();
	        final NBTTagCompound saddleHorseTag = (NBTTagCompound)saddleTag.get("horsetag");
	        spawned.getClass().getDeclaredMethod("a", NBTTagCompound.class).invoke(spawned, saddleHorseTag);
	        //spawned.a(saddleHorseTag);
	        spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
	        if(saddleTag.hasKey("cname"))
	            spawned.setCustomName(new ChatMessage(saddleTag.getString("cname")));
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }

    @Override
    public ItemStack setSpecialSaddle(ItemStack saddle){
        net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        ItemStack returnItem;
        if(nmsSaddle.hasTag()){
            nmsSaddle.getTag().setBoolean("spsaddle", true);
            returnItem = CraftItemStack.asCraftMirror(nmsSaddle);
            return returnItem;
        }
        NBTTagCompound toSet = new NBTTagCompound();
        toSet.setBoolean("spsaddle", true);
        nmsSaddle.setTag(toSet);
        return CraftItemStack.asCraftMirror(nmsSaddle);

    }

    @Override
    public EntityType getEntityType(ItemStack saddle){
        net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        EntityType t = EntityType.valueOf(nmsSaddle.getTag().getString("entype"));
        if(t==null){
            NBTTagCompound tag = nmsSaddle.getTag();
            int type = ((NBTTagCompound)tag.get("horsetag")).getInt("Type");
            switch(type){
                case 0: return EntityType.HORSE;
                case 1: return EntityType.DONKEY;
                case 2: return EntityType.MULE;
                case 3: return EntityType.ZOMBIE_HORSE;
                case 4: return EntityType.SKELETON_HORSE;
                default: return null;
            }
        }
        return t;
    }
}
