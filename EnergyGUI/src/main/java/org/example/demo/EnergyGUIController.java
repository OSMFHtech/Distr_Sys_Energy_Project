package org.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class EnergyGUIController {

    @FXML
    private TextField communityPoolField;
    @FXML
    private TextField gridPortionField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField producedField;
    @FXML
    private TextField usedField;
    @FXML
    private TextField gridUsedField;
    @FXML
    private ImageView infoImage;

    // Ajout des nouveaux champs pour les balises <Text>
    @FXML
    private Text producedText;
    @FXML
    private Text usedText;
    @FXML
    private Text gridUsedText;

    private static final String BASE_URL = "http://localhost:8080/energy";

    // Explicit default constructor
    public EnergyGUIController() {
        // Initialization if necessary
    }

    // Nouvelle méthode pour mettre à jour les balises <Text>
    @FXML
    public void updateDataFromAPI() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/current"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            // Extract the latest data (assuming it's the first object in the array)
            JsonNode latestData = jsonNode.get(0);

            // Update the Text elements
            producedText.setText(latestData.get("produced").asText() + " kWh");
            usedText.setText(latestData.get("used").asText() + " kWh");
            gridUsedText.setText(latestData.get("gridUsed").asText() + " kWh");
        } catch (Exception e) {
            System.err.println("Error fetching data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // existing method to handle refresh button
    public void handleRefresh() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/current"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Current Data: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching current data: " + e.getMessage());
        }
    }

    @FXML
    public void handleShowData() {
        try {
            // select calender start & end
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();
            if (start == null || end == null) {
                System.err.println("Please select a start and end date");
                return;
            }

            // URL
            String url = String.format("%s/current?start=%sT00:00:00&end=%sT23:59:59",
                    BASE_URL, start, end);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            //  JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);

            // getting recent data
            if (jsonNode.isArray() && jsonNode.size() > 0) {
                JsonNode latestData = jsonNode.get(0);

                // update <Text>
                producedText.setText(latestData.get("produced").asText() + " kWh");
                usedText.setText(latestData.get("used").asText() + " kWh");
                gridUsedText.setText(latestData.get("gridUsed").asText() + " kWh");
            } else {
                System.err.println("No data available for the selected period.");
            }
        } catch (Exception e) {
            System.err.println("Error during data recovery: " + e.getMessage());
            e.printStackTrace();
        }
    }
}