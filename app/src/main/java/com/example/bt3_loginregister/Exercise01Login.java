package com.example.bt3_loginregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Exercise01Login extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, facebookButton, googleButton;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise01);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        facebookButton = findViewById(R.id.facebookButton);
        googleButton = findViewById(R.id.googleButton);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Xử lý sự kiện khi nhấn nút đăng nhập
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kiểm tra email và password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Exercise01Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Trong ứng dụng thực tế, bạn sẽ kiểm tra với database hoặc API

                    // Lưu trạng thái đăng nhập
                    SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", email);
                    editor.apply();

                    // Chuyển đến MainActivity
                    Intent intent = new Intent(Exercise01Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Xử lý đăng nhập bằng Facebook
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Exercise01Login.this, "Đăng nhập bằng Facebook (chưa triển khai)", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý đăng nhập bằng Google
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Exercise01Login.this, "Đăng nhập bằng Google (chưa triển khai)", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi nhấn vào đăng ký
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng ký
                Intent intent = new Intent(Exercise01Login.this, Exercise01Register.class);
                startActivity(intent);
            }
        });

    }
}