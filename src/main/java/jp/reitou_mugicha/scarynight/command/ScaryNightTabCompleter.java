package jp.reitou_mugicha.scarynight.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ScaryNightTabCompleter implements TabCompleter
{
    private final List<String> subCommands = Arrays.asList("start", "end", "language", "reload");
    private final List<String> languages = Arrays.asList("en", "ja");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args)
    {
        if (args.length == 1)
        {
            return subCommands;
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("language"))
        {
            return languages;
        }

        return null;
    }
}
