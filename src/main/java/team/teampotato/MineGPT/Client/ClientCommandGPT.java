package team.teampotato.MineGPT.Client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.json.JSONObject;
import team.teampotato.MineGPT.Common.Config;
import team.teampotato.MineGPT.MGPT;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;


import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommandGPT implements ClientModInitializer {



    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("chatgpt")
                    .then(argument("message", StringArgumentType.string())
                            .executes(context -> {
                                try {
                                    String message = StringArgumentType.getString(context, "message");
                                    String prompt = generatePrompt(message);
                                    CompletableFuture<String> future = getChatGPTResponse(prompt);
                                    future.thenAcceptAsync(response -> {
                                        String playerName = context.getSource().getPlayer().getName().getString();
                                        assert MinecraftClient.getInstance().player != null;

                                        context.getSource().sendFeedback(Text.literal("[" + playerName + "] -> ").formatted(Formatting.GREEN)
                                                .append(Text.literal(message).formatted(Formatting.AQUA)));

                                        context.getSource().sendFeedback(Text.literal("[ChatGPT-" + Config.MODEL + "] -> " + "[" + playerName + "]" + ": ").formatted(Formatting.GOLD)
                                                .append(Text.literal("\"" + response + "\"").formatted(Formatting.YELLOW)));


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
        return URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
    private CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = "User: " + message + "\nChatGPT:";
            JSONObject requestData = new JSONObject()
                    .put("model", Config.MODEL)
                    .put("prompt", prompt)
                    .put("max_tokens", Integer.parseInt(Config.MAX_TOKENS))
                    .put("n", Integer.parseInt(Config.N))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(Config.ENDPOINT))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + Config.API_KEY)
                        .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray choices = responseJson.getAsJsonArray("choices");
                MGPT.LOGGER.info("Received response from ChatGPT API" + response);

                if (choices == null || choices.size() == 0) {
                    return "Failed to get a response from OpenAI API.";
                }
                JsonArray choices2 = responseJson.getAsJsonArray("choices");
                JsonObject choice = choices2.get(0).getAsJsonObject();
                return choice.get("text").getAsString().replaceAll("\"", "").replaceAll("'", "\"").trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred while processing the request.";
            }
        });
    }

}