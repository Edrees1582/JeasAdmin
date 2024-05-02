package com.example.jeasadmin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class JeasAdminApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JeasAdminApplication.class, args);

        ClassLoader classLoader = JeasAdminApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource("jeas-cfe02-firebase-adminsdk-lcsyq-b7fb4da383.json")).getFile());

        FileInputStream serviceAccount =
                new FileInputStream(file.getAbsoluteFile());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

}
