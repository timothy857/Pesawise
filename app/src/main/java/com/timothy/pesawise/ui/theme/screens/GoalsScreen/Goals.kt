package com.timothy.pesawise.ui.theme.screens.GoalsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.Goal
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.*
import com.timothy.pesawise.ui.theme.DarkBg
import com.timothy.pesawise.ui.theme.MutedGray
import com.timothy.pesawise.ui.theme.SoftGray

@Composable
fun GoalsScreen(
    user: User,
    accentColor: Color,
    onBack: () -> Unit
) {
    var showForm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        // Dark Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBg)
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
            ) {
                BackButton(onBack)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFF4757).copy(alpha = 0.2f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🎯", fontSize = 18.sp)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Savings Goals",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    
                    Button(
                        onClick = { showForm = !showForm },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("+ New", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (user.goals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No goals set yet", color = MutedGray)
                }
            } else {
                user.goals.forEach { goal ->
                    GoalItemCard(goal)
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun GoalItemCard(goal: Goal) {
    val pct = ((goal.saved / goal.target) * 100).toInt().coerceIn(0, 100)
    val goalColor = try { Color(android.graphics.Color.parseColor(goal.colorHex)) } catch (e: Exception) { Color.Gray }

    PesaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(goalColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(goal.icon, fontSize = 28.sp)
                }
                Column {
                    Text(goal.title, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text("Due: ${goal.deadline}", fontSize = 12.sp, color = MutedGray)
                }
            }
            
            Surface(
                color = Color(0xFFFFE4E6),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$pct%",
                    color = Color(0xFFF43F5E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        
        Spacer(Modifier.height(20.dp))
        
        PesaProgressBar(goal.saved, goal.target, goalColor)
        
        Spacer(Modifier.height(12.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "${money(goal.saved)} saved",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = goalColor
            )
            Text(
                text = "${money(goal.target - goal.saved)} to go",
                fontSize = 12.sp,
                color = MutedGray
            )
        }
        
        Spacer(Modifier.height(20.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { },
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = goalColor.copy(alpha = 0.1f), contentColor = goalColor)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💵", fontSize = 16.sp)
                    Spacer(Modifier.width(6.dp))
                    Text("Add Savings", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            
            Button(
                onClick = { },
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SoftGray, contentColor = MutedGray)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📊", fontSize = 16.sp)
                    Spacer(Modifier.width(6.dp))
                    Text("Details", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
