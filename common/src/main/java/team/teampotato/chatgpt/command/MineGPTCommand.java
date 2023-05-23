package team.teampotato.chatgpt.command;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import team.teampotato.chatgpt.MineGPT;
import team.teampotato.chatgpt.config.TOMLUtils;
import team.teampotato.chatgpt.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MineGPTCommand {
    public MineGPTCommand() {
        TOMLUtils.loadConfig();
    }

    private static final String CONFIG = MineGPT.CONFIG;
    private static final Logger LOGGER = MineGPT.LOGGER;

    public static void registerGPTCommand(CommandDispatcher<ServerCommandSource> commandDispatcher, CommandManager.RegistrationEnvironment commandDedicated) {
        commandDispatcher.register(CommandManager.literal("chatgpt")
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            //context.getSource().sendFeedback(new LiteralText("TestChat-1").formatted(Formatting.GRAY), false);
                            try {
                                String message = StringArgumentType.getString(context, "message");
                                String prompt = Utils.generatePrompt(message);
                                CompletableFuture<String> future = Utils.getChatGPTResponse(prompt);
                                future.thenAcceptAsync(response -> {
                                    String playerName = context.getSource().getName();
                                    //context.getSource().sendFeedback(new LiteralText("TestChat-2").formatted(Formatting.GRAY), false);


                                    context.getSource().sendFeedback(new LiteralText("[" + playerName + "] -> ").formatted(Formatting.GREEN)
                                            .append(new LiteralText(message).formatted(Formatting.AQUA)), false);
                                        /*context.getSource().sendFeedback(new LiteralText("[ChatGPT-" + MODEL + "] -> " + "[" + playerName + "]" + ": ").formatted(Formatting.GOLD)
                                                .append(new LiteralText(response).formatted(Formatting.YELLOW)), false);

                                         */
                                    context.getSource().sendFeedback(new LiteralText("[ChatGPT-" + TOMLUtils.MODEL + "] -> " + "[" + playerName + "]" + ": ").formatted(Formatting.GOLD)
                                            .append(new LiteralText("\"" + response + "\"").formatted(Formatting.YELLOW)), false);


                                });
                                return 1;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return 0;
                            }
                        })
                )
        );
    }
    public static void registerModCommand(CommandDispatcher<ServerCommandSource> commandDispatcher, CommandManager.RegistrationEnvironment commandDedicated) {
        LiteralArgumentBuilder<ServerCommandSource> mgptCommand = CommandManager.literal("mgpt")
                .requires(source -> source.hasPermissionLevel(2));
        LiteralArgumentBuilder<ServerCommandSource> tokenCommand = CommandManager.literal("token")
                .then(CommandManager.argument("token", StringArgumentType.string())
                        .executes(context -> {
                            String apiKey = StringArgumentType.getString(context, "token");
                            if (Command.isValidApiKey(apiKey)) {
                                // 保存 API Key 到配置文件
                                Toml toml = TOMLUtils.readTomlFromFile(CONFIG);
                                Map<String, Object> tomlMap = toml.toMap();
                                tomlMap.put("api_key", apiKey);
                                TomlWriter writer = new TomlWriter();
                                try {
                                    writer.write(tomlMap, new File(CONFIG));
                                    context.getSource().sendFeedback(new TranslatableText("minegpt.set.configapikey", CONFIG), true);
                                } catch (IOException e) {
                                    context.getSource().sendFeedback(new TranslatableText("minegpt.error.saveconfig", CONFIG), true);
                                    LOGGER.error("Failed to save config file: {}", e.getMessage());
                                }
                            } else {
                                context.getSource().sendError(new TranslatableText("minegpt.error.apikey"));
                            }

                            return 1;
                        })
                );

        // 添加默认命令
        mgptCommand.then(CommandManager.literal("default")
                .executes(context -> {
                    Toml toml = TOMLUtils.readTomlFromFile(CONFIG);
                    Map<String, Object> tomlMap = toml.toMap();
                    tomlMap.put("endpoint", "https://api.openai.com/v1/completions");
                    tomlMap.put("model", "text-davinci-003");
                    tomlMap.put("api_key", "api_key");
                    tomlMap.put("max_tokens", "1024");
                    tomlMap.put("n", "1");
                    TomlWriter writer = new TomlWriter();
                    try {
                        // 写入配置文件
                        TOMLUtils.writeTomlToFile(tomlMap, CONFIG);
                        context.getSource().sendFeedback(new TranslatableText("minegpt.set.default", CONFIG), true);
                    } catch (IOException e) {
                        // 捕获并处理异常
                        context.getSource().sendError(new TranslatableText("minegpt.error.saveconfig"));
                        LOGGER.error("Failed to save config file: {}", e.getMessage());
                    }
                    return 1;
                })
        );


        mgptCommand.then(tokenCommand);
        //重载指令
        mgptCommand.then(CommandManager.literal("reload")
                .executes(context -> {
                    TOMLUtils.reloadConfig();
                    context.getSource().sendFeedback(new TranslatableText("minegpt.reload.config"), true);
                    return 1;
                }));

        commandDispatcher.register(mgptCommand);
    }
}
