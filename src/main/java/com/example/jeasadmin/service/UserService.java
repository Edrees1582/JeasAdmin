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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        ClassLoader classLoader = JeasAdminApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource("jeas-cfe02-firebase-adminsdk-lcsyq-b7fb4da383.json")).getFile());

        FileInputStream serviceAccount =
                new FileInputStream(file.getAbsoluteFile());

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