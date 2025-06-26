package org.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnergyGUIController {

    @FXML private Text communityPoolText;
    @FXML private Text gridPortionText;
    @FXML private Text producedText;
    @FXML private Text usedText;
    @FXML private Text gridUsedText;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ImageView infoImage;

    private static final String BASE_URL = "http://localhost:8187/energy";

    @FXML
    public void initialize() {
        handleRefresh();
    }

    @FXML
    public void handleRefresh() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/current"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());

            if (node != null) {
                double communityDepleted = node.get("communityDepleted").asDouble();
                double gridPortion = node.get("gridPortion").asDouble();

                communityPoolText.setText(String.format("%.2f%% used", communityDepleted));
                gridPortionText.setText(String.format("%.2f%%", gridPortion));
            }

            // Optionally fetch latest hourly usage for kWh values
            updateLatestUsage();

        } catch (Exception e) {
            communityPoolText.setText("N/A");
            gridPortionText.setText("N/A");
        }
    }

    @FXML
    public void handleShowData() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (start == null || end == null) {
            producedText.setText("Select dates");
            usedText.setText("");
            gridUsedText.setText("");
            return;
        }
        try {
            String url = String.format("%s/historical?start=%sT00:00:00&end=%sT23:59:59",
                    BASE_URL,
                    start.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    end.format(DateTimeFormatter.ISO_LOCAL_DATE)
            );
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode arr = mapper.readTree(response.body());

            double produced = 0, used = 0, grid = 0;
            for (JsonNode entry : arr) {
                produced += entry.get("communityProduced").asDouble();
                used += entry.get("communityUsed").asDouble();
                grid += entry.get("gridUsed").asDouble();
            }
            producedText.setText(String.format("%.3f kWh", produced));
            usedText.setText(String.format("%.3f kWh", used));
            gridUsedText.setText(String.format("%.3f kWh", grid));
        } catch (Exception e) {
            producedText.setText("Error");
            usedText.setText("");
            gridUsedText.setText("");
        }
    }

    private void updateLatestUsage() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/historical"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode arr = mapper.readTree(response.body());

            if (arr.isArray() && arr.size() > 0) {
                JsonNode latest = arr.get(arr.size() - 1);
                producedText.setText(String.format("%.3f kWh", latest.get("communityProduced").asDouble()));
                usedText.setText(String.format("%.3f kWh", latest.get("communityUsed").asDouble()));
                gridUsedText.setText(String.format("%.3f kWh", latest.get("gridUsed").asDouble()));
            }
        } catch (Exception e) {
            producedText.setText("N/A");
            usedText.setText("N/A");
            gridUsedText.setText("N/A");
        }
    }
}