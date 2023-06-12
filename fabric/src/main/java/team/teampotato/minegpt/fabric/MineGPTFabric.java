package team.teampotato.minegpt.fabric;

import team.teampotato.minegpt.MineGPT;
import net.fabricmc.api.ModInitializer;

public class MineGPTFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MineGPT.init();
    }
}
