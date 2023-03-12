package team.teampotato.chatgpt;

import net.fabricmc.api.ModInitializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateConfig implements ModInitializer {

    public static void main(String[] args) {
        String filePath = "config/MineGPTconfig.toml";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("endpoint = \"API_Link\"\n");
                writer.write("api_key = \"OpenAI_API_Key\"\n");
                writer.write("model = \"AI_Model\"\n");
                writer.write("prompt = \"Undeveloped\"\n");
                writer.write("max_tokens = \"Max_Tokens\"\n");
                writer.write("n = \"N\"\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void onInitialize() {
        CreateConfig.main(new String[0]);
    }
}
