package com.example.bt3_loginregister.api;

import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // Endpoint để lấy tất cả danh mục
    @GET("/api/categories")
    Call<List<Category>> getAllCategories();

    // Endpoint để lấy sản phẩm mới nhất (sử dụng endpoint random meals)
    @GET("/api/meals/random/multiple")
    Call<List<Product>> getLatestProducts(@Query("count") int count);

    // Phương thức helper mặc định lấy 6 sản phẩm
    @GET("/api/meals/random/multiple")
    Call<List<Product>> getLatestProducts();
}