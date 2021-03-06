package com.i54m.punisher.listeners;

import com.i54m.punisher.PunisherPlugin;
import com.i54m.punisher.chats.StaffChat;
import com.i54m.punisher.exceptions.PunishmentsStorageException;
import com.i54m.punisher.handlers.ErrorHandler;
import com.i54m.punisher.managers.PunishmentManager;
import com.i54m.punisher.objects.Punishment;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class PlayerChat implements Listener {
    private final PunisherPlugin plugin = PunisherPlugin.getInstance();
    private final PunishmentManager punishmentManager = PunishmentManager.getINSTANCE();

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        ServerInfo server = player.getServer().getInfo();
        if (plugin.chatOffServers.contains(server)) {
            if (!player.hasPermission("punisher.togglechat.bypass")) {
                if (event.isCommand()) {
                    String[] args = event.getMessage().split(" ");
                    List<String> mutedcommands;
                    mutedcommands = plugin.getConfig().getStringList("Muted Commands");
                    if (mutedcommands.contains(args[0])) {
                        event.setCancelled(true);
                        player.sendMessage(new ComponentBuilder(plugin.getPrefix()).append("You may not use that command at this time!").color(ChatColor.RED).create());
                    } else {
                        event.setCancelled(false);
                    }
                    return;
                }
                event.setCancelled(true);
                player.sendMessage(new ComponentBuilder(plugin.getPrefix()).append("You may not chat at this time!").color(ChatColor.RED).create());
            } else {
                event.setCancelled(false);
            }
            return;
        }
        UUID uuid = player.getUniqueId();
        String targetname = player.getName();
        try {
            if (punishmentManager.isMuted(uuid)) {
                Punishment mute = punishmentManager.getMute(uuid);
                if (player.hasPermission("punisher.bypass")) {
                    punishmentManager.remove(mute, null, true, true);
                    StaffChat.sendMessage(new ComponentBuilder(player.getName() + " Bypassed their mute, Unmuting...").color(ChatColor.RED).event(mute.getHoverEvent()).create());
                    return;
                }
                if (System.currentTimeMillis() > mute.getExpiration()) {
                    punishmentManager.remove(mute, null, false, false);
                    player.sendMessage(new ComponentBuilder(plugin.getPrefix()).append("Your Mute has expired!").color(ChatColor.GREEN).create());
                    PunisherPlugin.getLOGS().info(player.getName() + "'s mute expired so they were unmuted");
                } else {
                    String timeLeft = punishmentManager.getTimeLeft(mute);
                    if (event.isCommand()) {
                        String[] args = event.getMessage().split(" ");
                        List<String> mutedcommands;
                        mutedcommands = plugin.getConfig().getStringList("Muted Commands");
                        if (!mutedcommands.contains(args[0])) {
                            event.setCancelled(true);
                            String muteMessage = plugin.getConfig().getString("Mute Deny Message").replace("%reason%", mute.getMessage()).replace("%timeleft%", timeLeft);

                            player.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', muteMessage)).create());
                        }
                    } else {
                        event.setCancelled(true);
                        String muteMessage = plugin.getConfig().getString("Mute Deny Message").replace("%reason%", mute.getMessage()).replace("%timeleft%", timeLeft);
                        player.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', muteMessage)).create());
                        if (plugin.getConfig().getBoolean("SendPlayersMessageToStaffChatOnMuteDeny"))
                            StaffChat.sendMessage(player.getName() + " Tried to speak but is muted: " + event.getMessage());
                        else if (plugin.getConfig().getBoolean("StaffChatOnMuteDeny"))
                            StaffChat.sendMessage(player.getName() + " Tried to speak but is muted!");
                    }
                }
            }
        } catch (Exception e) {
            PunishmentsStorageException pse = new PunishmentsStorageException("Removing mute on a player", targetname, this.getClass().getName(), e);
            ErrorHandler errorHandler = ErrorHandler.getINSTANCE();
            errorHandler.log(pse);
            errorHandler.alert(pse, player);
            errorHandler.adminChatAlert(pse, player);
            event.setCancelled(true);
        }
    }
}