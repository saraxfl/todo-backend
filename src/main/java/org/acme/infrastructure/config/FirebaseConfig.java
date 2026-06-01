package org.acme.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import io.quarkus.runtime.Startup;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Singleton
@Startup
public class FirebaseConfig {

    @PostConstruct
    void init() {
        try {

            if (FirebaseApp.getApps().isEmpty()) {

                String json = System.getenv(
                        "FIREBASE_SERVICE_ACCOUNT_JSON"
                );

                if (json == null || json.isBlank()) {
                    throw new RuntimeException(
                            "Falta FIREBASE_SERVICE_ACCOUNT_JSON"
                    );
                }

                InputStream serviceAccount =
                        new ByteArrayInputStream(
                                json.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );

                FirebaseOptions options =
                        FirebaseOptions.builder()
                        .setCredentials(
                                GoogleCredentials.fromStream(
                                        serviceAccount
                                )
                        )
                        .build();

                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error inicializando Firebase",
                    e
            );
        }
    }
}