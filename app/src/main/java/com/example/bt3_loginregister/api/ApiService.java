package com.example.bt3_loginregister.api;

import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Product;
import com.example.bt3_loginregister.model.request.LoginRequest;
import com.example.bt3_loginregister.model.request.OtpRequest;
import com.example.bt3_loginregister.model.request.RegisterRequest;
import com.example.bt3_loginregister.model.response.LoginResponse;
import com.example.bt3_loginregister.model.response.OtpResponse;
import com.example.bt3_loginregister.model.response.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("products/latest")
    Call<List<Product>> getLatestProducts();

    // Authentication endpoints
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("auth/verify-otp")
    Call<OtpResponse> verifyOtp(@Body OtpRequest otpRequest);

}

