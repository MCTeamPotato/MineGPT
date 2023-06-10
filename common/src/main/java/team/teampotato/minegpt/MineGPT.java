package team.teampotato.minegpt;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.teampotato.minegpt.command.ServerCommand;
import team.teampotato.minegpt.command.ServerConfigCommand;
import team.teampotato.minegpt.config.Config;

public class MineGPT {
    public static final String MOD_ID = "minegpt";
    public static final String MOD_NAME = "MineGPT";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        Config.onInitialize();
        CommandRegistrationEvent.EVENT.register(ServerCommand::registerCommand);
        CommandRegistrationEvent.EVENT.register(ServerConfigCommand::registerCommand);
    }
}
