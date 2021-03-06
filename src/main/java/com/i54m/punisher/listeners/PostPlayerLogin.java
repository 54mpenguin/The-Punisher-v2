package com.i54m.punisher.listeners;

import com.i54m.punisher.PunisherPlugin;
import com.i54m.punisher.chats.AdminChat;
import com.i54m.punisher.chats.StaffChat;
import com.i54m.punisher.handlers.ErrorHandler;
import com.i54m.punisher.managers.PunishmentManager;
import com.i54m.punisher.managers.WorkerManager;
import com.i54m.punisher.objects.Punishment;
import com.i54m.punisher.utils.NameFetcher;
import com.i54m.punisher.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PostPlayerLogin implements Listener {
    private final PunishmentManager punishmngr = PunishmentManager.getINSTANCE();
    private final PunisherPlugin plugin = PunisherPlugin.getInstance();
    private final WorkerManager workerManager = WorkerManager.getINSTANCE();

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String targetName = player.getName();
        workerManager.runWorker(new WorkerManager.Worker(() -> {
            //updated name & uuid fetcher cache
            NameFetcher.updateStoredName(uuid, targetName);
            UUIDFetcher.updateStoredUUID(targetName, uuid);
        }));
        if (punishmngr.hasPendingPunishment(uuid)) {
            ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
                try {
                    punishmngr.issue(punishmngr.getPendingPunishment(uuid), null, false, true, false);
                } catch (Exception e) {
                    ErrorHandler errorHandler = ErrorHandler.getINSTANCE();
                    errorHandler.log(e);
                    errorHandler.adminChatAlert(e, ProxyServer.getInstance().getPlayer(uuid));
                    errorHandler.alert(e, ProxyServer.getInstance().getPlayer(uuid));
                }
            }, 10, TimeUnit.SECONDS);
        }

        try {
            if (punishmngr.isBanned(uuid)) {
                if (player.hasPermission("punisher.bypass")) {
                    punishmngr.remove(punishmngr.getBan(uuid), null, true, true);
                    PunisherPlugin.getLOGS().info(player.getName() + " Bypassed their ban and were unbanned");
                    plugin.getProxy().getScheduler().schedule(plugin, () ->
                                    StaffChat.sendMessage(new ComponentBuilder(targetName + " Bypassed their ban, Unbanning...").color(ChatColor.RED).event(punishmngr.getBan(uuid).getHoverEvent()).create())
                            , 5, TimeUnit.SECONDS);
                } else {
                    Punishment ban = punishmngr.getBan(uuid);
                    if (System.currentTimeMillis() > ban.getExpiration()) {
                        punishmngr.remove(punishmngr.getBan(uuid), null, false, false);
                        PunisherPlugin.getLOGS().info(player.getName() + "'s ban expired so they were unbanned");
                    } else {
                        String timeleft = punishmngr.getTimeLeft(ban);
                        String reason = ban.getMessage();
                            String banMessage = plugin.getConfig().getString("Ban Message").replace("%timeleft%", timeleft).replace("%reason%", reason);
                            player.disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', banMessage)));
                            return;
                    }
                }
            }
        } catch (Exception e) {
            ErrorHandler errorHandler = ErrorHandler.getINSTANCE();
            errorHandler.log(e);
            errorHandler.loginError(event);
        }

        workerManager.runWorker(new WorkerManager.Worker(() -> {
            try {
                Thread.sleep(5000);
                if (!player.isConnected()) return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //check for banned alts
                ArrayList<UUID> altslist = plugin.getStorageManager().getAlts(uuid);
                if (!altslist.isEmpty()) {
                    altslist.removeIf((UUID alt) -> !punishmngr.isBanned(alt));
                    StringBuilder bannedalts = new StringBuilder();
                    int i = 0;
                    for (UUID alts : altslist) {
                        bannedalts.append(NameFetcher.getName(alts));
                        if (!(i + 1 >= altslist.size()))
                            bannedalts.append(", ");
                        i++;
                    }
                    if (bannedalts.length() >= 5 && !player.hasPermission("punisher.bypass")) {
                        if (player.hasPermission("punisher.staff")) {
                            AdminChat.sendMessage(targetName + " has a lot of banned linked accounts: " + bannedalts.toString() + ". Please talk to staff manager+ ASAP!!");
                        } else {
                            StaffChat.sendMessage(targetName + " has a lot of banned linked accounts: " + bannedalts.toString() + ". Checking banned alts to make sure they have the same ip, if more than 5 have the same all will be perm banned!!");
                            workerManager.runWorker(new WorkerManager.Worker(() -> {
                                ArrayList<UUID> confirmedAlts = new ArrayList<>();
                                try {
                                    String userIp = plugin.getStorageManager().getIpHist(uuid).firstEntry().getValue();
                                    for (UUID alts : new ArrayList<>(altslist)) {
                                        if (plugin.getStorageManager().getIpHist(alts).firstEntry().getValue().equalsIgnoreCase(userIp)) confirmedAlts.add(alts);
                                    }
                                    if (confirmedAlts.size() >= 5) {
                                        StaffChat.sendMessage("Confirmed banned alts for: " + targetName + ". Banning all confirmed alts permanently for ban evasion!");
                                        for (UUID confirmedAlt : confirmedAlts) {
                                            Punishment punishment = new Punishment(
                                                    Punishment.Type.BAN,
                                                    "BAN_EVASION",
                                                    (long) 3.154e+12 + System.currentTimeMillis(),
                                                    confirmedAlt,
                                                    NameFetcher.getName(confirmedAlt),
                                                    UUIDFetcher.getBLANK_UUID(),
                                                    null,
                                                    "Ban Evasion Auto Detection (5 or more banned confirmed alts)",
                                                    new Punishment.MetaData());
                                            punishmngr.issue(punishment, null, false, true, false);
                                        }
                                    }
                                } catch (Exception e) {
                                    ErrorHandler.getINSTANCE().log(e);
                                    ErrorHandler.getINSTANCE().adminChatAlert(e, event.getPlayer());
                                    ErrorHandler.getINSTANCE().loginError(event);
                                }
                            }));
                        }
                    } else if (bannedalts.length() > 0)
                        if (player.hasPermission("punisher.staff"))
                            AdminChat.sendMessage(targetName + " Might have banned alts: " + bannedalts.toString());
                        else
                            StaffChat.sendMessage(targetName + " Might have banned alts: " + bannedalts.toString());
                }
            } catch (Exception e) {
                ErrorHandler errorHandler = ErrorHandler.getINSTANCE();
                errorHandler.log(e);
                errorHandler.loginError(event);
            }
            if (player.isConnected() && player.getServer() != null && player.getServer().getInfo() != null) {
                if (player.hasPermission("punisher.staff"))
                    plugin.addStaff(player.getServer().getInfo(), player);
            } else plugin.getProxy().getScheduler().schedule(plugin, () -> {
                if (!player.isConnected()) return;
                if (player.hasPermission("punisher.staff"))
                    plugin.addStaff(player.getServer().getInfo(), player);
            }, 30, TimeUnit.SECONDS);
        }));
    }
}
