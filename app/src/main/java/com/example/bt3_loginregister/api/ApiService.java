package com.example.bt3_loginregister.api;

import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Otp;
import com.example.bt3_loginregister.model.Product;
import com.example.bt3_loginregister.model.RegisterRequest;
import com.example.bt3_loginregister.model.RegisterResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("products/latest")
    Call<List<Product>> getLatestProducts();
    @POST("api/auth/request-register")
    Call<RegisterResponse> requestRegister(@Body RegisterRequest request);

    @POST("api/auth/otp/verify")
    Call<Map<String, Object>> verifyOtp(@Body Otp request);

    @POST("api/auth/otp/resend")
    Call<Map<String, Object>> resendOtp(@Body Otp request);

}