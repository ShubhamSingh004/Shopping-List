package com.example.shoppingapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(var id: Int,
                        var name: String,
                        var quality: Double,
                        var isEditing: Boolean = false)

@Composable
fun ShoppingApp(paddingValues: PaddingValues) {
    var shoppingItemsList by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    var invalidItemNameDisplay by remember { mutableStateOf(false) }
    var invalidItemQualityDisplay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {showDialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(shoppingItemsList) { item ->
                ShoppingListItem(item, {}, {})
            }
        }
    }

    if(showDialog){
        AlertDialog(
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                        Button(onClick = {
                            if(itemName.isBlank()){
                               invalidItemNameDisplay = true
                            }
                            if(itemQuantity.toDoubleOrNull() == null)
                                invalidItemQualityDisplay = true


                            if(itemName.isNotBlank() && itemQuantity.toIntOrNull() != null){
                                val newItem: ShoppingItem = ShoppingItem(
                                    shoppingItemsList.size + 1,
                                    itemName,
                                    itemQuantity.toDouble()
                                )

                                itemName = ""
                                itemQuantity = ""
                                showDialog = false
                                invalidItemQualityDisplay = false
                                invalidItemNameDisplay = false

                                shoppingItemsList += newItem
                            }
                        }) { Text("Add")}

                    Button(onClick = {
                            showDialog = false
                    }) { Text("Cancel")}
                }
            },
            onDismissRequest = {showDialog = false},
            title = {Text("Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        label = {Text("Item Name")},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if(invalidItemNameDisplay)
                        Text("Item name cannot be blank",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().padding(8.dp))

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        label = {Text("Item Quantity")},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if(invalidItemQualityDisplay)
                        Text("Item name should be a number",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().padding(8.dp))
                }
            })
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
){
    Row(modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .border(
                                border = BorderStroke(width = 2.dp, color = Color.LightGray),
                                shape = RoundedCornerShape(20)
                            )) {
        Text(item.name, modifier = Modifier.padding(2.dp))
    }
}