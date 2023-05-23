package team.teampotato.chatgpt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import team.teampotato.chatgpt.config.TOMLUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class Utils {
    public static final Logger LOGGER = MineGPT.LOGGER;

    public static String generatePrompt(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "System.out.println(\"" + encodedMessage + "\");\n\npublic class MyFirstProgram {\n    public static void main(String[] args) {\n        // Type your code here.\n    }\n}";
    }

    public static CompletableFuture<String> getChatGPTResponse(String message) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = "User: " + message + "\nChatGPT:";
            JSONObject requestData = new JSONObject()
                    .put("model", TOMLUtils.MODEL)
                    .put("prompt", prompt)
                    .put("max_tokens", Integer.parseInt(TOMLUtils.getMaxTokens()))
                    .put("n", Integer.parseInt(TOMLUtils.getN()))
                    .put("stop", "\n");
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TOMLUtils.getEndpoint()))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + TOMLUtils.getApiKey())
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
