package team.teampotato.MineGPT.Server;



import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.teampotato.MineGPT.Common.Config;


public class ServerCommandConfig implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
       CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

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
            //括号真多


        });
    }
}

