package com.lankaice.project.controller;

import com.lankaice.project.Validation.FirebaseService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class VehicleTrackerController implements Initializable {

    @FXML
    private AnchorPane ancVehicalTracker;

    @FXML
    public Label lblLat, lblLon;

    public void setLocation(double lat, double lon) {
        lblLat.setText(String.valueOf(lat));
        lblLon.setText(String.valueOf(lon));
    }


    @FXML
    private WebView webView;

    private FirebaseService firebaseService = new FirebaseService();

  /*  @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        *//*
        try {
            firebaseService.initialize();
            firebaseService.listenForLocationUpdates((lat, lon) -> {
                Platform.runLater(() -> {
                    lblLat.setText("Latitude: " + lat);
                    lblLon.setText("Longitude: " + lon);
                    // update map here if needed
                    webView.getEngine().load("https://maps.google.com/maps?q=" + lat + "," + lon + "&z=15&output=embed");
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        *//*

        // For testing static coordinates:
        double lat = 6.9271;
        double lon = 79.8612;

        lblLat.setText(String.valueOf(lat));
        lblLon.setText(String.valueOf(lon));

        webView.getEngine().load("https://maps.google.com/maps?q=" + lat + "," + lon + "&z=15&output=embed");
    }*/
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    /*  try {
          firebaseService.listenForLocationUpdates((lat, lon) -> {
              Platform.runLater(() -> {
                  lblLat.setText("Latitude: " + lat);
                  lblLon.setText("Longitude: " + lon);

                  // WebView Map Update
                  String jsCommand = String.format("updateMarker(%f, %f);", lat, lon);
                  webView.getEngine().executeScript(jsCommand);
              });
          });

      } catch (Exception e) {
          e.printStackTrace();
      }
*/
    /*  double lat = 6.0535;
      double lon = 80.2170;
*/
     /* lblLat.setText(String.valueOf(lat));
      lblLon.setText(String.valueOf(lon));*/


      String html = """
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Simple OSM Map</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css" />
  <style> html, body, #map { height: 100%; margin: 0; padding: 0; } </style>
</head>
<body>
  <div id="map"></div>
  <script src="https://unpkg.com/leaflet@1.9.3/dist/leaflet.js"></script>
  <script>
    var map = L.map('map').setView([6.9271, 79.8612], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors'
    }).addTo(map);
    var marker = L.marker([6.9271, 79.8612]).addTo(map);
  </script>
</body>
</html>
""";

      webView.getEngine().loadContent(html);


  }

}
