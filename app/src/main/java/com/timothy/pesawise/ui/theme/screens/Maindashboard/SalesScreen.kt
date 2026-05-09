package com.timothy.pesawise.ui.theme.screens.Maindashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.pesawise.models.BusinessSale
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.*
import com.timothy.pesawise.viewmodel.AppViewModel

@Composable
fun SalesScreen(
    user: User,
    accentColor: Color,
    onBack: () -> Unit,
    vm: AppViewModel = viewModel()
) {
    var showForm by remember { mutableStateOf(false) }

    // Form fields
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var buyingPrice by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }

    val bizPrimary = BusinessPurple
    val bizSecondary = RoyalBlue
    val bizAccent = GoldAccent

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        GradientHeader(bizPrimary, bizSecondary) {
            BackButton(onBack)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📦 Business Sales",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = bizAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (showForm) "Cancel" else "+ Record", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(Modifier.height(10.dp)) }

            if (showForm) {
                item {
                    PesaCard {
                        Text("RECORD NEW SALE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = bizAccent)
                        Spacer(Modifier.height(16.dp))
                        
                        PesaInput(value = itemName, onValueChange = { itemName = it }, label = "Item Name", placeholder = "e.g. Headphones")
                        Spacer(Modifier.height(12.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                PesaInput(value = quantity, onValueChange = { quantity = it }, label = "Qty", placeholder = "0", keyboardType = KeyboardType.Number)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                PesaInput(value = buyingPrice, onValueChange = { buyingPrice = it }, label = "Buying Price", placeholder = "0", keyboardType = KeyboardType.Number)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        PesaInput(value = sellingPrice, onValueChange = { sellingPrice = it }, label = "Selling Price", placeholder = "0", keyboardType = KeyboardType.Number)
                        
                        Spacer(Modifier.height(20.dp))
                        
                        PesaButton(label = "Save Sale Record", color = bizAccent) {
                            val q = quantity.toIntOrNull() ?: 0
                            val bp = buyingPrice.toDoubleOrNull() ?: 0.0
                            val sp = sellingPrice.toDoubleOrNull() ?: 0.0
                            
                            if (itemName.isNotEmpty() && q > 0) {
                                vm.addBusinessSale(itemName, q, bp, sp)
                                showForm = false
                                itemName = ""; quantity = ""; buyingPrice = ""; sellingPrice = ""
                            }
                        }
                    }
                }
            }

            if (user.businessSales.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 80.dp), contentAlignment = Alignment.Center) {
                        Text("No sales recorded yet.", color = MutedGray)
                    }
                }
            } else {
                items(user.businessSales) { sale ->
                    SaleItemCard(sale, onDelete = { vm.deleteBusinessSale(it) })
                }
            }
            
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SaleItemCard(sale: BusinessSale, onDelete: (Long) -> Unit) {
    val totalBuying = sale.quantity * sale.buyingPrice
    val totalSelling = sale.quantity * sale.sellingPrice
    val profit = totalSelling - totalBuying

    PesaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(sale.itemName, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text("${sale.quantity} units • ${sale.date}", fontSize = 12.sp, color = MutedGray)
            }
            IconButton(onClick = { onDelete(sale.id) }) {
                Text("🗑️", fontSize = 16.sp)
            }
        }
        
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = SoftGray)
        Spacer(Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Unit BP", fontSize = 10.sp, color = MutedGray)
                Text(money(sale.buyingPrice), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column {
                Text("Unit SP", fontSize = 10.sp, color = MutedGray)
                Text(money(sale.sellingPrice), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(if (profit >= 0) "Profit" else "Loss", fontSize = 10.sp, color = MutedGray)
                Text(
                    text = money(profit),
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = if (profit >= 0) SuccessGreen else ErrorRed
                )
            }
        }
    }
}
