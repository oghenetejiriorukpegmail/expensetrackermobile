package com.example.expensetrackermobile;

import android.os.Bundle;
import androidx.activity.compose.setContent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.material3.Text;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.tooling.preview.Preview;
import androidx.compose.ui.unit.dp;
import androidx.compose.foundation.layout.padding;
import androidx.compose.material3.Card;
import androidx.compose.material3.CardDefaults;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(() -> {
            DashboardScreen();
        });
    }

    @Composable
    fun DashboardScreen() {
        Text(text = "Dashboard Screen")
    }
}