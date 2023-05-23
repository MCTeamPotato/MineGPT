package team.teampotato.chatgpt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static team.teampotato.chatgpt.TomlUtils.MODEL;

public class ChatGPT implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {


        // Register the "/chatgpt" command
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("chatgpt")
                    .then(CommandManager.argument("message", StringArgumentType.greedyString())
                            .executes(context -> {
                                //context.getSource().sendFeedback(new LiteralText("TestChat-1").formatted(Formatting.GRAY), false);
                                try {
                                    String message = StringArgumentType.getString(context, "message");
                                    String prompt = generatePrompt(message);
                                    CompletableFuture<String> future = getChatGPTResponse(prompt);
                                    future.thenAcceptAsync(response -> {
                                        String playerName = context.getSource().getName();
                                        //context.getSource().sendFeedback(new LiteralText("TestChat-2").formatted(Formatting.GRAY), false);


                                        context.getSource().sendFeedback(new LiteralText("[" + playerName + "] -> ").formatted(Formatting.GREEN)
                                                .append(new LiteralText(message).formatted(Formatting.AQUA)), false);
                                        /*context.getSource().sendFeedback(new LiteralText("[ChatGPT-" + MODEL + "] -> " + "[" + playerName + "]" + ": ").formatted(Formatting.GOLD)
                                                .append(new LiteralText(response).formatted(Formatting.YELLOW)), false);

                                         */
                                        context.getSource().sendFeedback(new LiteralText("[ChatGPT-" + MODEL + "] -> " + "[" + playerName + "]" + ": ").formatted(Formatting.GOLD)
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
        });


    }


    private String generatePrompt(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n        // Type your code here.\n    }\n}";
    }

    private CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = "User: " + message + "\nChatGPT:";
            JSONObject requestData = new JSONObject()
                    .put("model", MODEL)
                    .put("prompt", prompt)
                    .put("max_tokens", Integer.parseInt(TomlUtils.getMaxTokens()))
                    .put("n", Integer.parseInt(TomlUtils.getN()))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TomlUtils.getEndpoint()))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + TomlUtils.getApiKey())
                        .POST(HttpRequest.BodyPublishers.ofString(requestData.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray choices = responseJson.getAsJsonArray("choices");
                LOGGER.info("Received response from ChatGPT API" + response);

                if (choices == null || choices.size() == 0) {
                    return "Failed to get a response from OpenAI API.";
                }
                JsonArray choices2 = responseJson.getAsJsonArray("choices");
                JsonObject choice = choices2.get(0).getAsJsonObject();
                String cleanedText = choice.get("text").getAsString().replaceAll("\"", "").replaceAll("'", "\"").trim();
                return cleanedText;
            } catch (Exception e) {
                e.printStackTrace();
                return "An error occurred while processing the request.";
            }
        });
    }


}
