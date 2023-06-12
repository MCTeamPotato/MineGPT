package team.teampotato.minegpt.fabric;

import net.fabricmc.api.ClientModInitializer;

import team.teampotato.minegpt.MineGPTClient;
import team.teampotato.minegpt.fabric.command.ClientCommand;
import team.teampotato.minegpt.fabric.command.ClientConfigCommand;

public class MineGPTClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MineGPTClient.clientInit();
        ClientCommand.registerCommand();
        ClientConfigCommand.registerCommand();
    }
}
