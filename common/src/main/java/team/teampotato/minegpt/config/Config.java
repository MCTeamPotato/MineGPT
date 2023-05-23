package team.teampotato.minegpt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import team.teampotato.minegpt.MineGPT;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Config {
    public static String ENDPOINT = "https://api.openai.com/v1/completions";
    public static String API_KEY = "Your OpenAI API";
    public static String MODEL = "text-davinci-003";
    public static String MAX_TOKENS = "1024";
    public static String N = "1";
    public static final String JSON = "MineGPT.json";
    public static int Ping = 10;
    static Locale defaultLocale = Locale.getDefault();
    static String language = defaultLocale.getLanguage();


    public static void onInitialize() {
        MineGPT.LOGGER.info("Your system language is: " + language);
        loadConfig();
    }

    public static void loadConfig() {
        File configFolder = new File("config" + File.separator + "MineGPT");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        File configFile = new File(configFolder, JSON);
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                MineGPT.LOGGER.info("Loading configuration file...");
                JsonObject config = new Gson().fromJson(reader, JsonObject.class);
                ENDPOINT = config.get("API_Link").getAsString();
                API_KEY = config.get("API_KEY").getAsString();
                MODEL = config.get("MODEL").getAsString();
                MAX_TOKENS = config.get("MAX_TOKENS").getAsString();
                N = config.get("N").getAsString();
                Ping = config.get("Ping").getAsInt();

            } catch (IOException e) {
                e.printStackTrace();
                MineGPT.LOGGER.error("Failed to load configuration file!" + e);
            }
        } else {
            try {
                MineGPT.LOGGER.info("Generating configuration file...");
                configFile.createNewFile();
                JsonObject config = new JsonObject();
                config.addProperty("API_Link","https://api.openai.com/v1/completions");
                config.addProperty("API_KEY", "Your OpenAI API");
                config.addProperty("MODEL", "gpt-3.5-turbo");
                config.addProperty("MAX_TOKENS","1024");
                config.addProperty("N","1");
                config.addProperty("Ping",10);


                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
                }
            } catch (IOException e) {
                e.printStackTrace();
                MineGPT.LOGGER.info("Error generating configuration file!" + e);
            }
        }
    }
}
