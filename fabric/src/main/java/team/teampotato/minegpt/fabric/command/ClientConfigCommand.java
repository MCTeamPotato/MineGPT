package team.teampotato.minegpt.fabric.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.teampotato.minegpt.config.Config;
import team.teampotato.minegpt.screen.PingScreen;

@Environment(EnvType.CLIENT)
public class ClientConfigCommand {
    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registry) {
        dispatcher.register(ClientCommandManager.literal("mgpt")
                .then(ClientCommandManager.literal("help")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.1").formatted(Formatting.GREEN));
                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.2").formatted(Formatting.GREEN));
                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.3").formatted(Formatting.GREEN));
                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.4").formatted(Formatting.GREEN));
                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.5").formatted(Formatting.GREEN));
                            context.getSource().sendFeedback(Text.translatable("minegpt.client.command.help.6").formatted(Formatting.GREEN));


                            return 1;
                        }))
                .then(ClientCommandManager.literal("pingtest")
                        .executes(context -> {
                            MinecraftClient.getInstance().execute(() -> {
                                MinecraftClient.getInstance().setScreen(new PingScreen());
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.ping").formatted(Formatting.BLUE));
                            });
                            return 1;
                        }))

                .then(ClientCommandManager.literal("config"))
                .then(ClientCommandManager.literal("reload")
                        .executes(context -> {
                            Config.loadConfig();
                            MinecraftClient.getInstance().execute(() -> {
                                context.getSource().sendFeedback(Text.translatable("minegpt.client.command.reload").formatted(Formatting.BLUE));
                            });
                            return 1;
                        }))

        );
    }
}
