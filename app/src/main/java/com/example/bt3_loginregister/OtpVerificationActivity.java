package com.example.bt3_loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText editTextOtp1, editTextOtp2, editTextOtp3, editTextOtp4, editTextOtp5, editTextOtp6;
    private Button buttonVerifyOtp;
    private TextView textViewResendOtp;
    private String fullName, phone, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhận dữ liệu từ màn hình đăng ký
        Intent intent = getIntent();
        if (intent != null) {
            fullName = intent.getStringExtra("fullName");
            phone = intent.getStringExtra("phone");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
        }

        // Ánh xạ các view
        editTextOtp1 = findViewById(R.id.editTextOtp1);
        editTextOtp2 = findViewById(R.id.editTextOtp2);
        editTextOtp3 = findViewById(R.id.editTextOtp3);
        editTextOtp4 = findViewById(R.id.editTextOtp4);
        editTextOtp5 = findViewById(R.id.editTextOtp5);
        editTextOtp6 = findViewById(R.id.editTextOtp6);
        buttonVerifyOtp = findViewById(R.id.buttonVerifyOtp);
        textViewResendOtp = findViewById(R.id.textViewResendOtp);

        // Thiết lập di chuyển tự động giữa các ô nhập OTP
        setupOtpTextWatchers();

        // Xử lý sự kiện khi nhấn nút xác thực
        buttonVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editTextOtp1.getText().toString() +
                        editTextOtp2.getText().toString() +
                        editTextOtp3.getText().toString() +
                        editTextOtp4.getText().toString() +
                        editTextOtp5.getText().toString() +
                        editTextOtp6.getText().toString();

                // Kiểm tra OTP (giả định là "123456")
                if (otp.length() == 6) {
                    if (otp.equals("123456")) {
                        // OTP đúng, tạo tài khoản thành công
                        Toast.makeText(OtpVerificationActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                        // Lưu thông tin người dùng (có thể thực hiện API request ở đây)
                        // ...

                        // Chuyển về màn hình đăng nhập
                        Intent loginIntent = new Intent(OtpVerificationActivity.this, Exercise01Login.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Xóa các activity trước đó
                        startActivity(loginIntent);
                        finish();
                    } else {
                        Toast.makeText(OtpVerificationActivity.this, "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Vui lòng nhập đủ 6 số!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn vào gửi lại OTP
        textViewResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OtpVerificationActivity.this, "Đã gửi lại mã OTP!", Toast.LENGTH_SHORT).show();
                // Reset các ô nhập OTP
                editTextOtp1.setText("");
                editTextOtp2.setText("");
                editTextOtp3.setText("");
                editTextOtp4.setText("");
                editTextOtp5.setText("");
                editTextOtp6.setText("");
                editTextOtp1.requestFocus();
            }
        });
    }

    private void setupOtpTextWatchers() {
        editTextOtp1.addTextChangedListener(new OtpTextWatcher(editTextOtp1, editTextOtp2));
        editTextOtp2.addTextChangedListener(new OtpTextWatcher(editTextOtp2, editTextOtp3));
        editTextOtp3.addTextChangedListener(new OtpTextWatcher(editTextOtp3, editTextOtp4));
        editTextOtp4.addTextChangedListener(new OtpTextWatcher(editTextOtp4, editTextOtp5));
        editTextOtp5.addTextChangedListener(new OtpTextWatcher(editTextOtp5, editTextOtp6));
        editTextOtp6.addTextChangedListener(new OtpTextWatcher(editTextOtp6, null));
    }

    // Class hỗ trợ di chuyển tự động giữa các ô OTP
    private class OtpTextWatcher implements TextWatcher {
        private EditText currentEditText;
        private EditText nextEditText;

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