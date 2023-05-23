package team.teampotato.chatgpt.fabric;

import team.teampotato.chatgpt.MineGPT;
import net.fabricmc.api.ModInitializer;

public class MineGPTFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MineGPT.init();
    }
}
