package com.example.shoppingapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id: Int,
                        var name: String,
                        var quality: Int,
                        var isEditing: Boolean = false)




@Composable
fun ShoppingApp(paddingValues: PaddingValues) {
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingItem>()) }

    Column(
        modifier = Modifier.padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = 0) {

            }
        }
    }
}