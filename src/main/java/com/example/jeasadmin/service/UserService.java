package com.example.jeasadmin.service;

import com.example.jeasadmin.JeasAdminApplication;
import com.example.jeasadmin.model.User;
import com.example.jeasadmin.model.Worker;
import com.google.api.core.ApiFuture;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    public Object getUserDetails(String name, String type) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(type).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        if (document.exists())
            if (type.equals("customers"))
                return document.toObject(User.class);
            else if (type.equals("workers"))
                return document.toObject(Worker.class);
            else return null;
        else return null;
    }

    public void updateUserDetails(Object user, String type) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if (type.equalsIgnoreCase("customers")) {
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(type).document(((User) user).getUid()).set(user);
            collectionsApiFuture.get();
        }
        else if (type.equalsIgnoreCase("workers")) {
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(type).document(((Worker) user).getUid()).set(user);
            collectionsApiFuture.get();
        }
    }

    public List<Object> getAllUsers(String type) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(type).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();

        List<Object> users =new ArrayList<>();

        while(iterator.hasNext()){
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot documentSnapshot = future.get();

            if(documentSnapshot.exists())
                if (type.equals("customers"))
                    users.add(documentSnapshot.toObject(User.class));
                else if (type.equals("workers"))
                    users.add(documentSnapshot.toObject(Worker.class));
        }

        return users;
    }

    public String getPhoto(String id) throws IOException {
        String json = """
                        {
                          "type": "service_account",
                          "project_id": "jeas-cfe02",
                          "private_key_id": "b7fb4da383e44b9d82b7227b1eec1a29b39a701f",
                          "private_key": "-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXvzXxXMWoGEeX\\n9LIIVP702tmrvM0dW1ZEHY4j2s64xAhp9ySwAhEe1YBkXhBihGY2z9NtbIvaixQt\\ndsQs2Hr3lkhbmeMf3GQV1ZuRl1N5T4QSAf3jliG9xZpyMuHu+Qj/5gFhs25ZSt4n\\nctNClDh/2SI2E9TI8DX7jcCf4me5nz+EqFX83VxqAT0w5NpHhkDAqjQPOWO9+Qh+\\n2+2jo0yjMEzgpxwlf9TwqldkOihLn+F/HR2qEVZh9Us/cG4y3aJvhu65+MWy6Bua\\nCZxKzDhdpf3wvFepN8a2RA68bHX0qNCRo8A0MJodQfTz6zW4lMtbzeuw7BrYT4iD\\naR67wyGnAgMBAAECggEAOwxe00MEbvqzsT+4n2FjrKgUl61qK4hFM1BE5VLZqjAu\\nDf3rusmzG8p8oZGw0oUuuRl57lSUvz/rZB2ZDNJYV0uVcH+bT+SU1aI/XRIDm2sD\\nu3Ym846SJqRkCxgdrr4dcGZyFzCZOgnWFIesCXZf3fACTgS9gktm1jI8yHpzXlku\\nE3dDWUws8SrHMMOyx5OtkvXHIh6p0JqZkHHzSOkpFVN2h+RKzWImN/WemukyJxw+\\nYPEAUcU7FfBZ/2TdV9Yfn/PiuXn7RipSIhS7RM0v2SEuOF4Iv16x40aj9ljao+nA\\nRxL8vXzn/qmHTlXb/IQ9jGbF2FoHWhE42V1Ujoe3UQKBgQDJNVUWn1kWuhbTEjaH\\nUlA48nRVCfl4nuZS/RaEN4wrqiQGoYN7F9XCk9SR2P1v348DBlH5hVT8QteOZCmt\\nCbHz045BxNw3iYV3FHFo25kSX1TxZjTtIWey7o4VMnWeJmUJ07U1PxVzEEfQqhDl\\nK/sC5XQ4xzMxx0RAvR0+ZAcvowKBgQDBEdD7DuaGpHRhDLkrjeQV1TQkxL6qD0S4\\nAYuNkm7Dq7n7FdpXdHLDlHg6WIFFrgjSxnT/sj3who7KiAyVoI2P6TZDvbZgbNPa\\nYk+RwpgwT0vbCUBkokeaLMW+JERKIr6GOgjD2bmLR1hnJVJAJIUfkcCHskBPzIQX\\n/FyPnXVWLQKBgGseE4CJsNoik8gm6BS+w6qtmTU0rDWFEsWXq4cUM3GDkM5e6ip8\\nE/cU0JQc2z/V2sMXeEjIrOfnhe5ucKmIgc53EEL/VOXUNtOgdIgIcMORMR2a9g5Q\\nPG7MBaRPSsK5Wg0TQmNohDjPsAvsnjYDZgyyr18S3bRgi3uTsp/mjNTVAoGBAKYY\\nqr3h4sbAonJ/nKnwpuF1iyd6r0DkY80dXy6LZIknqpdtBGsqfjTt/MTNMWF9yI90\\nNmFnuUu8pwlsI89x0XeWPZ3njwfDeDqvSx7t1znwbiTg/6nTxl/H1tKupqVU4K3f\\nLX04SRyeY1pRTEJsaK5xGL5kxlkgbB+qdwMChlh9AoGAC22EtTdIU8jLeNAWgbNz\\nWAaZnEPE6Jhj08+qH/MRt0YAeeT3jXBvZ3gHTvhF8GtX/CW/kKf70nVqMDUsIQ1r\\nG8ALMpRyJMw1b4kp5N6CCkNnHc0IPKYs2JI7ws/lOofVkOvaCMqh1Xf16+IxqHSM\\nIKyHZTOqY8B7wdxSWVI6b84=\\n-----END PRIVATE KEY-----\\n",
                          "client_email": "firebase-adminsdk-lcsyq@jeas-cfe02.iam.gserviceaccount.com",
                          "client_id": "108368817332529355065",
                          "auth_uri": "https://accounts.google.com/o/oauth2/auth",
                          "token_uri": "https://oauth2.googleapis.com/token",
                          "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
                          "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-lcsyq%40jeas-cfe02.iam.gserviceaccount.com",
                          "universe_domain": "googleapis.com"
                        }""";

        InputStream serviceAccount = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        Credentials credentials = GoogleCredentials.fromStream(serviceAccount);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.list("jeas-cfe02.appspot.com").streamAll().forEach(item -> System.out.println(item.getName()));
        Optional<Blob> blob = storage.list("jeas-cfe02.appspot.com").streamAll().filter(item -> !item.getName().equals("logo.jpeg") && item.getName().substring(0, item.getName().indexOf("/")).equals(id)).findFirst();
        return blob.map(value -> "https://firebasestorage.googleapis.com/v0/b/" +
                value.getBucket() +
                "/o/" +
                value.getName().replace("/ID.jpg", "%2FID.jpg") +
                "?alt=media&token=" +
                Objects.requireNonNull(value.getMetadata()).get("firebaseStorageDownloadTokens")).orElse(null);
    }
}