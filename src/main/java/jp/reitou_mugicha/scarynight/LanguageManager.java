package jp.reitou_mugicha.scarynight;

import com.earth2me.essentials.libs.geantyref.TypeToken;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager
{
    private final SimplePlugin plugin;
    private String language;
    private Map<String, String> messages = new HashMap<>();

    private final String MESSAGE_PREFIX = "§l§6[ScaryNight]§r ";

    public LanguageManager(SimplePlugin plugin)
    {
        this.plugin = plugin;
        this.language = plugin.getConfig().getString("language", "ja");
    }

    public void loadLanguageFile()
    {
        File langDir = new File(plugin.getDataFolder(), "language");
        if (!langDir.exists()) {
            langDir.mkdirs();
        }

        File langFile = new File(plugin.getDataFolder(), "language/" + language + ".json");
        if (!langFile.exists())
        {
            saveDefaultResource("language/" + language + ".json");
        }

        try (FileReader reader = new FileReader(langFile))
        {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            messages = gson.fromJson(reader, type);
        }
        catch (IOException e)
        {
            plugin.getLogger().info("Failed to load language file.");
            e.printStackTrace();
        }
    }

    private void saveDefaultResource(String resourcePath) {
        File destFile = new File(plugin.getDataFolder(), resourcePath);
        if (!destFile.exists()) {
            plugin.saveResource(resourcePath, false);
        }
    }

    /**
     * 指定したキーのメッセージを取得する
     * @param key メッセージキー
     * @return 翻訳されたメッセージ
     */
    public String getMessage(String key) {
        return MESSAGE_PREFIX + messages.getOrDefault(key, key);
    }

    /**
     * 言語を変更する
     * @param newLang 新しい言語コード
     */
    public void setLanguage(String newLang) {
        this.language = newLang;
        plugin.getConfig().set("language", newLang);
        plugin.saveConfig();
        loadLanguageFile();
    }

    public String getCurrentLanguage() {
        return language;
    }
}