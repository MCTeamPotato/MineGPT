package team.teampotato.minegpt.forge;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import team.teampotato.minegpt.MineGPTClient;
import team.teampotato.minegpt.forge.command.ClientCommand;
import team.teampotato.minegpt.forge.command.ClientConfigCommand;
import team.teampotato.minegpt.forge.forged.api.ClientCommandRegistrationEvent;

public class MineGPTClientForge {
    public static void onInitializeClient(FMLClientSetupEvent event) {
        MineGPTClient.clientInit();
        ClientCommandRegistrationEvent.EVENT.register(ClientCommand::registerCommand);
        ClientCommandRegistrationEvent.EVENT.register(ClientConfigCommand::registerCommand);
    }
}
