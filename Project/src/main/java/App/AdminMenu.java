package App;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import util.DBConnector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class AdminMenu {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1373805131455266957/7znW9OA1SipNaoqA8wwiS40j-WjRNKFr36Y4XIp2pkYxvNwBIYpL0_Bv-IKtGGsZWVhD";

    private static final int COLOR_SUCCESS = 0x2ECC71; // green
    private static final int COLOR_ERROR   = 0xE74C3C; // red
    private static final int COLOR_INFO    = 0x3498DB; // blue
    private static final int COLOR_WARN    = 0xF1C40F; // yellow

    private final DBConnector db;
    private final String adminUsername;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public AdminMenu(DBConnector db, String adminUsername) {
        this.db = db;
        this.adminUsername = adminUsername;
    }

    public boolean banUser(String username) {
        if (!db.userExists(username)) {
            showAlert("User not found", "The user " + username + " does not exist.", Alert.AlertType.ERROR);
            sendWebhook("Ban Failed", username + " does not exist.", COLOR_ERROR);
            return false;
        }

        Optional<ButtonType> result = showConfirmation("Ban User",
                "Are you sure you want to ban " + username + "?");

        if (result.filter(btn -> btn == ButtonType.OK).isPresent()) {
            boolean success = db.executeUpdate("UPDATE users SET banned = 'Yes' WHERE username = '" + username + "'");
            if (success) {
                showAlert("User Banned", username + " has been banned.", Alert.AlertType.INFORMATION);
                sendWebhook("User Banned", "**" + username + "** was banned by **" + adminUsername + "**.", COLOR_ERROR);
            } else {
                showAlert("Error", "Failed to ban user.", Alert.AlertType.ERROR);
                sendWebhook("Ban Failed", "Failed to ban **" + username + "**.", COLOR_ERROR);
            }
            return success;
        }
        return false;
    }

    public boolean unbanUser(String username) {
        if (!db.userExists(username)) {
            showAlert("User not found", "The user " + username + " does not exist.", Alert.AlertType.ERROR);
            sendWebhook("Unban Failed", username + " does not exist.", COLOR_ERROR);
            return false;
        }

        Optional<ButtonType> result = showConfirmation("Unban User",
                "Are you sure you want to unban " + username + "?");

        if (result.filter(btn -> btn == ButtonType.OK).isPresent()) {
            boolean success = db.executeUpdate("UPDATE users SET banned = 'No' WHERE username = '" + username + "'");
            if (success) {
                showAlert("User Unbanned", username + " has been unbanned.", Alert.AlertType.INFORMATION);
                sendWebhook("User Unbanned", "**" + username + "** was unbanned by **" + adminUsername + "**.", COLOR_SUCCESS);
            } else {
                showAlert("Error", "Failed to unban user.", Alert.AlertType.ERROR);
                sendWebhook("Unban Failed", "Failed to unban **" + username + "**.", COLOR_ERROR);
            }
            return success;
        }
        return false;
    }

    public boolean changeUserRole(String username, String newRole) {
        if (!db.userExists(username)) {
            showAlert("User not found", "The user " + username + " does not exist.", Alert.AlertType.ERROR);
            sendWebhook("Role Change Failed", username + " does not exist.", COLOR_ERROR);
            return false;
        }

        Optional<ButtonType> result = showConfirmation("Change Role",
                "Are you sure you want to change " + username + "'s role to " + newRole + "?");

        if (result.filter(btn -> btn == ButtonType.OK).isPresent()) {
            boolean success = db.changeRole(username, newRole);
            if (success) {
                showAlert("Role Changed", username + "'s role has been changed to " + newRole + ".", Alert.AlertType.INFORMATION);
                sendWebhook("Role Changed", "**" + username + "** is now **" + newRole + "** (by **" + adminUsername + "**).", COLOR_INFO);
            } else {
                showAlert("Error", "Failed to change user's role.", Alert.AlertType.ERROR);
                sendWebhook("Role Change Failed", "Failed to set **" + username + "** to **" + newRole + "**.", COLOR_ERROR);
            }
            return success;
        }
        return false;
    }

    public boolean resetProfilePicture(String username) {
        if (!db.userExists(username)) {
            showAlert("User not found", "The user " + username + " does not exist.", Alert.AlertType.ERROR);
            sendWebhook("Reset Picture Failed", username + " does not exist.", COLOR_ERROR);
            return false;
        }

        Optional<ButtonType> result = showConfirmation("Reset Profile Picture",
                "Are you sure you want to reset " + username + "'s profile picture?");

        if (result.filter(btn -> btn == ButtonType.OK).isPresent()) {
            boolean success = db.changeProfilePicture(username, "person1.png");
            if (success) {
                showAlert("Profile Picture Reset", username + "'s profile picture has been reset.", Alert.AlertType.INFORMATION);
                sendWebhook("Profile Picture Reset", "**" + username + "**'s avatar was reset by **" + adminUsername + "**.", COLOR_WARN);
            } else {
                showAlert("Error", "Failed to reset profile picture.", Alert.AlertType.ERROR);
                sendWebhook("Reset Picture Failed", "Failed to reset avatar for **" + username + "**.", COLOR_ERROR);
            }
            return success;
        }
        return false;
    }


    private void sendWebhook(String title, String description, int color) {
        try {
            String json = "{" +
                    "\"embeds\":[{" +
                    "\"title\":\"" + escapeJson(title) + "\"," +
                    "\"description\":\"" + escapeJson(description) + "\"," +
                    "\"color\":" + color + "}]" +
                    "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ignored) {

        }
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
