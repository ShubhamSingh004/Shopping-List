package com.example.shoppingapp

// Importing necessary Compose UI components and tools
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Data class representing a shopping item
data class ShoppingItem(
    var id: Int,                  // Unique ID for the item
    var name: String,             // Name of the item
    var quantity: Double,         // Quantity of the item
    var isEditing: Boolean = false // Flag to track if item is in editing mode
)

// Main composable function for the Shopping App
@Composable
fun ShoppingApp(paddingValues: PaddingValues) {
    // State to hold list of shopping items
    var shoppingItemsList by remember { mutableStateOf(listOf<ShoppingItem>()) }

    // Dialog visibility state
    var showDialog by remember { mutableStateOf(false) }

    // States for input fields in dialog
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    // Validation error flags
    var invalidItemNameDisplay by remember { mutableStateOf(false) }
    var invalidItemQuantityDisplay by remember { mutableStateOf(false) }

    // UI layout
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        // Button to open "Add Item" dialog
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Display the list of shopping items
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(shoppingItemsList) { item ->
                if (item.isEditing) {
                    // Show editor if item is being edited
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQuantity ->
                            // Turn off editing mode for all items
                            shoppingItemsList = shoppingItemsList.map { it.copy(isEditing = false) }

                            // Find the item and update its details
                            val editedItem = shoppingItemsList.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity ?: 0.0
                            }
                        }
                    )
                } else {
                    // Display item in regular (non-editing) mode
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            // Enable editing for the selected item
                            shoppingItemsList = shoppingItemsList.map {
                                it.copy(isEditing = (it.id == item.id))
                            }
                        },
                        onDeleteClick = {
                            // Remove item from list
                            shoppingItemsList = shoppingItemsList - item
                        }
                    )
                }
            }
        }
    }

    // Show Add Item dialog if triggered
    if (showDialog) {
        AlertDialog(
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Add button logic
                    Button(onClick = {
                        // Validation
                        if (itemName.isBlank()) {
                            invalidItemNameDisplay = true
                        }
                        if (itemQuantity.toDoubleOrNull() == null) {
                            invalidItemQuantityDisplay = true
                        }

                        // If input is valid, create new item
                        if (itemName.isNotBlank() && itemQuantity.toDoubleOrNull() != null) {
                            val newItem = ShoppingItem(
                                shoppingItemsList.size + 1,
                                itemName,
                                itemQuantity.toDouble()
                            )

                            // Reset dialog and input fields
                            itemName = ""
                            itemQuantity = ""
                            showDialog = false
                            invalidItemQuantityDisplay = false
                            invalidItemNameDisplay = false

                            // Add new item to list
                            shoppingItemsList += newItem
                        }
                    }) { Text("Add") }

                    // Cancel button
                    Button(onClick = {
                        showDialog = false
                    }) { Text("Cancel") }
                }
            },
            onDismissRequest = { showDialog = false },
            title = { Text("Add Shopping Item") },
            text = {
                Column {
                    // Input for item name
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error message if name is invalid
                    if (invalidItemNameDisplay)
                        Text(
                            "Item name cannot be blank",
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                    // Input for quantity
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        label = { Text("Item Quantity") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Error message if quantity is invalid
                    if (invalidItemQuantityDisplay)
                        Text(
                            "Item name should be a number",
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                }
            })
    }
}

// Composable to display a shopping item in list view
@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(width = 2.dp, color = Color.LightGray),
                shape = RoundedCornerShape(20)
            )
    ) {
        // Display item name and quantity
        Text(item.name, modifier = Modifier.padding(16.dp))
        Text(item.quantity.toString(), modifier = Modifier.padding(16.dp))

        // Buttons for edit and delete
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit item"
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item"
                )
            }
        }
    }
}

// Composable to edit a shopping item
@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Double?) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Input fields for name and quantity
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(start = 2.dp)
        ) {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                singleLine = true
            )

            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Save button to confirm edits
        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toDoubleOrNull() ?: 0.0)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
