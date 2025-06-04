package com.lankaice.project.Validation;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;

public class FirebaseService {
    private DatabaseReference ref;

    public void initialize() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("path-to-service-account.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://your-project-id.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        ref = FirebaseDatabase.getInstance().getReference("vehicles/VEH001");
    }

    public void listenForLocationUpdates(LocationListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                double lat = snapshot.child("latitude").getValue(Double.class);
                double lon = snapshot.child("longitude").getValue(Double.class);
                listener.onLocationUpdate(lat, lon);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    public interface LocationListener {
        void onLocationUpdate(double latitude, double longitude);
    }
}
