package team.teampotato.minegpt.fabric;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.api.ModInitializer;

import team.teampotato.minegpt.MineGPT;
import team.teampotato.minegpt.fabric.command.ClientCommand;
import team.teampotato.minegpt.fabric.command.ClientConfigCommand;

public class MineGPTFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MineGPT.init();
        ClientCommandRegistrationCallback.EVENT.register(ClientCommand::registerCommand);
        ClientCommandRegistrationCallback.EVENT.register(ClientConfigCommand::registerCommand);
    }
}
