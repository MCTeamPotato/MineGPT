package team.teampotato.chatgpt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.registry.DynamicRegistryManager.LOGGER;
import static team.teampotato.chatgpt.TomlUtils.MODEL;

public class ChatGPT implements ModInitializer {



    @Override
    public void onInitialize() {
        Toml toml = TomlUtils.readTomlFromFile("config/MineGPTconfig.toml");
        TomlUtils.ENDPOINT = toml.getString("endpoint");
        TomlUtils.API_KEY = toml.getString("api_key");
        MODEL = toml.getString("model");
        TomlUtils.MAX_TOKENS = toml.getString("max_tokens");
        TomlUtils.N = toml.getString("n");


        // 注册指令
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("chatgpt")
                    .then(CommandManager.argument("message", StringArgumentType.greedyString())
                            .executes(context -> {
                                try {
                                    String message = StringArgumentType.getString(context, "message");
                                    String prompt = generatePrompt(message);
                                    CompletableFuture<String> future = getChatGPTResponse(prompt);
                                    future.thenAcceptAsync(response -> {
                                        String playerName = context.getSource().getName();

                                        context.getSource().sendFeedback(new LiteralText("[" + playerName + "] -> ").formatted(Formatting.GREEN)
                                                .append(new LiteralText(message).formatted(Formatting.AQUA)), false);
                                        context.getSource().sendFeedback(new LiteralText("[ChatGPT-" + MODEL + "] -> " + "[" + playerName + "]" +": ").formatted(Formatting.GOLD)
                                                .append(new LiteralText(response).formatted(Formatting.YELLOW)), false);

                                    });
                                    return 1;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            })
                    )
            );
        });

    }
    private String generatePrompt(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n\n    }\n}";
    }

    private CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = "User: " + message + "\nChatGPT:";
            JSONObject requestData = new JSONObject()
                    .put("model", MODEL)
                    .put("prompt", prompt)
                    .put("max_tokens", Integer.parseInt(TomlUtils.MAX_TOKENS))
                    .put("n", Integer.parseInt(TomlUtils.N))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TomlUtils.ENDPOINT))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + TomlUtils.API_KEY)
                        .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray choices = responseJson.getAsJsonArray("choices");
                LOGGER.info("Received response from ChatGPT API: {}", response);

                if (choices == null || choices.size() == 0) {
                    return "Failed to get a response from OpenAI API.";
                }
                JsonObject choice = choices.get(0).getAsJsonObject();
                return choice.get("text").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred while processing the request.";
            }
        });
    }

}
