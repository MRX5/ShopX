package com.example.shopx;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.shopx.HomeFragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shopx.Model.Mobile;

public class Repository {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    boolean favourite = false;
    private LifecycleOwner lifecycle;

    public Repository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public Repository(LifecycleOwner lifecycle) {
        this.lifecycle = lifecycle;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Mobile>> getMobiles() {
        MutableLiveData<List<Mobile>> results = new MutableLiveData<>();
        db.collection("Mobiles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Mobile> mobiles = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            inWishlist(document.getId()).observe(lifecycle, s -> {
                                boolean wish = (s.equals("true"));
                                mobiles.add(new Mobile(document.getId(), document.getString("name"), document.getString("price"), wish));
                                results.setValue(mobiles);
                            });
                        }
                    }
                });
        return results;
    }

    public LiveData<String> inWishlist(String productId) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        String userUID = mAuth.getCurrentUser().getUid();
        db.collection("Users")
                .document(userUID)
                .collection("Wishlist")
                .document(productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            liveData.setValue("true");
                        } else {
                            liveData.setValue("false");
                        }
                    }
                });
        return liveData;
    }

    public void addToWishlist(String productId, boolean IsWishListed) {
        String userUID = mAuth.getCurrentUser().getUid();

        if (IsWishListed) {
            db.collection("Users").
                    document(userUID).
                    collection("Wishlist").document(productId).delete();
        } else {
            HashMap<String,Object>mop=new HashMap<>();
            db.collection("Users").
                    document(userUID).
                    collection("Wishlist").document(productId).set(mop);
        }
    }

    private void addInCart(String productId) {

    }
}
