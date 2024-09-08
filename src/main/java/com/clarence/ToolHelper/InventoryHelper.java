package com.clarence.ToolHelper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryHelper {

    public InventoryHelper (Player player) {
        Inventory inventory = getInventory(9, Util.getEconomyPlugin().getName());
        ItemStack itemStack = getItemStack(Material.GOLD_INGOT, 1, "MONEY", uuid.getUUID().get(player.getUniqueId()).toString());
        inventory.setItem(1, itemStack);
        player.openInventory(inventory);
    }

    private Inventory getInventory(int size, String title) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        return inventory;
    }
    private ItemStack getItemStack(Material itemMaterial, int itemAmount, String displayName, String... lores) {
        ItemStack itemStack = new ItemStack(itemMaterial, itemAmount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Util.setColor(displayName).toUpperCase());
        itemMeta.setLore(List.of(lores));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
