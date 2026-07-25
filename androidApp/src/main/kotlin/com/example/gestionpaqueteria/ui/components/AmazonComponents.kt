package com.example.gestionpaqueteria.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import com.example.gestionpaqueteria.ui.theme.*

@Composable
fun AmazonTopBar(
    title: String,
    subtitle: String = "Sistema Logístico Amazon",
    letter: String = "A"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmazonDark)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AmazonOrange),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                color = AmazonDark,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = AmazonTypography.TopBarTitle
            )
            Text(
                text = subtitle,
                style = AmazonTypography.TopBarSub
            )
        }
    }
}

@Composable
fun AmazonStepBar(activeStep: Int) {
    Column(modifier = Modifier.background(AmazonNavy)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                val color = when {
                    index < activeStep -> SuccessGreen
                    index == activeStep -> AmazonOrange
                    else -> StepPending
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val steps = listOf("Admisión", "Distribución", "Última Milla")
            steps.forEachIndexed { index, label ->
                Text(
                    text = label,
                    style = AmazonTypography.StepLabel,
                    color = if (index == activeStep) AmazonOrange else TopBarSub
                )
            }
        }
    }
}

@Composable
fun AmazonTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(8.dp))
            .border(0.5.dp, BorderLight, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Text(text = label, style = AmazonTypography.FieldLabel)
        Spacer(modifier = Modifier.height(2.dp))
        Box {
            if (value.isEmpty() && placeholder.isNotEmpty()) {
                Text(text = placeholder, style = AmazonTypography.Placeholder)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = AmazonTypography.FieldValue,
                cursorBrush = SolidColor(AmazonOrange),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun AmazonSectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        style = AmazonTypography.SectionTitle,
        modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
    )
}

@Composable
fun AmazonDivider() {
    HorizontalDivider(
        thickness = 0.5.dp,
        color = BorderLight,
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

@Composable
fun AmazonCoordinateCard(
    lat: Double?,
    lng: Double?,
    status: String = "Marcador fijado manualmente"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(8.dp))
            .border(0.5.dp, AmazonOrange, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(50))
                .background(AmazonOrange)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = if (lat != null && lng != null) "%.6f, %.6f".format(lat, lng) else "Sin coordenadas",
                style = AmazonTypography.Coordinates
            )
            Text(text = status, style = AmazonTypography.SubtextCoords)
        }
    }
}

@Composable
fun PinIcon(color: Color) {
    Canvas(modifier = Modifier.size(14.dp)) {
        val width = size.width
        val height = size.height
        // Pin body
        drawCircle(color, radius = width / 3f, center = Offset(width / 2f, height / 3f))
        // Pin tip
        val path = Path().apply {
            moveTo(width / 2f - width / 4f, height / 3f + width / 8f)
            lineTo(width / 2f + width / 4f, height / 3f + width / 8f)
            lineTo(width / 2f, height)
            close()
        }
        drawPath(path, color)
        // Center hole
        drawCircle(AmazonDark, radius = width / 8f, center = Offset(width / 2f, height / 3f))
    }
}

@Composable
fun SendIcon(color: Color) {
    Canvas(modifier = Modifier.size(13.dp)) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, height / 2f)
            lineTo(0f, height)
            lineTo(width * 0.2f, height / 2f)
            close()
        }
        drawPath(path, color)
    }
}

@Composable
fun AmazonMapButton(
    onClick: () -> Unit,
    text: String = "Cambiar ubicación en mapa"
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = AmazonDark),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 9.dp)
    ) {
        PinIcon(color = AmazonOrange)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = AmazonOrange, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AmazonIdChip(id: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF0F0F0))
            .padding(vertical = 4.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(RoundedCornerShape(50))
                .background(SuccessGreen)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = id, style = AmazonTypography.ChipId)
    }
}

@Composable
fun AmazonPrimaryButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AmazonOrange,
            disabledContainerColor = AmazonOrange.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 11.dp)
    ) {
        SendIcon(color = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = AmazonTypography.ButtonPrimary)
    }
}

@Composable
fun AmazonMapPanel(
    lat: Double?,
    lng: Double?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    confirmText: String = "Confirmar ubicación"
) {
    Surface(
        color = SurfaceWhite,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(AmazonOrange)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (lat != null && lng != null) "%.6f, %.6f".format(lat, lng) else "Fijar punto",
                    style = AmazonTypography.Coordinates
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = CancelGray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 9.dp)
                ) {
                    Text(text = "Cancelar", style = AmazonTypography.ButtonCancel)
                }
                Button(
                    onClick = onConfirm,
                    enabled = lat != null,
                    colors = ButtonDefaults.buttonColors(containerColor = AmazonOrange),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(2f),
                    contentPadding = PaddingValues(vertical = 9.dp)
                ) {
                    Text(text = confirmText, style = AmazonTypography.ButtonPrimary.copy(fontSize = 10.sp))
                }
            }
        }
    }
}
