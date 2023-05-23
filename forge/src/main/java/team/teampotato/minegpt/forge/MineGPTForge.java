package team.teampotato.minegpt.forge;

import dev.architectury.platform.forge.EventBuses;
import team.teampotato.minegpt.MineGPT;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.teampotato.minegpt.forge.command.ClientCommand;
import team.teampotato.minegpt.forge.command.ClientConfigCommand;
import team.teampotato.minegpt.forge.forged.api.ClientCommandRegistrationEvent;

@Mod(MineGPT.MOD_ID)
public class MineGPTForge {
    public MineGPTForge() {
        EventBuses.registerModEventBus(MineGPT.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MineGPT.init();
        ClientCommandRegistrationEvent.EVENT.register(ClientCommand::registerCommand);
        ClientCommandRegistrationEvent.EVENT.register(ClientConfigCommand::registerCommand);
    }
}
