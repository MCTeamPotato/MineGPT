package team.teampotato.chatgpt;


import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static team.teampotato.chatgpt.TomlUtils.readTomlFromFile;

public class Command implements ModInitializer {

    private static final String CONFIG = "config/MineGPTconfig.toml";
    private Toml toml;
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        try {
            toml = readOrCreateConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to load config file: {}", e.getMessage());
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<ServerCommandSource> mgptCommand = CommandManager.literal("mgpt")
                    .requires(source -> source.hasPermissionLevel(2));
            LiteralArgumentBuilder<ServerCommandSource> tokenCommand = CommandManager.literal("token")
                    .then(CommandManager.argument("token", StringArgumentType.string())
                            .executes(context -> {
                                String apiKey = StringArgumentType.getString(context, "token");
                                if (isValidApiKey(apiKey)) {
                                    // 保存 API Key 到配置文件
                                    Toml toml = readTomlFromFile(CONFIG);
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
                        Toml toml = TomlUtils.readTomlFromFile(CONFIG);
                        Map<String, Object> tomlMap = toml.toMap();
                        tomlMap.put("endpoint", "https://api.openai.com/v1/completions");
                        tomlMap.put("model", "text-davinci-003");
                        tomlMap.put("max_tokens", "1024");
                        tomlMap.put("n", "1");
                        TomlWriter writer = new TomlWriter();
                        try {
                            writer.write(toml, new File(CONFIG));
                            context.getSource().sendFeedback(new TranslatableText("minegpt.set.default", CONFIG), true);
                        } catch (IOException e) {
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
                        TomlUtils.reloadConfig();
                        context.getSource().sendFeedback(new TranslatableText("minegpt.reload.config"), true);
                        return 1;
                    }));
/*
            mgptCommand.then(CommandManager.literal("langtest")
                    .executes(context -> {
                        context.getSource().sendError(new TranslatableText("minegpt.error.apikey"));
                        context.getSource().sendFeedback(new TranslatableText("minegpt.set.default"), true);
                        context.getSource().sendFeedback(new TranslatableText("minegpt.error.saveconfig", CONFIG), true);
                        context.getSource().sendFeedback(new TranslatableText("minegpt.set.configapikey", CONFIG), true);
                        context.getSource().sendFeedback(new TranslatableText("minegpt.reload.config"), true);
                        return 1;
                    }));
 */

            dispatcher.register(mgptCommand);

        });
    }

    private Toml readOrCreateConfig() throws IOException {
        Path configFile = Paths.get(CONFIG);
        if (!Files.exists(configFile)) {
            Files.createDirectories(configFile.getParent());
            Files.createFile(configFile);
            Toml toml = new Toml();
            TomlWriter writer = new TomlWriter();
            writer.write(toml, new File(CONFIG));
        }
        return new Toml().read(new File(CONFIG));
    }
    private static boolean isValidApiKey(String apiKey) {
        if (!apiKey.startsWith("sk-")) {
            return false;
        }
        return true;
    }

}
