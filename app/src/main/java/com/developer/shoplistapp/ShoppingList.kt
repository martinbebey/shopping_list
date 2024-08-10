package com.developer.shoplistapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingList(){
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingListItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val context = LocalContext.current

        Button(
            onClick = {
                Toast.makeText(context, "Add Item Button Clicked", Toast.LENGTH_LONG).show()
                showDialog = true
            },
        ) {
            Text(text = "Add Item")
        }

        LazyColumn(){
            items(shoppingItems){
                item ->

                if(item.isEditing){
                    EditItem(item = item, onEditSave = {
                        itemName, itemQuantity ->
                        item.name = itemName
                        item.quantity = itemQuantity
                        shoppingItems = shoppingItems.map { it.copy(isEditing = false) }
                    })
                }

                else {
                    ShoppingListItem(item,
                        onEditClick = { shoppingItems = shoppingItems.map { it.copy(isEditing = it.id == item.id) }},
                        onDeleteClick = {shoppingItems -= item})
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = {showDialog = false},
            confirmButton = { /*TODO*/ },
            title = { Text(text = "Add Shopping Item") },
            text = {
                Column {
                    val context = LocalContext.current

                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier.padding(8.dp),
                        label = { Text(text = "Enter Item Name") }

                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier.padding(8.dp),
                        label = { Text(text = "Enter Item Quantity") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Button(
                            onClick = {
                                if(itemName.isNotBlank()) {
                                    val newShoppingItem = ShoppingListItem(
                                        shoppingItems.size + 1,
                                        itemName,
                                        itemQuantity.toInt()
                                    )

                                    shoppingItems += newShoppingItem
                                    itemName = ""
                                    itemQuantity = ""
                                    showDialog = false
                                    Toast.makeText(context, "New Item Added", Toast.LENGTH_SHORT).show()
                                }
                            },

                            ) {
                            Text(text = "Add")
                        }

                        Button(
                            onClick = {showDialog = false},

                            ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingListItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFFFFAD45)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))

        Row(
            horizontalArrangement = Arrangement.End
        ){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit button")
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete button")
            }
        }

    }
}

@Composable
fun EditItem(item: ShoppingListItem, onEditSave: (String, Int) -> Unit){
    var itemName by remember { mutableStateOf(item.name) }
    var itemQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            BasicTextField(value = itemName, onValueChange = {itemName = it})
            BasicTextField(value = itemQuantity, onValueChange = {itemQuantity = it})
        }

        Button(
            onClick = {
                onEditSave(itemName, itemQuantity.toIntOrNull() ?: 1)
                isEditing = false
            })
        {
            Text(text = "Save")
        }
    }

}

data class ShoppingListItem(
    var id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)