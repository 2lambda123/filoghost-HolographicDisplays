package com.gmail.filoghost.holographicdisplays.nms.v1_8_R2;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.ItemPickupManager;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSItem;
import com.gmail.filoghost.holographicdisplays.util.ConsoleLogger;
import com.gmail.filoghost.holographicdisplays.util.bukkit.ItemUtils;
import com.gmail.filoghost.holographicdisplays.util.ReflectionUtils;

import net.minecraft.server.v1_8_R2.Blocks;
import net.minecraft.server.v1_8_R2.DamageSource;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntityItem;
import net.minecraft.server.v1_8_R2.EntityPlayer;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagList;
import net.minecraft.server.v1_8_R2.NBTTagString;
import net.minecraft.server.v1_8_R2.World;

public class EntityNMSItem extends EntityItem implements NMSItem {
	
	private boolean lockTick;
	private ItemLine parentPiece;
	private ItemPickupManager itemPickupManager;
	
	public EntityNMSItem(World world, ItemLine piece, ItemPickupManager itemPickupManager) {
		super(world);
		super.pickupDelay = Integer.MAX_VALUE;
		this.parentPiece = piece;
		this.itemPickupManager = itemPickupManager;
	}
	
	@Override
	public void t_() {
		
		// So it won't getCurrent removed.
		ticksLived = 0;
		
		if (!lockTick) {
			super.t_();
		}
	}
	
	// Method called when a player is near.
	@Override
	public void d(EntityHuman human) {
		
		if (human.locY < this.locY - 1.5 || human.locY > this.locY + 1.0) {
			// Too low or too high, it's a bit weird.
			return;
		}
		
		if (parentPiece.getPickupHandler() != null && human instanceof EntityPlayer) {
			itemPickupManager.handleItemLinePickup((Player) human.getBukkitEntity(), parentPiece.getPickupHandler(), parentPiece.getParent());
			// It is never added to the inventory.
		}
	}
	
	@Override
	public void b(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}
	
	@Override
	public void e(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	@Override
	public boolean isInvulnerable(DamageSource source) {
		/*
		 * The field Entity.invulnerable is private.
		 * It's only used while saving NBTTags, but since the entity would be killed
		 * on chunk unload, we prefer to override isInvulnerable().
		 */
	    return true;
	}
	
	@Override
	public void inactiveTick() {
		// Check inactive ticks.
		
		if (!lockTick) {
			super.inactiveTick();
		}
	}

	@Override
	public void setLockTick(boolean lock) {
		lockTick = lock;
	}
	
	@Override
	public void die() {
		setLockTick(false);
		super.die();
	}

	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftNMSItem(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}
	
	@Override
	public void killEntityNMS() {
		die();
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
	}

	@Override
	public void setItemStackNMS(org.bukkit.inventory.ItemStack stack) {
		ItemStack newItem = CraftItemStack.asNMSCopy(stack);
		
		if (newItem == null) {
			newItem = new ItemStack(Blocks.BEDROCK);
		}
		
		if (newItem.getTag() == null) {
			newItem.setTag(new NBTTagCompound());
		}
		NBTTagCompound display = newItem.getTag().getCompound("display");
		
		if (!newItem.getTag().hasKey("display")) {
		newItem.getTag().set("display", display);
		}
		
		NBTTagList tagList = new NBTTagList();
		tagList.add(new NBTTagString(ItemUtils.ANTISTACK_LORE)); // Antistack lore
		
		display.set("Lore", tagList);
		newItem.count = 0;
		setItemStack(newItem);
	}
	
	@Override
	public int getIdNMS() {
		return this.getId();
	}
	
	@Override
	public ItemLine getHologramLine() {
		return parentPiece;
	}
	
	@Override
	public void allowPickup(boolean pickup) {
		if (pickup) {
			super.pickupDelay = 0;
		} else {
			super.pickupDelay = Integer.MAX_VALUE;
		}
	}
	
	@Override
	public org.bukkit.entity.Entity getBukkitEntityNMS() {
		return getBukkitEntity();
	}
	
	@Override
	public void setPassengerOfNMS(NMSEntityBase vehicleBase) {
		if (vehicleBase == null || !(vehicleBase instanceof Entity)) {
			// It should never dismount
			return;
		}
		
		Entity entity = (Entity) vehicleBase;
		
		try {
			ReflectionUtils.setPrivateField(Entity.class, this, "ar", 0.0);
			ReflectionUtils.setPrivateField(Entity.class, this, "as", 0.0);
		} catch (Exception ex) {
			ConsoleLogger.error(ex);
		}

        if (this.vehicle != null) {
        	this.vehicle.passenger = null;
        }
        
        this.vehicle = entity;
        entity.passenger = this;
	}
	
	@Override
	public Object getRawItemStack() {
		return super.getItemStack();
	}
}
