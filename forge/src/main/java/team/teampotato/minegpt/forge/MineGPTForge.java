package team.teampotato.minegpt.forge;

import dev.architectury.platform.forge.EventBuses;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import team.teampotato.minegpt.MineGPT;

@Mod(MineGPT.MOD_ID)
public class MineGPTForge {
    public MineGPTForge() {
        IEventBus modEventBus = EventBuses.getModEventBus(MineGPT.MOD_ID).get();
        EventBuses.registerModEventBus(MineGPT.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        modEventBus.addListener(MineGPTForge::onInitialize);
        modEventBus.addListener(MineGPTClientForge::onInitializeClient);
    }

    public static void onInitialize(FMLCommonSetupEvent event) {
        MineGPT.init();
    }
}
