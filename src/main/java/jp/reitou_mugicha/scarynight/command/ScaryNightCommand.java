package jp.reitou_mugicha.scarynight.command;

import jp.reitou_mugicha.scarynight.ScaryNight;
import jp.reitou_mugicha.scarynight.ScaryNightController;
import jp.reitou_mugicha.scarynight.LanguageManager;
import jp.reitou_mugicha.scarynight.ScaryNightEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ScaryNightCommand implements CommandExecutor
{
    private final ScaryNight plugin;
    private final LanguageManager languageManager;
    private final ScaryNightController controller;
    private final ScaryNightEvent event;

    public ScaryNightCommand(ScaryNight plugin)
    {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
        this.controller = plugin.getController();
        this.event = plugin.getEvent();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(languageManager.getMessage("scarynight.command.info"));
            return true;
        }

        switch (args[0].toLowerCase())
        {
            case "start":
                controller.startScaryNight();
                break;
            case "end":
                controller.endScaryNight();
                break;
            case "language":
                if (args.length < 2)
                {
                    sender.sendMessage(languageManager.getMessage("scarynight.command.language"));
                    return true;
                }

                languageManager.setLanguage(args[1]);
                sender.sendMessage(languageManager.getMessage("scarynight.command.language_changed"));
                break;
            case "reload":
                sender.sendMessage(languageManager.getMessage("scarynight.command.reload"));
                event.reloadSettings(plugin);
                break;
            default:
                sender.sendMessage(languageManager.getMessage("scarynight.command.help"));
                break;
        }

        return true;
    }
}