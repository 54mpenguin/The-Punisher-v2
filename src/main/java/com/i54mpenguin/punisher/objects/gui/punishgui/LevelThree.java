package com.i54mpenguin.punisher.objects.gui.punishgui;

import com.i54mpenguin.protocol.inventory.InventoryType;
import com.i54mpenguin.protocol.items.ItemFlag;
import com.i54mpenguin.protocol.items.ItemStack;
import com.i54mpenguin.protocol.items.ItemType;
import com.i54mpenguin.punisher.objects.IconMenu;
import com.i54mpenguin.punisher.objects.gui.ConfirmationGUI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LevelThree {

    public static final IconMenu MENU = new IconMenu(InventoryType.GENERIC_9X6);

    public static void open(final ProxiedPlayer punisher, String targetuuid, String targetName, String reputation) {
        MENU.setTitle(new ComponentBuilder("Punish: " + targetName + " ").color(ChatColor.RED).appendLegacy(reputation).create());
        MENU.setClick((player, menu, slot, item) -> {
            if (item.getLore() != null && !item.getLore().isEmpty()) {
                menu.close(player);
                ConfirmationGUI.open(punisher, targetuuid, targetName, item, slot);
                return true;
            }
            return false;
        });
        MENU.open(punisher);
    }

    public static void setupMenu() {
        ItemStack fill = new ItemStack(ItemType.RED_STAINED_GLASS_PANE, 1);
        ItemStack ironBoots = new ItemStack(ItemType.IRON_BOOTS, 1);
        ironBoots.setFlag(ItemFlag.HIDE_ATTRIBUTES, true);
        ItemStack woodSword = new ItemStack(ItemType.WOODEN_SWORD, 1);
        woodSword.setFlag(ItemFlag.HIDE_ATTRIBUTES, true);
        ItemStack ironSword = new ItemStack(ItemType.IRON_SWORD, 1);
        ironSword.setFlag(ItemFlag.HIDE_ATTRIBUTES, true);
        ItemStack diamondSword = new ItemStack(ItemType.DIAMOND_SWORD, 1);
        diamondSword.setFlag(ItemFlag.HIDE_ATTRIBUTES, true);
        MENU.addButton(0, fill, " ");
        MENU.addButton(1, fill, " ");
        MENU.addButton(2, fill, " ");
        MENU.addButton(3, fill, " ");
        MENU.addButton(4, fill, " ");
        MENU.addButton(5, fill, " ");
        MENU.addButton(6, fill, " ");
        MENU.addButton(7, fill, " ");
        MENU.addButton(8, fill, " ");
        MENU.addButton(9, fill, " ");
        MENU.addButton(10, fill, " ");
        MENU.addButton(11, new ItemStack(ItemType.BLUE_ICE, 1), ChatColor.RED + "Minor Chat Offence", ChatColor.WHITE + "Spam, Flood ETC");
        MENU.addButton(12, new ItemStack(ItemType.MAGMA_BLOCK, 1), ChatColor.RED + "Major Chat Offence", ChatColor.WHITE + "Racism, Disrespect ETC");
        MENU.addButton(13, ironBoots, ChatColor.RED + "DDoS/DoX Threats", ChatColor.WHITE + "Includes Hinting At It and", ChatColor.WHITE + "Saying They Have a Player's DoX");
        MENU.addButton(14, new ItemStack(ItemType.WRITABLE_BOOK, 1), ChatColor.RED + "Inappropriate Link", ChatColor.WHITE + "Includes Pm's");
        MENU.addButton(15, new ItemStack(ItemType.DRAGON_HEAD, 1), ChatColor.RED + "Scamming", ChatColor.WHITE + "When a player is unfairly taking a player's money", ChatColor.WHITE + "Through a fake scheme");
        MENU.addButton(16, fill, " ");
        MENU.addButton(17, fill, " ");
        MENU.addButton(18, new ItemStack(ItemType.GLASS, 1), ChatColor.RED + "X-Raying", ChatColor.WHITE + "Mining Straight to Ores/Bases/Chests", ChatColor.WHITE + "Includes Chest Esp and Player Esp");
        MENU.addButton(19, woodSword, ChatColor.RED + "AutoClicker (Non PvP)", ChatColor.WHITE + "Using AutoClicker to Farm Mobs ETC");
        MENU.addButton(20, new ItemStack(ItemType.FEATHER, 1), ChatColor.RED + "Fly/Speed Hacking", ChatColor.WHITE + "Includes Hacks Such as Jesus and Spider");
        MENU.addButton(21, diamondSword, ChatColor.RED + "Malicous PvP Hacks", ChatColor.WHITE + "Includes Hacks Such as Kill Aura and Reach");
        MENU.addButton(22, ironSword, ChatColor.RED + "Disallowed Mods", ChatColor.WHITE + "Includes Hacks Such as Derp and Headless");
        MENU.addButton(23, new ItemStack(ItemType.TNT, 1), ChatColor.RED + "Greifing", ChatColor.WHITE + "Excludes Cobble Monstering and Bypassing land claims", ChatColor.WHITE + "Includes Things Such as 'lava curtaining' and TnT Cannoning");
        MENU.addButton(24, new ItemStack(ItemType.OAK_SIGN, 1), ChatColor.RED + "Server Advertisement", ChatColor.RED + "Warning: Must Also Clear Chat After you have proof!!");
        MENU.addButton(25, new ItemStack(ItemType.PURPLE_SHULKER_BOX, 1), ChatColor.RED + "Exploiting", ChatColor.WHITE + "Includes Bypassing Land Claims and Cobble Monstering");
        MENU.addButton(26, new ItemStack(ItemType.IRON_TRAPDOOR, 1), ChatColor.RED + "TPA-Trapping", ChatColor.WHITE + "Sending a TPA Request to Someone", ChatColor.WHITE + "and Then Killing Them Once They Tp");
        MENU.addButton(27, fill, " ");
        MENU.addButton(28, fill, " ");
        MENU.addButton(29, fill, " ");
        MENU.addButton(30, new ItemStack(ItemType.ZOMBIE_HEAD, 1), ChatColor.RED + "Other Minor Offence", ChatColor.WHITE + "For Other Minor Offences", ChatColor.WHITE + "(Will ban for 7 days regardless of offence)");
        MENU.addButton(31, new ItemStack(ItemType.PLAYER_HEAD, 1), ChatColor.RED + "Impersonation", ChatColor.WHITE + "Any type of Impersonation");
        MENU.addButton(32, new ItemStack(ItemType.CREEPER_HEAD, 1), ChatColor.RED + "Other Major Offence", ChatColor.WHITE + "Includes Inappropriate IGN's and Other Major Offences", ChatColor.WHITE + "(Will ban for 30 days regardless of offence)");
        MENU.addButton(33, fill, " ");
        MENU.addButton(34, fill, " ");
        MENU.addButton(35, fill, " ");
        MENU.addButton(36, new ItemStack(ItemType.COBBLESTONE, 1), ChatColor.RED + "Warn", ChatColor.WHITE + "Manually Warn the Player");
        MENU.addButton(37, new ItemStack(ItemType.COAL, 1), ChatColor.RED + "1 Hour Mute", ChatColor.WHITE + "Manually Mute the Player For 1 Hour");
        MENU.addButton(38, new ItemStack(ItemType.IRON_INGOT, 1), ChatColor.RED + "1 Day Mute", ChatColor.WHITE + "Manually Mute the Player For 1 Day");
        MENU.addButton(39, new ItemStack(ItemType.GOLD_INGOT, 1), ChatColor.RED + "3 Day Mute", ChatColor.WHITE + "Manually Mute the Player For 3 Days");
        MENU.addButton(40, new ItemStack(ItemType.LAPIS_LAZULI, 1), ChatColor.RED + "1 Week Mute", ChatColor.WHITE + "Manually Mute the Player For 1 Week");
        MENU.addButton(41, new ItemStack(ItemType.REDSTONE, 1), ChatColor.RED + "2 Week Mute", ChatColor.WHITE + "Manually Mute the Player For 2 Weeks");
        MENU.addButton(42, new ItemStack(ItemType.DIAMOND, 1), ChatColor.RED + "3 Week Mute", ChatColor.WHITE + "Manually Mute the Player For 3 Weeks");
        MENU.addButton(43, new ItemStack(ItemType.EMERALD, 1), ChatColor.RED + "1 Month Mute", ChatColor.WHITE + "Manually Mute the Player For 1 Month");
        MENU.addButton(44, new ItemStack(ItemType.BARRIER, 1), ChatColor.RED + "Perm Mute", ChatColor.WHITE + "Manually Mute the Player Permanently");
        MENU.addButton(45, new ItemStack(ItemType.STONE, 1), ChatColor.RED + "Kick", ChatColor.WHITE + "Manually Kick the Player");
        MENU.addButton(46, new ItemStack(ItemType.COAL_ORE, 1), ChatColor.RED + "1 Hour Ban", ChatColor.WHITE + "Manually Ban The Player For 1 Hour");
        MENU.addButton(47, new ItemStack(ItemType.IRON_ORE, 1), ChatColor.RED + "1 Day Ban", ChatColor.WHITE + "Manually Ban the Player For 1 Day");
        MENU.addButton(48, new ItemStack(ItemType.GOLD_ORE, 1), ChatColor.RED + "3 Day Ban", ChatColor.WHITE + "Manually Ban the Player For 3 Days");
        MENU.addButton(49, new ItemStack(ItemType.LAPIS_ORE, 1), ChatColor.RED + "1 Week Ban", ChatColor.WHITE + "Manually Ban the Player For 1 Week");
        MENU.addButton(50, new ItemStack(ItemType.REDSTONE_ORE, 1), ChatColor.RED + "2 Week Ban", ChatColor.WHITE + "Manually Ban the Player For 2 Weeks");
        MENU.addButton(51, new ItemStack(ItemType.DIAMOND_ORE, 1), ChatColor.RED + "3 Week Ban", ChatColor.WHITE + "Manually Ban the Player For 3 Weeks");
        MENU.addButton(52, new ItemStack(ItemType.EMERALD_ORE, 1), ChatColor.RED + "1 Month Ban", ChatColor.WHITE + "Manually Ban the Player For 1 Month");
        MENU.addButton(53, new ItemStack(ItemType.BEDROCK, 1), ChatColor.RED + "Perm Ban", ChatColor.WHITE + "Manually Ban the Player Permanently");


    }
}
