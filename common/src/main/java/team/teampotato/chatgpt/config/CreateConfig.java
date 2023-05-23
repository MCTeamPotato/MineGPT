package team.teampotato.chatgpt.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CreateConfig {
    public static void onInitialize() {
        String folderPath = "config" + File.separator;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String filePath = folderPath + "MineGPTconfig.toml";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                    writer.write("endpoint = \"API_Link\"\n");
                    writer.write("api_key = \"OpenAI_API_Key\"\n");
                    writer.write("model = \"AI_Model\"\n");
                    writer.write("prompt = \"Undeveloped\"\n");
                    writer.write("max_tokens = \"Max_Tokens\"\n");
                    writer.write("n = \"N\"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
