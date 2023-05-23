package team.teampotato.chatgpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.teampotato.chatgpt.command.Command;
import team.teampotato.chatgpt.config.CreateConfig;
import team.teampotato.chatgpt.config.TOMLUtils;

import java.io.File;

public class MineGPT {
    public static final String MOD_ID = "minegpt";
    public static final String MOD_NAME = "MineGPT";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final String CONFIG = "config" + File.separator + "MineGPTconfig.toml";

    public static void init() {
        Command.onInitialize();
        CreateConfig.onInitialize();
        TOMLUtils.onInitialize();
    }
}
