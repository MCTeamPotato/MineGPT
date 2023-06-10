package team.teampotato.minegpt.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import team.teampotato.minegpt.MineGPTClient;
import team.teampotato.minegpt.fabric.command.ClientCommand;
import team.teampotato.minegpt.fabric.command.ClientConfigCommand;

public class MineGPTClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MineGPTClient.clientInit();
        ClientCommandRegistrationCallback.EVENT.register(ClientCommand::registerCommand);
        ClientCommandRegistrationCallback.EVENT.register(ClientConfigCommand::registerCommand);
    }
}
