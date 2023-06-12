package team.teampotato.minegpt.forge;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import team.teampotato.minegpt.forge.command.ClientCommand;
import team.teampotato.minegpt.forge.command.ClientConfigCommand;

public class MineGPTClientForge {
    public static void onInitializeClient(FMLClientSetupEvent event) {
        ClientCommand.registerCommand();
        ClientConfigCommand.registerCommand();
    }
}
