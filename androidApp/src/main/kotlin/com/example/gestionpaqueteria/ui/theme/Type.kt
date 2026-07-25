package com.example.gestionpaqueteria.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

object AmazonTypography {
    val TopBarTitle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = AmazonOrange
    )
    val TopBarSub = TextStyle(
        fontSize = 9.sp,
        fontWeight = FontWeight.Normal,
        color = com.example.gestionpaqueteria.ui.theme.TopBarSub
    )
    val SectionTitle = TextStyle(
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        color = SectionTitleColor,
        letterSpacing = 0.5.sp
    )
    val FieldLabel = TextStyle(
        fontSize = 8.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary
    )
    val FieldValue = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary
    )
    val Placeholder = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = TextMuted
    )
    val Coordinates = TextStyle(
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary
    )
    val SubtextCoords = TextStyle(
        fontSize = 7.5.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary
    )
    val ButtonPrimary = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = SurfaceWhite
    )
    val ButtonCancel = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        color = TextCancel
    )
    val StepLabel = TextStyle(
        fontSize = 7.5.sp,
        fontWeight = FontWeight.Normal
    )
    val ChipId = TextStyle(
        fontSize = 8.sp,
        fontWeight = FontWeight.Normal,
        color = TextCancel
    )
}
