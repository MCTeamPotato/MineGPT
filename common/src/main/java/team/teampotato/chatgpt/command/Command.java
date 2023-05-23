package team.teampotato.chatgpt.command;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import team.teampotato.chatgpt.MineGPT;
import team.teampotato.chatgpt.config.TOMLUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Command {
    public Command() {
        TOMLUtils.loadConfig();
    }
    private static final String CONFIG = MineGPT.CONFIG;
    private static Toml toml;
    public static void onInitialize() {
        // Register the "/chatgpt" command
        CommandRegistrationEvent.EVENT.register(MineGPTCommand::registerGPTCommand);

        try {
            toml = readOrCreateConfig();
        } catch (IOException e) {
            MineGPT.LOGGER.error("Failed to load config file: {}", e.getMessage());
        }
        CommandRegistrationEvent.EVENT.register(MineGPTCommand::registerModCommand);
    }

    private static Toml readOrCreateConfig() throws IOException {
        Path configFile = Paths.get(CONFIG);
        if (!Files.exists(configFile)) {
            Files.createDirectories(configFile.getParent());
            Files.createFile(configFile);
            Toml toml = new Toml();
            TomlWriter writer = new TomlWriter();
            writer.write(toml, new File(CONFIG));
        }
        return new Toml().read(new File(CONFIG));
    }
    static boolean isValidApiKey(String apiKey) {
        if (!apiKey.startsWith("sk-")) {
            return false;
        }
        return true;
    }
}
