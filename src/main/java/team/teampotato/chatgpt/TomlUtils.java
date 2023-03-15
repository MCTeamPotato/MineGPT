package team.teampotato.chatgpt;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;


public class TomlUtils implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Toml toml;

    private static String ENDPOINT;
    private static String API_KEY;
    static String MODEL;
    private static String MAX_TOKENS;
    private static String N;

    public static void loadConfig() {
        toml = readTomlFromFile("config" + File.separator + "MineGPTconfig.toml");
        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");

    }

    public static String getEndpoint() {
        return ENDPOINT;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getModel() {
        return MODEL;
    }

    public static String getMaxTokens() {
        return MAX_TOKENS;
    }

    public static String getN() {
        return N;
    }


    public static Toml readTomlFromFile(String path) {
        Toml toml = null;
        try {
            String path2 = "config" + File.separator + "MineGPTconfig.toml";
            InputStream inputStream = new FileInputStream(new File(path2));
            if (inputStream != null) {
                toml = new Toml().read(new InputStreamReader(inputStream));
            } else {
                LOGGER.warn("Failed to find file {}", path);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read file {}", path, e);
        }
        return toml;
    }


    public static void reloadConfig() {
        Toml toml = readTomlFromFile("config" + File.separator + "MineGPTconfig.toml");
        ENDPOINT = toml.getString("endpoint");
        API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        MAX_TOKENS = toml.getString("max_tokens");
        N = toml.getString("n");
    }
    public static void writeTomlToFile(Map<String, Object> map, String path) throws IOException {
        // 创建一个TomlWriter对象
        TomlWriter writer = new TomlWriter();
        // 创建一个File对象
        File file = new File(path);
        // 读取原有文件中的API_KEY的值
        Toml toml = new Toml().read(file);
        String API_KEY = toml.getString("API_KEY");
        // 调用writer.write方法将map写入到file中
        writer.write(map, file);
        // 将API_KEY的值追加到文件末尾
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write("\napi_key = \"" + API_KEY + "\"\n");
        fileWriter.close();
    }



    @Override
    public void onInitialize() {
        loadConfig();

        LOGGER.info("-----------------------------------------");
        LOGGER.info("[Fabric-MineGPT] Your ChatGPT Config Info:");
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Endpoint: {}", ENDPOINT);
        //LOGGER.info("API key: {}" , API_KEY);
        LOGGER.info("Model: {}" , MODEL);
        LOGGER.info("Max tokens: {}" , MAX_TOKENS);
        LOGGER.info("N: {}" , N);
        LOGGER.info("-----------------------------------------");
    }
}
