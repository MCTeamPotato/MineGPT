package team.teampotato.minegpt.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.teampotato.minegpt.config.Config;

public class ServerConfigCommand {
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry, CommandManager.RegistrationEnvironment selection) {
        dispatcher.register(CommandManager.literal("mgpt")
                .then(CommandManager.literal("help")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.translatable("minegpt.server.command.help.1").formatted(Formatting.GREEN),false);
                            context.getSource().sendFeedback(Text.translatable("minegpt.server.command.help.2").formatted(Formatting.GREEN),false);
                            context.getSource().sendFeedback(Text.translatable("minegpt.server.command.help.3").formatted(Formatting.GREEN),false);
                            context.getSource().sendFeedback(Text.translatable("minegpt.server.command.help.4").formatted(Formatting.GREEN),false);
                            context.getSource().sendFeedback(Text.translatable("minegpt.server.command.help.5").formatted(Formatting.GREEN),false);
                            context.getSource().sendFeedback(Text.translatable("minegpt.server.command.help.6").formatted(Formatting.GREEN),false);


                            return 1;
                        }))

                .then(CommandManager.literal("config"))
                .then(CommandManager.literal("reload")
                        .executes(context -> {
                            Config.loadConfig();
                            MinecraftClient.getInstance().execute(() -> {
                                context.getSource().sendFeedback(Text.translatable("minegpt.server.command.reload").formatted(Formatting.BLUE),false);
                            });
                            return 1;
                        }))

        );
    }
}
