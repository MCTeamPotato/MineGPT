package team.teampotato.MineGPT.Client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.teampotato.MineGPT.Client.GUI.PingGUI;
import team.teampotato.MineGPT.Common.Config;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommandConfig implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(literal("mgpt")
                    .then(literal("help")
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.1").formatted(Formatting.GREEN));
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.2").formatted(Formatting.GREEN));
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.3").formatted(Formatting.GREEN));
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.4").formatted(Formatting.GREEN));
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.5").formatted(Formatting.GREEN));
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.6").formatted(Formatting.GREEN));


                                return 1;
                            }))
                    .then(literal("pingtest")
                            .executes(context -> {
                                MinecraftClient.getInstance().execute(() -> {
                                    MinecraftClient.getInstance().setScreen(new PingGUI.GUI());
                                    context.getSource().sendFeedback(Text.translatable("minegpt.client.command.ping").formatted(Formatting.BLUE));
                                });
                                return 1;
                            }))

                    .then(literal("config"))
                            .then(literal("reload")
                                    .executes(context -> {
                                        Config.loadConfig();
                                        MinecraftClient.getInstance().execute(() -> {
                                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.reload").formatted(Formatting.BLUE));
                                        });
                                        return 1;
                                    }))

            );
            //括号真多


        });
    }
}
