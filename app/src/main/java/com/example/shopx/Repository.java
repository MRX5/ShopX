package com.example.shopx;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shopx.Model.ProductInfo;
import com.example.shopx.Model.UserResponse;
import com.example.shopx.Model.myResponse;
import com.example.shopx.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shopx.Model.Product;

public class Repository {

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

    public LiveData<List<ProductInfo>> getProducts(String category) {
        MutableLiveData<List<ProductInfo>> results = new MutableLiveData<>();
        db.collection(Constants.CATEGORIES)
                .document(category)
                .collection(Constants.PRODUCTS)
                .addSnapshotListener((value, error) -> {
                        List<ProductInfo> products = new ArrayList<>();
                        for (DocumentSnapshot document : value) {

                            getInWish_and_InCart(document.getId()).observe(lifecycle, response -> {
                                boolean wish = response.isInWish();
                                boolean cart = response.isInCart();
                                ProductInfo product = document.toObject(ProductInfo.class);
                                product.setId(document.getId());
                                product.setInWish(wish);
                                product.setInCart(cart);
                                products.add(product);
                                if (products.size() == value.size()) {
                                    results.setValue(products);
                                }
                            });
                        }
                });
        return results;
    }

    public LiveData<UserResponse> getInWish_and_InCart(String productId) {
        MutableLiveData<UserResponse> liveData = new MutableLiveData<>();
        String userUID = mAuth.getCurrentUser().getUid();
        db.collection(Constants.USERS)
                .document(userUID)
                .collection(Constants.USER_PRODUCTS)
                .document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean wish, cart;
                    if (documentSnapshot != null) {
                        try {
                            wish = (boolean) documentSnapshot.get(Constants.IN_WISHLIST);
                        } catch (Exception ex) {
                            wish = false;
                        }

                        try {
                            cart = (boolean) documentSnapshot.get(Constants.IN_CART);
                        } catch (Exception ex) {
                            cart = false;
                        }
                        UserResponse response = new UserResponse();
                        response.setInCart(cart);
                        response.setInWish(wish);
                        liveData.setValue(response);
                    } else {
                        UserResponse response = new UserResponse();
                        response.setInCart(false);
                        response.setInWish(false);
                        liveData.setValue(response);
                    }
                });
        return liveData;
    }

    public void addToUserProducts(String productId, boolean IsWishListed, boolean IsInCart, String category) {
        String userUID = mAuth.getCurrentUser().getUid();

        Map<String, Object> value = new HashMap<>();
        value.put(Constants.IN_WISHLIST, IsWishListed);
        value.put(Constants.IN_CART, IsInCart);
        value.put(Constants.CATEGORY, category);

        db.collection(Constants.USERS).
                document(userUID).
                collection(Constants.USER_PRODUCTS).document(productId).set(value);

    }

    public LiveData<List<myResponse>> getWishlist() {
        MutableLiveData<List<myResponse>> results = new MutableLiveData<>();
        String curr_user_id = mAuth.getCurrentUser().getUid();
        db.collection(Constants.USERS)
                .document(curr_user_id)
                .collection(Constants.USER_PRODUCTS)
                .whereEqualTo(Constants.IN_WISHLIST, true)
                .get()
                .addOnCompleteListener(task -> {
                    List<myResponse> response = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        response.add(new myResponse(document.getId(), document.getString(Constants.CATEGORY))); // id - category
                    }
                    results.setValue(response);
                });
        return results;
    }

    public LiveData<ProductInfo> getWishlistProduct(String category, String productId) {

        MutableLiveData<ProductInfo> product = new MutableLiveData<>();

        db.collection(Constants.CATEGORIES)
                .document(category)
                .collection(Constants.PRODUCTS)
                .document(productId)
                .get()
                .addOnCompleteListener(task -> {
                    ProductInfo productInfo = task.getResult().toObject(ProductInfo.class);
                    productInfo.setId(task.getResult().getId());
                    product.setValue(productInfo);
                });
        return product;
    }

    public LiveData<List<myResponse>> getCartList() {
        MutableLiveData<List<myResponse>> results = new MutableLiveData<>();
        String curr_user_id = mAuth.getCurrentUser().getUid();
        db.collection(Constants.USERS)
                .document(curr_user_id)
                .collection(Constants.USER_PRODUCTS)
                .whereEqualTo(Constants.IN_CART, true)
                .get()
                .addOnCompleteListener(task -> {
                    List<myResponse> response = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        response.add(new myResponse(document.getId(), document.getString(Constants.CATEGORY)));
                    }
                    results.setValue(response);
                });
        return results;
    }

    public LiveData<ProductInfo> getCartProduct(String category, String productId) {

        MutableLiveData<ProductInfo> product = new MutableLiveData<>();
        db.collection(Constants.CATEGORIES)
                .document(category)
                .collection(Constants.PRODUCTS)
                .document(productId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        ProductInfo result = task.getResult().toObject(ProductInfo.class);
                        result.setId(task.getResult().getId());
                        product.setValue(result);
                    }
                });
        return product;
    }

    public void removeFormWishlist(ProductInfo product) {
        String curr_user_id = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.IN_WISHLIST, false);
        db.collection(Constants.USERS)
                .document(curr_user_id)
                .collection(Constants.USER_PRODUCTS)
                .document(product.getId())
                .update(data);
    }

    public void removeFormCart(ProductInfo product) {
        String curr_user_id = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.IN_CART, false);
        db.collection(Constants.USERS)
                .document(curr_user_id)
                .collection(Constants.USER_PRODUCTS)
                .document(product.getId())
                .update(data);
    }

    public LiveData<List<ProductInfo>> searchForProduct() {
        MutableLiveData<List<ProductInfo>> results = new MutableLiveData<>();
        db.collectionGroup(Constants.PRODUCTS)
                .get()
                .addOnCompleteListener(task -> {
                    List<ProductInfo> responses = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ProductInfo info = document.toObject(ProductInfo.class);
                        info.setId(document.getId());
                        responses.add(info);
                    }
                    results.setValue(responses);
                });
        return results;
    }

    public LiveData<Product> getProduct(String category, String productId) {
        MutableLiveData<Product> product = new MutableLiveData<>();
        db.collection(Constants.CATEGORIES)
                .document(category)
                .collection(Constants.PRODUCTS)
                .document(productId)
                .get()
                .addOnCompleteListener(task -> {

                    Product result = task.getResult().toObject(Product.class);
                    result.setId(productId);
                    product.setValue(result);
                });

        return product;
    }

    public LiveData<UserResponse> query(String productId) {
        MutableLiveData<UserResponse> result = new MutableLiveData<>();

        db.collection(Constants.USERS)
                .document(mAuth.getCurrentUser().getUid())
                .collection(Constants.USER_PRODUCTS)
                .document(productId)
                .get()
                .addOnCompleteListener(task -> {
                    UserResponse response;
                    if(task.getResult().exists())
                    {
                        response = task.getResult().toObject(UserResponse.class);
                    }
                    else {
                        response=new UserResponse();
                        response.setInWish(false);
                        response.setInCart(false);
                    }
                    result.setValue(response);
                });
        return result;
    }
}
