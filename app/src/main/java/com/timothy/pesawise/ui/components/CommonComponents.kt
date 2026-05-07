package com.timothy.pesawise.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timothy.pesawise.models.Transaction
import com.timothy.pesawise.models.TransactionType
import com.timothy.pesawise.ui.theme.DangerRed
import com.timothy.pesawise.ui.theme.MutedGray

// ── Format KES amount ─────────────────────────────────────
fun money(amount: Double): String = "KES %,.0f".format(amount)

// ── Gradient brush helper ─────────────────────────────────
fun gradientBrush(startHex: Long, endHex: Long) = Brush.linearGradient(
    colors = listOf(Color(startHex), Color(endHex))
)

// ── Progress Bar ──────────────────────────────────────────
@Composable
fun PesaProgressBar(
    value: Double,
    max: Double,
    color: Color,
    modifier: Modifier = Modifier
) {
    val pct = if (max <= 0) 0f else (value / max).toFloat().coerceIn(0f, 1f)
    val animPct by animateFloatAsState(targetValue = pct, animationSpec = tween(600))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(Color(0xFFE8EFED))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animPct)
                .fillMaxHeight()
                .clip(RoundedCornerShape(99.dp))
                .background(color)
        )
    }
}

// ── Chip / Badge ──────────────────────────────────────────
@Composable
fun PesaChip(label: String, color: Color, small: Boolean = false) {
    val bgColor = color.copy(alpha = 0.13f)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = if (small) 7.dp else 10.dp, vertical = if (small) 2.dp else 3.dp)
    ) {
        Text(
            text  = label,
            color = color,
            fontSize = if (small) 10.sp else 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Toggle Switch ─────────────────────────────────────────
@Composable
fun PesaToggle(on: Boolean, color: Color, onChange: (Boolean) -> Unit) {
    Switch(
        checked = on,
        onCheckedChange = onChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor  = Color.White,
            checkedTrackColor  = color,
            uncheckedThumbColor= Color.White,
            uncheckedTrackColor= Color(0xFFD1D5DB)
        )
    )
}

// ── Gradient Header Box ───────────────────────────────────
@Composable
fun GradientHeader(
    startColor: Color,
    endColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(startColor, endColor)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(20.dp),
            content = content
        )
    }
}

// ── Card ──────────────────────────────────────────────────
@Composable
fun PesaCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        content = { Column(modifier = Modifier.padding(16.dp), content = content) }
    )
}

// ── Transaction Row ───────────────────────────────────────
@Composable
fun TransactionRow(tx: Transaction, accentColor: Color) {
    val color = if (tx.type == TransactionType.Income) accentColor else DangerRed
    PesaCard(modifier = Modifier.padding(bottom = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) { Text(tx.icon, fontSize = 20.sp) }

            Column(modifier = Modifier.weight(1f)) {
                Text(tx.category, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(tx.note, fontSize = 11.sp, color = MutedGray)
            }

            Text(
                text  = "${if (tx.type == TransactionType.Income) "+" else "-"}${money(tx.amount)}",
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 14.sp,
                color      = color
            )
        }
    }
}

// ── Section Header ────────────────────────────────────────
@Composable
fun SectionHeader(title: String, actionLabel: String = "", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
        if (actionLabel.isNotEmpty()) {
            Text(
                text     = actionLabel,
                color    = Color(0xFF00C896),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onAction() }
            )
        }
    }
}

// ── Quick Action Button ───────────────────────────────────
@Composable
fun QuickActionButton(icon: String, label: String, color: Color, onClick: () -> Unit) {
    PesaCard(onClick = onClick, modifier = Modifier.padding(0.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) { Text(icon, fontSize = 16.sp) }
            Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

// ── Back Button ───────────────────────────────────────────
@Composable
fun BackButton(onClick: () -> Unit, label: String = "← Back") {
    Surface(
        onClick = onClick,
        color = Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(99.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

// ── AI Insight Card ───────────────────────────────────────
@Composable
fun AiInsightCard(text: String, accentColor: Color, startColor: Color, endColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(startColor, endColor)))
            .padding(16.dp)
    ) {
        Column {
            Text("🤖 AI INSIGHT", color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(text, color = Color.White, fontSize = 13.sp, lineHeight = 20.sp)
        }
    }
}

// ── Stat Pill inside header card ─────────────────────────
@Composable
fun StatPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = color)
        Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.5f))
    }
}

// ── Input Field ──────────────────────────────────────────
@Composable
fun PesaInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: androidx.compose.ui.text.input.KeyboardType =
        androidx.compose.ui.text.input.KeyboardType.Text
) {
    var showPw by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text     = label.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color    = MutedGray,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        if (isPassword) {
            OutlinedTextField(
                value         = value,
                onValueChange = onValueChange,
                placeholder   = { Text(placeholder, color = MutedGray) },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                visualTransformation = if (showPw) androidx.compose.ui.text.input.VisualTransformation.None
                                       else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPw = !showPw }) {
                        Text(if (showPw) "🙈" else "👁️", fontSize = 16.sp)
                    }
                },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType)
            )
        } else {
            OutlinedTextField(
                value         = value,
                onValueChange = onValueChange,
                placeholder   = { Text(placeholder, color = MutedGray) },
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboardType)
            )
        }
    }
}

// ── Primary Button ────────────────────────────────────────
@Composable
fun PesaButton(label: String, color: Color, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape    = RoundedCornerShape(14.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor = if (enabled) color else Color(0xFFCBD5E1),
            contentColor   = Color.White
        )
    ) { Text(label, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
}

// ── Outline Button ────────────────────────────────────────
@Composable
fun PesaOutlineButton(label: String, color: Color, onClick: () -> Unit) {
    OutlinedButton(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape    = RoundedCornerShape(14.dp),
        border   = androidx.compose.foundation.BorderStroke(2.dp, color),
        colors   = ButtonDefaults.outlinedButtonColors(contentColor = color)
    ) { Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
}
