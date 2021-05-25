package com.example.shopx.MainActivity;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.shopx.Model.Mobile;
import com.example.shopx.Repository;

import java.util.List;

public class SharedViewModel extends ViewModel {

    public MutableLiveData<List<Mobile>> mobiles=null;

    public void sendMobiles(List<Mobile>results)
    {
        mobiles=new MutableLiveData<>();
        mobiles.setValue(results);
    }

    public void cleanMemory()
    {
        mobiles=null;
    }

}
