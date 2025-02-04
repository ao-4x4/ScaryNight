package jp.reitou_mugicha.scarynight;

import jp.reitou_mugicha.scarynight.command.ScaryNightCommand;
import jp.reitou_mugicha.scarynight.command.ScaryNightTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Random;

public final class ScaryNight extends SimplePlugin
{
    private LanguageManager languageManager;
    private ScaryNightController controller;
    private ScaryNightEvent event;

    @Override
    public void onPluginStart()
    {
        saveDefaultConfig();

        languageManager = new LanguageManager(this);
        controller = new ScaryNightController(this);
        event = new ScaryNightEvent(this);

        languageManager.loadLanguageFile();

        new BukkitRunnable() {
            private boolean wasDay = true;

            @Override
            public void run() {
                World world = Bukkit.getWorlds().get(0);
                long time = world.getTime();

                if (wasDay && time >= 13000) {
                    wasDay = false;
                    if (new Random().nextInt(10) == 0)
                    {
                        controller.startScaryNight();
                    }
                } else if (time < 13000) {
                    controller.endScaryNight();
                    wasDay = true;
                }
            }
        }.runTaskTimer(ScaryNight.getInstance(), 0L, 100L);

        getServer().getPluginManager().registerEvents(event, this);
        registerCommands();

        getLogger().info("ScaryNight is Enabled.");
    }

    @Override
    public void onPluginStop()
    {
        getLogger().info("ScaryNight is Disabled.");
    }

    @Override
    public void onPluginLoad()
    {
        saveDefaultConfig();
    }

    @Override
    protected void onPluginReload()
    {
        reloadConfig();
        languageManager.loadLanguageFile();
        event.reloadSettings(this);

        getLogger().info(languageManager.getMessage("scarynight.command.reload"));
    }

    private void registerCommands()
    {
        getCommand("scarynight").setExecutor(new ScaryNightCommand(this));
        getCommand("scarynight").setTabCompleter(new ScaryNightTabCompleter());
    }

    public LanguageManager getLanguageManager()
    {
        return languageManager;
    }

    public ScaryNightController getController()
    {
        return controller;
    }

    public ScaryNightEvent getEvent()
    {
        return event;
    }
}