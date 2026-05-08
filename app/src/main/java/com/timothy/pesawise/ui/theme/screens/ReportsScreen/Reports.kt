package com.timothy.pesawise.ui.theme.screens.ReportsScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.PIE_COLORS
import com.timothy.pesawise.models.User
import com.timothy.pesawise.ui.components.BackButton
import com.timothy.pesawise.ui.components.PesaCard
import com.timothy.pesawise.ui.components.money
import com.timothy.pesawise.ui.theme.SoftGray

@Composable
fun ReportsScreen(
    user: User,
    accentColor: Color,
    startColor: Color,
    endColor: Color,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(startColor, endColor)))
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
            ) {
                BackButton(onBack)
                Text(
                    "Reports & Analytics",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val savings = user.income - user.expenses
                val rate = if (user.income > 0) ((savings / user.income) * 100).toInt() else 0
                
                ReportStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Net Savings",
                    value = money(savings),
                    icon = "💰",
                    color = Color(0xFF4ECDC4)
                )
                
                ReportStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Savings Rate",
                    value = "$rate%",
                    icon = "🎯",
                    color = Color(0xFFF5C842)
                )
            }

            // Spending Breakdown Card
            PesaCard {
                Text(
                    "Spending Breakdown",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                
                Spacer(Modifier.height(24.dp))
                
                if (user.pieData.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChart(
                            data = user.pieData.map { it.value.toFloat() },
                            colors = PIE_COLORS.map { Color(it) }
                        )
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    
                    // Legend
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        user.pieData.forEachIndexed { index, entry ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(RoundedCornerShape(99.dp))
                                        .background(Color(PIE_COLORS[index % PIE_COLORS.size]))
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = entry.name,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No spending data yet", color = Color.Gray)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp)) // Bottom nav space
        }
    }
}

@Composable
fun ReportStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                value,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = color
            )
            Text(
                label,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DonutChart(
    data: List<Float>,
    colors: List<Color>
) {
    val total = data.sum()
    if (total == 0f) return

    Canvas(modifier = Modifier.size(160.dp)) {
        var startAngle = -90f
        data.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Butt)
            )
            startAngle += sweepAngle
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}
