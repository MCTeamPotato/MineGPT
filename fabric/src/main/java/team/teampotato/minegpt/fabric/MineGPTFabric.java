package team.teampotato.minegpt.fabric;

import net.fabricmc.api.ModInitializer;

import team.teampotato.minegpt.MineGPT;

public class MineGPTFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MineGPT.init();
    }
}
