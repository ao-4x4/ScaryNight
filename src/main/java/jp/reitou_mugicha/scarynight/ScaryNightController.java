package jp.reitou_mugicha.scarynight;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScaryNightController
{
    private final LanguageManager languageManager;
    public ScaryNightController(ScaryNight plugin)
    {
        this.languageManager = plugin.getLanguageManager();
    }

    private boolean isScaryNightActive = false;

    public void startScaryNight()
    {
        if (!isScaryNightActive)
        {
            isScaryNightActive = true;

            Bukkit.broadcast(Component.text(languageManager.getMessage("scarynight.start")));
            for (Player player : Bukkit.getOnlinePlayers())
            {
                player.playSound(player.getLocation(), "entity.wither.spawn", 1.0f, 1.0f);
            }
        }
    }

    public void endScaryNight()
    {
        if (isScaryNightActive)
        {
            isScaryNightActive = false;

            Bukkit.broadcast(Component.text(languageManager.getMessage("scarynight.end")));
            for (Player player : Bukkit.getOnlinePlayers())
            {
                player.playSound(player.getLocation(), "entity.wither.death", 1.0f, 1.0f);
            }
        }
    }

    public boolean isScaryNightActive()
    {
        return isScaryNightActive;
    }
}