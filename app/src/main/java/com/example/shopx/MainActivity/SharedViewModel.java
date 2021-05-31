package com.example.shopx.MainActivity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopx.Model.Product;
import com.example.shopx.Model.ProductInfo;

import java.util.List;

public class SharedViewModel extends ViewModel {

    public MutableLiveData<List<ProductInfo>> mobiles=null;
    public void sendMobiles(List<ProductInfo>results)
    {
        mobiles=new MutableLiveData<>();
        mobiles.setValue(results);
    }

    public void cleanMemory()
    {
        mobiles=null;
    }

}
