package net.nordicraft.phorses.implementations.v1_12_R1;


import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.Entity;
import net.nordicraft.phorses.api.ModernNMSHandler;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class Handler1_12_R1 implements ModernNMSHandler {

    Plugin instance;
    private MethodHandle canAddEntity;
    private MethodHandle onEntityAdded;

    public Handler1_12_R1(Plugin instance){
        this.instance = instance;

        try {
            Method canAddEntity = WorldServer.class.getDeclaredMethod("j", Entity.class);
            Method onEntityAdded = World.class.getDeclaredMethod("b", Entity.class);

            canAddEntity.setAccessible(true);
            onEntityAdded.setAccessible(true);

            this.canAddEntity = MethodHandles.lookup().unreflect(canAddEntity);
            this.onEntityAdded = MethodHandles.lookup().unreflect(onEntityAdded);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemStack transferTag(LivingEntity horse, ItemStack saddle) {
        NBTTagCompound saddleTag;
        net.minecraft.server.v1_12_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        saddleTag = nmsSaddle.getTag();
        if(saddleTag!=null){
            saddleTag.setBoolean("phorse", true);
        }else{
            saddleTag = new NBTTagCompound();
            saddleTag.setBoolean("phorse", true);
        }
        NBTTagCompound horseTag = new NBTTagCompound();
        saddleTag.setString("entype", horse.getType().toString());
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
        final NBTTagCompound saddleTag = CraftItemStack.asNMSCopy(saddle).getTag();
        if(h instanceof Horse){
            final EntityHorse spawned = ((CraftHorse)h).getHandle();
            final NBTTagCompound saddleHorseTag = (NBTTagCompound)saddleTag.get("horsetag");
            spawned.a(saddleHorseTag);
            spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
            if(saddleTag.hasKey("cname"))
                spawned.setCustomName(saddleTag.getString("cname"));
            return;
        }
        if(h instanceof Mule){
            final EntityHorseMule spawned = (EntityHorseMule) ((CraftMule)h).getHandle();
            final NBTTagCompound saddleHorseTag = (NBTTagCompound)saddleTag.get("horsetag");
            spawned.a(saddleHorseTag);
            spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
            if(saddleTag.hasKey("cname"))
                spawned.setCustomName(saddleTag.getString("cname"));
        }
        if(h instanceof Donkey){
            final EntityHorseDonkey spawned = (EntityHorseDonkey) ((CraftDonkey)h).getHandle();
            final NBTTagCompound saddleHorseTag = (NBTTagCompound)saddleTag.get("horsetag");
            spawned.a(saddleHorseTag);
            spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
            if(saddleTag.hasKey("cname"))
                spawned.setCustomName(saddleTag.getString("cname"));
        }
        if(h instanceof SkeletonHorse){
            final EntityHorseSkeleton spawned = (EntityHorseSkeleton) ((CraftSkeletonHorse)h).getHandle();
            final NBTTagCompound saddleHorseTag = (NBTTagCompound)saddleTag.get("horsetag");
            spawned.a(saddleHorseTag);
            spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
            if(saddleTag.hasKey("cname"))
                spawned.setCustomName(saddleTag.getString("cname"));
        }
        if(h instanceof ZombieHorse){
            final EntityHorseZombie spawned = (EntityHorseZombie) ((CraftZombieHorse)h).getHandle();
            final NBTTagCompound saddleHorseTag = (NBTTagCompound)saddleTag.get("horsetag");
            spawned.a(saddleHorseTag);
            spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
            if(saddleTag.hasKey("cname"))
                spawned.setCustomName(saddleTag.getString("cname"));
        }
    }

    @Override
    public double getSpeedOfHorse(LivingEntity h){
        EntityHorseAbstract nmsHorse = ((CraftAbstractHorse)h).getHandle();
        AttributeInstance speed = nmsHorse.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        double value = -1D;
        value = speed.getValue();
        return value;
    }



    @Override
    public boolean isPHorse(ItemStack saddle) {
        NBTTagCompound saddleTag;
        net.minecraft.server.v1_12_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        if(!nmsSaddle.hasTag()){
            return false;
        }
        saddleTag = nmsSaddle.getTag();
        if(saddleTag.hasKey("phorse")){
            return true;
        }
        return false;
    }

    @Override
    public boolean isSpecialSaddle(ItemStack saddle) {
        net.minecraft.server.v1_12_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        if(!nmsSaddle.hasTag()){
            return false;
        }
        if(nmsSaddle.getTag().hasKey("spsaddle")){
            return true;
        }
        return false;
    }

    @Override
    public ItemStack setSpecialSaddle(ItemStack saddle){
        net.minecraft.server.v1_12_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
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
        net.minecraft.server.v1_12_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        EntityType t = EntityType.fromName(nmsSaddle.getTag().getString("entype"));
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

    @Override
    public LivingEntity forceSpawn(EntityType type, Location spawnLocation){
        Class<? extends LivingEntity> entityClass = (Class<? extends LivingEntity>)type.getEntityClass();
        net.minecraft.server.v1_12_R1.Entity nmsEntity = ((CraftWorld)spawnLocation.getWorld()).createEntity(spawnLocation, entityClass);
        try {
            loadEntity(nmsEntity, (CraftWorld)spawnLocation.getWorld());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return (LivingEntity) nmsEntity.getBukkitEntity();
    }

    @Override
    public LivingEntity spawn(EntityType type, Location spawnLocation){
        Class<? extends LivingEntity> entityClass = (Class<? extends LivingEntity>)type.getEntityClass();

        return ((CraftWorld)spawnLocation.getWorld()).spawn(spawnLocation, entityClass, null, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public boolean isFakeSaddle(ItemStack saddle) {
        net.minecraft.server.v1_12_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        return !nmsSaddle.hasTag() ? false : nmsSaddle.getTag().hasKey("fake-saddle");
    }

    private void loadEntity(Entity entity, CraftWorld craftWorld) throws Throwable {
        World world = craftWorld.getHandle();
        ((EntityInsentient)entity).prepare(world.D(new BlockPosition(entity)), null);

        if(!(boolean)canAddEntity.invoke(world, entity)){
            return;
        }

        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);
        world.getChunkAt(i, j).a(entity);
        world.entityList.add(entity);
        onEntityAdded.invoke(world, entity);
    }

}
