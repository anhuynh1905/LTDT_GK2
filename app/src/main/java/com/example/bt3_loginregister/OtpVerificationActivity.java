package com.example.bt3_loginregister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt3_loginregister.api.ApiService;
import com.example.bt3_loginregister.api.RetrofitClient;
import com.example.bt3_loginregister.model.Otp;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {

    private static final String TAG = "OTP_Khoalt0811";

    private EditText editTextOtp1, editTextOtp2, editTextOtp3, editTextOtp4, editTextOtp5, editTextOtp6;
    private Button buttonVerifyOtp;
    private TextView textViewResendOtp, textViewTimer;
    private ProgressDialog progressDialog;
    private ApiService apiService;
    private CountDownTimer countDownTimer;

    private String email;
    private String verificationCode;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        // Khởi tạo API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.setCancelable(false);

        // Lấy dữ liệu từ intent
        email = getIntent().getStringExtra("email");
        verificationCode = getIntent().getStringExtra("verificationCode");

        // Thay đổi nội dung mô tả để hiển thị email thay vì số điện thoại
        TextView textViewOtpDesc = findViewById(R.id.textViewOtpDesc);
        textViewOtpDesc.setText("Chúng tôi đã gửi mã xác thực đến email " + email + ". Vui lòng nhập mã 6 chữ số dưới đây.");

        // Khởi tạo các view
        editTextOtp1 = findViewById(R.id.editTextOtp1);
        editTextOtp2 = findViewById(R.id.editTextOtp2);
        editTextOtp3 = findViewById(R.id.editTextOtp3);
        editTextOtp4 = findViewById(R.id.editTextOtp4);
        editTextOtp5 = findViewById(R.id.editTextOtp5);
        editTextOtp6 = findViewById(R.id.editTextOtp6);
        buttonVerifyOtp = findViewById(R.id.buttonVerifyOtp);
        textViewResendOtp = findViewById(R.id.textViewResendOtp);
        textViewTimer = findViewById(R.id.textViewTimer);

        // Thiết lập tự động chuyển focus khi nhập
        setupOtpInputs();

        // Nếu có mã xác thực từ đăng ký (chỉ trong môi trường development), điền vào
        if (verificationCode != null && verificationCode.length() == 6) {
            fillOtpFields(verificationCode);
        }

        // Khởi động bộ đếm thời gian
        startTimer();

        // Click nút xác thực OTP
        buttonVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

        // Click gửi lại OTP
        textViewResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    resendOtp();
                    startTimer();
                } else {
                    Toast.makeText(OtpVerificationActivity.this,
                            "Vui lòng đợi hết thời gian để gửi lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thay đổi tiêu đề để phù hợp với xác thực email
        TextView textViewOtpTitle = findViewById(R.id.textViewOtpTitle);
        textViewOtpTitle.setText("Xác thực\nEmail");
    }

    private void setupOtpInputs() {
        editTextOtp1.addTextChangedListener(new OtpTextWatcher(editTextOtp1, editTextOtp2));
        editTextOtp2.addTextChangedListener(new OtpTextWatcher(editTextOtp2, editTextOtp3));
        editTextOtp3.addTextChangedListener(new OtpTextWatcher(editTextOtp3, editTextOtp4));
        editTextOtp4.addTextChangedListener(new OtpTextWatcher(editTextOtp4, editTextOtp5));
        editTextOtp5.addTextChangedListener(new OtpTextWatcher(editTextOtp5, editTextOtp6));
        editTextOtp6.addTextChangedListener(new OtpTextWatcher(editTextOtp6, null));

        // Thiết lập focus ban đầu
        editTextOtp1.requestFocus();
    }

    private void fillOtpFields(String otp) {
        if (otp.length() >= 1) editTextOtp1.setText(String.valueOf(otp.charAt(0)));
        if (otp.length() >= 2) editTextOtp2.setText(String.valueOf(otp.charAt(1)));
        if (otp.length() >= 3) editTextOtp3.setText(String.valueOf(otp.charAt(2)));
        if (otp.length() >= 4) editTextOtp4.setText(String.valueOf(otp.charAt(3)));
        if (otp.length() >= 5) editTextOtp5.setText(String.valueOf(otp.charAt(4)));
        if (otp.length() >= 6) editTextOtp6.setText(String.valueOf(otp.charAt(5)));
    }

    private void startTimer() {
        // Vô hiệu hóa nút gửi lại OTP khi đang chạy bộ đếm thời gian
        textViewResendOtp.setEnabled(false);
        textViewResendOtp.setAlpha(0.5f);
        isTimerRunning = true;

        // Thiết lập bộ đếm 60 giây
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                textViewTimer.setText("Gửi lại sau " + seconds + " giây");
            }

            @Override
            public void onFinish() {
                textViewResendOtp.setEnabled(true);
                textViewResendOtp.setAlpha(1.0f);
                textViewTimer.setText("");
                isTimerRunning = false;
            }
        }.start();
    }

    private void verifyOtp() {
        String otp = editTextOtp1.getText().toString() +
                editTextOtp2.getText().toString() +
                editTextOtp3.getText().toString() +
                editTextOtp4.getText().toString() +
                editTextOtp5.getText().toString() +
                editTextOtp6.getText().toString();

        if (otp.length() < 6) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Tạo request object
        Otp request = new Otp(email, otp);

        // Gọi API
        Call<Map<String, Object>> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> result = response.body();
                    Boolean verified = (Boolean) result.get("verified");

                    if (verified != null && verified) {
                        Toast.makeText(OtpVerificationActivity.this,
                                "Xác thực thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển đến màn hình đăng nhập
                        Intent intent = new Intent(OtpVerificationActivity.this, Exercise01Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OtpVerificationActivity.this,
                                "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtpVerificationActivity.this,
                            "Lỗi xác thực: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Verification failed: " + t.getMessage());
                Toast.makeText(OtpVerificationActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp() {
        progressDialog.show();

        // Tạo request object
        Otp request = new Otp(email);

        // Gọi API
        Call<Map<String, Object>> call = apiService.resendOtp(request);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> result = response.body();
                    String message = (String) result.get("message");
                    Toast.makeText(OtpVerificationActivity.this, message, Toast.LENGTH_SHORT).show();

                    // Kiểm tra nếu có OTP trong response (môi trường development)
                    String newOtp = (String) result.get("otp");
                    if (newOtp != null && !newOtp.isEmpty()) {
                        Log.d(TAG, "New OTP: " + newOtp);

                        // Xóa OTP hiện tại
                        editTextOtp1.setText("");
                        editTextOtp2.setText("");
                        editTextOtp3.setText("");
                        editTextOtp4.setText("");
                        editTextOtp5.setText("");
                        editTextOtp6.setText("");

                        // Điền OTP mới
                        fillOtpFields(newOtp);
                    }
                } else {
                    Toast.makeText(OtpVerificationActivity.this,
                            "Không thể gửi lại mã OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Resend failed: " + t.getMessage());
                Toast.makeText(OtpVerificationActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // TextWatcher cho các ô nhập OTP
    private class OtpTextWatcher implements TextWatcher {
        private final EditText currentEditText;
        private final EditText nextEditText;

        public OtpTextWatcher(EditText currentEditText, EditText nextEditText) {
            this.currentEditText = currentEditText;
            this.nextEditText = nextEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && nextEditText != null) {
                nextEditText.requestFocus();
            }
        }
    }
}