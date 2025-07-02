package org.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class EnergyGUIController {

    @FXML private Text communityPoolText;
    @FXML private Text gridPortionText;
    @FXML private Text producedText;
    @FXML private Text usedText;
    @FXML private Text gridUsedText;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> startHourBox;
    @FXML private ComboBox<String> endHourBox;
    @FXML private Text startDisplayText;
    @FXML private Text endDisplayText;
    @FXML private ImageView infoImage;

    private static final String BASE_URL = "http://localhost:8187/energy";

    @FXML
    public void initialize() {
        IntStream.range(0, 24)
                .mapToObj(i -> String.format("%02d:00", i))
                .forEach(h -> {
                    startHourBox.getItems().add(h);
                    endHourBox.getItems().add(h);
                });
        startHourBox.getSelectionModel().select("00:00");
        endHourBox.getSelectionModel().select("23:00");

        startDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateStartDisplay());
        startHourBox.valueProperty().addListener((obs, oldV, newV) -> updateStartDisplay());
        endDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateEndDisplay());
        endHourBox.valueProperty().addListener((obs, oldV, newV) -> updateEndDisplay());

        handleRefresh();
    }

    private void updateStartDisplay() {
        LocalDate date = startDatePicker.getValue();
        String hour = startHourBox.getValue();
        if (date != null && hour != null) {
            startDisplayText.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + hour);
        } else {
            startDisplayText.setText("");
        }
    }

    private void updateEndDisplay() {
        LocalDate date = endDatePicker.getValue();
        String hour = endHourBox.getValue();
        if (date != null && hour != null) {
            endDisplayText.setText(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + hour);
        } else {
            endDisplayText.setText("");
        }
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

                communityPoolText.setText(String.format("%.2f%%", communityDepleted));
                gridPortionText.setText(String.format("%.2f%%", gridPortion));
            } else {
                communityPoolText.setText("N/A");
                gridPortionText.setText("N/A");
            }
        } catch (Exception e) {
            communityPoolText.setText("N/A");
            gridPortionText.setText("N/A");
        }
        // Clear energy data fields
        producedText.setText("");
        usedText.setText("");
        gridUsedText.setText("");
    }

    @FXML
    public void handleShowData() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        String startHour = startHourBox.getValue();
        String endHour = endHourBox.getValue();
        if (start == null || end == null || startHour == null || endHour == null) {
            producedText.setText("Select dates/hours");
            usedText.setText("");
            gridUsedText.setText("");
            return;
        }
        try {
            String startStr = start.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T" + startHour;
            String endStr = end.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T" + endHour;
            String url = String.format("%s/historical?start=%s&end=%s",
                    BASE_URL, startStr, endStr);

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
                // Use gridUsedDisplay if present, else fallback to gridUsed
                grid += entry.has("gridUsedDisplay") ? entry.get("gridUsedDisplay").asDouble() : entry.get("gridUsed").asDouble();
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
}