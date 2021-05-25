package com.example.shopx;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shopx.Model.Wishlist;
import com.example.shopx.Model.WishlistResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shopx.Model.Mobile;

public class Repository {

    private final String USER_PRODUCTS="UserProducts";
    private final String MOBILES="Mobiles";
    private final String USERS="Users";
    private final String IN_WISHLIST="InWish";
    private final String IN_CART="InCart";
    private final String CATEGORY="category";

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
                                        wish = (boolean) s.get(IN_WISHLIST);
                                    } catch (Exception ex) {
                                        wish = false;
                                    }

                                    try {
                                        cart = (boolean) s.get(IN_CART);
                                    } catch (Exception ex) {
                                        cart = false;
                                    }
                                }
                                mobiles.add(new Mobile(document.getId(), document.getString("name"), document.getString("price"), document.getString("category"), wish, cart));
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

    public void addToUserProducts(String productId, boolean IsWishListed, boolean IsInCart,String category) {
        String userUID = mAuth.getCurrentUser().getUid();

        Map<String, Object> value = new HashMap<>();
        value.put(IN_WISHLIST, IsWishListed);
        value.put(IN_CART, IsInCart);
        value.put(CATEGORY,category);

        db.collection(USERS).
                document(userUID).
                collection(USER_PRODUCTS).document(productId).set(value);

    }

    public LiveData<List<WishlistResponse>> getWishlist() {
        MutableLiveData<List<WishlistResponse>> results=new MutableLiveData<>();
        String curr_user_id=mAuth.getCurrentUser().getUid();
        db.collection(USERS)
                .document(curr_user_id)
                .collection(USER_PRODUCTS)
                .whereEqualTo(IN_WISHLIST,true)
                .get()
                .addOnCompleteListener(task -> {
                    List<WishlistResponse> response=new ArrayList<>();
                    for (DocumentSnapshot document:task.getResult())
                    {
                        response.add(new WishlistResponse(document.getId(),document.getString("category")));
                    }
                    results.setValue(response);
                });
        return results;
    }

    public LiveData<Wishlist> getProduct(String category,String productId) {

        MutableLiveData<Wishlist>product=new MutableLiveData<>();

        db.collection(category)
                .document(productId)
                .get()
                .addOnCompleteListener(task -> {
                    Wishlist wishlist=task.getResult().toObject(Wishlist.class);
                    wishlist.setProductId(task.getResult().getId());
                    product.setValue(wishlist);
                });
        return product;
    }

    public void removeFormWishlist(Wishlist product)
    {
        String curr_user_id=mAuth.getCurrentUser().getUid();
        HashMap<String,Object>data=new HashMap<>();
        data.put(IN_WISHLIST,false);
        db.collection(USERS)
                .document(curr_user_id)
                .collection(USER_PRODUCTS)
                .document(product.getProductId())
                .update(data);
    }
}
