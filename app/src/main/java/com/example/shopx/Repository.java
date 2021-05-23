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
                            getUserData(document.getId()).observe(lifecycle, s -> {
                                boolean wish, cart;
                                if (s == null) {
                                    wish = false;
                                    cart = false;
                                } else {
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


/*
    public LiveData<List<Mobile>> RefreshMobiles(List<Mobile> mobiles) {
        for (Mobile mobile : mobiles) {
            Log.d("Before", "" + mobile.getId() + ": " + mobile.isInCart() + " " + mobile.isInWishlist());
        }
        for ( int i = 0; i < mobiles.size(); i++) {
            Mobile mobile = mobiles.get(i);
            getUserData(mobile.getId()).observe(lifecycle, s -> {
                boolean wish, cart;
                if (s == null) {
                    wish = false;
                    cart = false;
                } else {
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

                Log.d("Inside", "Cart1 : " + cart + ",  wish1 :" + wish);

                mobile.setInCart(cart);
                mobile.setInWishlist(wish);
                Log.d("Inside", "Cart2: " + mobile.isInCart() + ",  wish2: " + mobile.isInWishlist());
            });

            mobiles.get(i).setInCart(mobile.isInCart());
            mobiles.get(i).setInWishlist(mobile.isInWishlist());
        }

        for (Mobile mobile : mobiles) {
            Log.d("After", "" + mobile.getId() + ": " + mobile.isInCart() + " " + mobile.isInWishlist());
        }

        return new LiveData<List<Mobile>>() {
            @Override
            protected void setValue(List<Mobile> value) {
                super.setValue(mobiles);
            }
        };
    }
*/


    public LiveData<DocumentSnapshot> getUserData(String productId) {
        MutableLiveData<DocumentSnapshot> liveData = new MutableLiveData<>();
        String userUID = mAuth.getCurrentUser().getUid();
        db.collection("Users")
                .document(userUID)
                .collection("UserList")
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

        db.collection("Users").
                document(userUID).
                collection("UserList").document(productId).set(value);

    }

}
