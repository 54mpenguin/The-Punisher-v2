package com.i54m.punisher.fetchers;

import com.i54m.punisher.managers.PunishmentManager;
import com.i54m.punisher.objects.Punishment;
import com.i54m.punisher.utils.NameFetcher;
import com.i54m.punisher.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.UUID;
import java.util.concurrent.Callable;

public class Status implements Callable<BaseComponent[]> {

    private UUID targetuuid;
    private final PunishmentManager punishMnger = PunishmentManager.getINSTANCE();

    public void setTargetuuid(UUID targetuuid) {
        this.targetuuid = targetuuid;
    }

    @Override
    public BaseComponent[] call() {
        ComponentBuilder status = new ComponentBuilder("Current Status: ").color(ChatColor.GREEN);
        if (punishMnger.hasActivePunishment(targetuuid)) {
            if (punishMnger.isMuted(targetuuid)) {
                Punishment punishment = punishMnger.getMute(targetuuid);
                String timeLeft = punishMnger.getTimeLeft(punishment);
                String reasonMessage = punishment.getMessage() == null ? punishment.getReason().replace("_", " ") : punishment.getMessage();
                String punisher = punishment.getPunisherUUID().equals(UUIDFetcher.getBLANK_UUID()) ? "CONSOLE" : NameFetcher.getName(punishment.getPunisherUUID());
                status.append("Muted for " + timeLeft + ". Reason: " + reasonMessage + " by: " + punisher).color(ChatColor.YELLOW).event(punishment.getHoverEvent());
            }
            if (punishMnger.isBanned(targetuuid)) {
                if (punishMnger.isMuted(targetuuid))
                    status.append(" & ").color(ChatColor.WHITE);
                Punishment punishment = punishMnger.getBan(targetuuid);
                String timeLeft = punishMnger.getTimeLeft(punishment);
                String reasonMessage = punishment.getMessage() == null ? punishment.getReason().replace("_", " ") : punishment.getMessage();
                String punisher = punishment.getPunisherUUID().equals(UUIDFetcher.getBLANK_UUID()) ? "CONSOLE" : NameFetcher.getName(punishment.getPunisherUUID());
                status.append("Banned for " + timeLeft + ". Reason: " + reasonMessage + " by: " + punisher).color(ChatColor.RED).event(punishment.getHoverEvent());
            }
        } else status.append("No currently active punishments!").color(ChatColor.GREEN);
        return status.create();
    }
}
