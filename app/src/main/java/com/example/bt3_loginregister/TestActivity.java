package com.example.bt3_loginregister;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt3_loginregister.adapter.CategoryAdapter;
import com.example.bt3_loginregister.adapter.ProductAdapter;
import com.example.bt3_loginregister.model.Category;
import com.example.bt3_loginregister.model.Product;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView productsRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Ánh xạ các view
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        // Thiết lập RecyclerView cho Categories (theo chiều ngang)
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Thiết lập RecyclerView cho Products (hiển thị dạng lưới 2 cột)
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Tạo dữ liệu mẫu và thiết lập adapter
        setupCategoriesAdapter();
        setupProductsAdapter();

        // Xử lý sự kiện bottom navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout supportBtn = findViewById(R.id.supportBtn);
        LinearLayout settingsBtn = findViewById(R.id.settingsBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                // Đây đã là trang Home nên không cần chuyển đi đâu
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                // Thêm code để chuyển đến trang Profile nếu cần
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Cart clicked", Toast.LENGTH_SHORT).show();
                // Thêm code để chuyển đến trang Cart nếu cần
            }
        });

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Support clicked", Toast.LENGTH_SHORT).show();
                // Thêm code để chuyển đến trang Support nếu cần
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                // Thêm code để chuyển đến trang Settings nếu cần
            }
        });
    }

    private void setupCategoriesAdapter() {
        // Tạo dữ liệu mẫu cho Categories
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Pizza", "https://cdn-icons-png.flaticon.com/128/3132/3132693.png"));
        categoryList.add(new Category(2, "Burger", "https://cdn-icons-png.flaticon.com/128/3075/3075977.png"));
        categoryList.add(new Category(3, "Hotdog", "https://cdn-icons-png.flaticon.com/128/3075/3075929.png"));
        categoryList.add(new Category(4, "Drink", "https://cdn-icons-png.flaticon.com/128/3081/3081065.png"));
        categoryList.add(new Category(5, "Donut", "https://cdn-icons-png.flaticon.com/128/3075/3075850.png"));

        // Thiết lập adapter
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void setupProductsAdapter() {
        // Tạo dữ liệu mẫu cho Products
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Pepperoni Pizza", "Delicious pizza with pepperoni", 8.99, "https://www.freepnglogos.com/uploads/pizza-png/pizza-slice-icon-download-icons-14.png", 1));
        productList.add(new Product(2, "Cheese Burger", "Tasty burger with cheese", 5.99, "https://www.freepnglogos.com/uploads/burger-png/burger-png-transparent-images-png-only-27.png", 2));
        productList.add(new Product(3, "Classic Hotdog", "Classic hotdog with ketchup", 4.49, "https://www.freepnglogos.com/uploads/hotdog-png/hotdog-transparent-images-download-clip-art-19.png", 3));
        productList.add(new Product(4, "Coca Cola", "Refreshing soft drink", 1.99, "https://www.freepnglogos.com/uploads/coca-cola-png/coca-cola-soda-soft-drink-34.png", 4));
        productList.add(new Product(5, "Glazed Donut", "Sweet glazed donut", 2.49, "https://www.freepnglogos.com/uploads/donut-png/donut-png-images-transparent-background-12.png", 5));
        productList.add(new Product(6, "Veggie Pizza", "Healthy pizza with vegetables", 9.99, "https://www.freepnglogos.com/uploads/pizza-png/pizza-images-download-pizza-17.png", 1));

        // Thiết lập adapter
        productAdapter = new ProductAdapter(this, productList);
        productsRecyclerView.setAdapter(productAdapter);
    }
}