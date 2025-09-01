import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class VoiceAIChatbot {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String API_KEY = System.getenv("GROQ_API_KEY");

    private static Scanner scanner = new Scanner(System.in);
    private static boolean speakResponses = false;

    public static void main(String[] args) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("❌ ERROR: GROQ_API_KEY environment variable is not set!");
            System.err.println("Run this command to set it temporarily:");
            System.err.println("    export GROQ_API_KEY=\"your_api_key_here\n");
            System.exit(1);
        }

        System.out.println("🎤 Voice AI ChatBot Started!");
        System.out.println("---------------------------------------------");
        System.out.println("Commands:");
        System.out.println("  'voice' - Speak your message");
        System.out.println("  'text'  - Type your message");
        System.out.println("  'speak' - Toggle voice responses (current: OFF)");
        System.out.println("  'exit'  - Quit the program");
        System.out.println("---------------------------------------------");

        String systemRole = "You are a helpful and friendly assistant. Keep your answers concise.";

        while (true) {
            System.out.print("\nChoose input mode ('voice', 'text', or 'speak'): ");
            String mode = scanner.nextLine().toLowerCase().trim();

            // Handle special commands
            if (mode.equals("exit")) {
                break;
            } else if (mode.equals("speak")) {
                speakResponses = !speakResponses;
                System.out.println("🗣️  Speech responses: " + (speakResponses ? "ON" : "OFF"));
                continue;
            } else if (!mode.equals("voice") && !mode.equals("text")) {
                System.out.println("❌ Please choose 'voice', 'text', or 'speak'");
                continue;
            }

            String userInput;

            if (mode.equals("voice")) {
                userInput = getVoiceInput();
                if (userInput.equalsIgnoreCase("exit"))
                    break;
            } else {
                System.out.print("You: ");
                userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit"))
                    break;

                // Check if user wants to toggle speech in the middle of text input
                if (userInput.equalsIgnoreCase("speak")) {
                    speakResponses = !speakResponses;
                    System.out.println("🗣️  Speech responses: " + (speakResponses ? "ON" : "OFF"));
                    continue;
                }
            }

            if (userInput.trim().isEmpty()) {
                continue;
            }

            try {
                String aiResponse = getAIResponse(systemRole, userInput);
                System.out.println("Bot: " + aiResponse);

                // Speak the response if enabled
                if (speakResponses) {
                    speakText(aiResponse);
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("👋 Goodbye!");
    }

    private static String getVoiceInput() {
        System.out.println("🎤 Please speak your message now... (say 'exit' to quit)");
        System.out.println("💡 Tip: After speaking, type what you said in the next prompt");

        try {
            // Give user time to "speak"
            Thread.sleep(2000);

            System.out.print("Please type what you said: ");
            String spokenText = scanner.nextLine();

            return spokenText;

        } catch (Exception e) {
            System.out.println("❌ Voice input error: " + e.getMessage());
            System.out.print("Please type your message instead: ");
            return scanner.nextLine();
        }
    }

    private static void speakText(String text) {
        try {
            System.out.print("🗣️  Speaking: ");

            // Limit text length for speech
            String speechText = text;
            if (speechText.length() > 150) {
                speechText = speechText.substring(0, 150) + "...";
                System.out.println(speechText);
            } else {
                System.out.println(speechText);
            }

            // Use Mac's built-in 'say' command
            Process process = Runtime.getRuntime().exec(new String[] { "say", speechText });
            process.waitFor(); // Wait for speech to finish

        } catch (Exception e) {
            System.out.println("❌ Could not speak text: " + e.getMessage());
        }
    }

    private static String getAIResponse(String systemRole, String userInput) throws Exception {
        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "llama-3.1-8b-instant");
        requestJson.put("temperature", 0.7);

        JSONArray messages = new JSONArray();

        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemRole);
        messages.put(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userInput);
        messages.put(userMsg);

        requestJson.put("messages", messages);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestJson.toString()))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API Error: " + response.statusCode() + " - " + response.body());
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray choices = jsonResponse.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        return message.getString("content").trim();
    }
}