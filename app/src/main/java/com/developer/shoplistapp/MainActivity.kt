package com.developer.shoplistapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.developer.shoplistapp.ui.theme.ShopListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingList()
                }
            }
        }
    }
}

@Composable
fun ShoppingList(){
    var shoppingItems by remember { mutableStateOf(listOf<ShoppingListItem>())}
    var showDialog by remember { mutableStateOf(false)}
    var itemName by remember {mutableStateOf("")}
    var itemQuantity by remember {mutableStateOf("")}

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
                        label = {Text(text = "Enter Item Name")}

                    )

                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier.padding(8.dp),
                        label = {Text(text = "Enter Item Quantity")}
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

@Preview(showBackground = true)
@Composable
fun ShoppingListPreview(){
    ShoppingList()
}

data class ShoppingListItem(
    var id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)