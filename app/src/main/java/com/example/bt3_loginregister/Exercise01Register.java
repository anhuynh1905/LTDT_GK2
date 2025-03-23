package com.example.bt3_loginregister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt3_loginregister.api.ApiService;
import com.example.bt3_loginregister.api.RetrofitClient;
import com.example.bt3_loginregister.model.RegisterRequest;
import com.example.bt3_loginregister.model.RegisterResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Exercise01Register extends AppCompatActivity {

    private static final String TAG = "Register_Khoalt0811";

    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private ProgressDialog progressDialog;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise01_register);

        // Khởi tạo API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);

        // Ánh xạ các view
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewLogin = findViewById(R.id.textViewLogin);

        // Xử lý sự kiện click nút đăng ký
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Xử lý sự kiện click text đăng nhập
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng nhập
                Intent intent = new Intent(Exercise01Register.this, Exercise01Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        // Lấy giá trị từ các trường nhập
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (fullName.isEmpty()) {
            editTextFullName.setError("Vui lòng nhập họ tên");
            editTextFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Vui lòng nhập email");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email không hợp lệ");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Vui lòng nhập mật khẩu");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // Hiển thị dialog loading
        progressDialog.show();

        // Tạo request body
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(fullName);
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        // Log thông tin request để debug
        Log.d(TAG, "Register request: " + new Gson().toJson(registerRequest));

        // Gọi API đăng ký
        Call<RegisterResponse> call = apiService.requestRegister(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();

                // Log thông tin response để debug
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();

                    // Log response body để debug
                    Log.d(TAG, "Response body: " + new Gson().toJson(registerResponse));

                    // Chuyển đến màn hình xác thực OTP
                    Intent intent = new Intent(Exercise01Register.this, OtpVerificationActivity.class);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("email", email);
                    intent.putExtra("username", email); // Sử dụng email làm username
                    intent.putExtra("password", password);

                    // Gửi mã OTP nếu có (môi trường dev)
                    if (registerResponse.getOtp() != null && !registerResponse.getOtp().isEmpty()) {
                        intent.putExtra("verificationCode", registerResponse.getOtp());
                        Log.d(TAG, "OTP received: " + registerResponse.getOtp());
                    } else {
                        // Nếu không có OTP trong response, log để debug
                        Log.d(TAG, "No OTP in response. User will need to check email.");
                    }

                    startActivity(intent);

                    Toast.makeText(Exercise01Register.this,
                            "Đăng ký thành công! Vui lòng xác thực mã OTP",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error: " + errorBody);

                        // Hiển thị thông báo lỗi thân thiện hơn
                        String errorMessage;
                        if (response.code() == 400) {
                            errorMessage = "Email đã được sử dụng hoặc thông tin không hợp lệ";
                        } else if (response.code() == 500) {
                            errorMessage = "Lỗi server. Vui lòng thử lại sau";
                        } else {
                            errorMessage = "Đăng ký thất bại: " + response.code();
                        }

                        Toast.makeText(Exercise01Register.this,
                                errorMessage,
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                        Toast.makeText(Exercise01Register.this,
                                "Đã xảy ra lỗi không xác định",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "API call failed", t);

                // Phân tích chi tiết lỗi để hiển thị thông báo phù hợp
                String errorMessage;
                if (t.getMessage() != null && t.getMessage().contains("CLEARTEXT communication")) {
                    errorMessage = "Lỗi kết nối: Vui lòng kiểm tra cài đặt mạng và quyền ứng dụng";

                    // Kiểm tra và cảnh báo nếu thiếu cấu hình usesCleartextTraffic
                    Log.e(TAG, "CLEARTEXT communication error - Make sure you have android:usesCleartextTraffic=\"true\" in your AndroidManifest.xml");
                } else if (t.getMessage() != null && t.getMessage().contains("Failed to connect")) {
                    errorMessage = "Không thể kết nối đến server. Vui lòng kiểm tra địa chỉ server và cài đặt mạng";

                    // Kiểm tra URL của API
                    Log.e(TAG, "Connection error - Check if your BASE_URL is correct in RetrofitClient.java");
                } else if (t.getMessage() != null && t.getMessage().contains("socket failed: EPERM")) {
                    errorMessage = "Lỗi quyền truy cập mạng. Vui lòng kiểm tra cài đặt và quyền của ứng dụng";
                } else {
                    errorMessage = "Lỗi kết nối: " + t.getMessage();
                }

                Toast.makeText(Exercise01Register.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}