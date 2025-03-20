package com.example.bt3_loginregister.api;

import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("products/latest")
    Call<List<Product>> getLatestProducts();


}