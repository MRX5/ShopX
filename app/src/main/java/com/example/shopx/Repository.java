package com.example.shopx;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shopx.Model.Mobile;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

public class Repository {

    private final String USER_PRODUCTS="UserProducts";
    private final String MOBILES="Mobiles";
    private final String USERS="Users";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
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
        db.collection(MOBILES)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Mobile> mobiles = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // get inWishlist and inCart Products
                            getInWish_and_InCart(document.getId()).observe(lifecycle, s -> {
                                boolean wish=false, cart=false;
                                if (s != null) {
                                    try {
                                        wish = (boolean) s.get("IsWish");
                                    } catch (Exception ex) {
                                        wish = false;
                                    }

                                    try {
                                        cart = (boolean) s.get("IsCart");
                                    } catch (Exception ex) {
                                        cart = false;
                                    }
                                }
                                mobiles.add(new Mobile(document.getId(), document.getString("name"), document.getString("price"), wish, cart));
                                results.setValue(mobiles);
                            });
                        }
                    }
                });
        return results;
    }

    public LiveData<DocumentSnapshot> getInWish_and_InCart(String productId) {
        MutableLiveData<DocumentSnapshot> liveData = new MutableLiveData<>();String userUID = mAuth.getCurrentUser().getUid();
        db.collection(USERS)
                .document(userUID)
                .collection(USER_PRODUCTS)
                .document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        liveData.setValue(documentSnapshot);
                    } else {
                        liveData.setValue(null);
                    }

                });
        return liveData;
    }

    public void addToUserList(String productId, boolean IsWishListed, boolean IsInCart) {
        String userUID = mAuth.getCurrentUser().getUid();

        Map<String, Boolean> value = new HashMap<>();
        value.put("IsWish", IsWishListed);
        value.put("IsCart", IsInCart);

        db.collection(USERS).
                document(userUID).
                collection(USER_PRODUCTS).document(productId).set(value);

    }

    public LiveData<List<String>> getWishlist()
    {
        MutableLiveData<List<String>> results=new MutableLiveData<>();
        String curr_user_id=mAuth.getCurrentUser().getUid();
        db.collection(USERS)
                .document(curr_user_id)
                .collection(USER_PRODUCTS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> tmp=new ArrayList<>();
                        for (DocumentSnapshot document:queryDocumentSnapshots.getDocuments())
                        {
                            tmp.add(document.getId());
                        }
                        results.setValue(tmp);
                    }
                });
        return results;
    }

}
