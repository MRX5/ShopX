package com.example.shopx;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shopx.HomeFragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.shopx.Model.Mobile;

public class Repository {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public Repository(Application app)
    {
        db=FirebaseFirestore.getInstance();
    }

    public LiveData<List<Mobile>> getMobiles()
    {
        MutableLiveData<List<Mobile>> results=new MutableLiveData<>();
        db.collection("Mobiles")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        List<Mobile>mobiles=new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mobiles.add(new Mobile(document.getId(),document.getString("name"),document.getString("price")));
                        }
                        results.setValue(mobiles);
                    }
                });
        return results;
    }

    private void signIn()
    {

    }

}
