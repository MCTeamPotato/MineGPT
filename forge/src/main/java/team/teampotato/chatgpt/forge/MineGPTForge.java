package team.teampotato.chatgpt.forge;

import dev.architectury.platform.forge.EventBuses;
import team.teampotato.chatgpt.MineGPT;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineGPT.MOD_ID)
public class MineGPTForge {
    public MineGPTForge() {
        EventBuses.registerModEventBus(MineGPT.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MineGPT.init();
    }
}
