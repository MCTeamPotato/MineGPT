package team.teampotato.chatgpt;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.api.ModInitializer;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import static net.minecraft.util.registry.DynamicRegistryManager.LOGGER;

public class TomlUtils implements ModInitializer {
    public static String ENDPOINT;
    public static String API_KEY;
    public static String MODEL;
    public static String MAX_TOKENS;
    public static String N;

    public static void loadConfig() {
        Toml toml = readTomlFromFile("config/MineGPTconfig.toml");

        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
        LOGGER.info("-----------------------------------------");
        LOGGER.info("[Fabric-MineGPT]Your ChatGPT Config Info:");
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Endpoint: {}", ENDPOINT);
        //LOGGER.info("API key: {}" , API_KEY);
        LOGGER.info("Model: {}" , MODEL);
        LOGGER.info("Max tokens: {}" , MAX_TOKENS);
        LOGGER.info("N: {}" , N);
        LOGGER.info("-----------------------------------------");

    }

    public static Toml readTomlFromFile(String path) {
        Toml toml = new Toml();
        try {
            toml = new Toml().read(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toml;
    }

    public static void reloadConfig() {
        Toml toml = readTomlFromFile("config/MineGPTconfig.toml");
        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
    }



    @Override
    public void onInitialize() {
        loadConfig();
    }
}
