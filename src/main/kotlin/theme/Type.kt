package theme


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val DefaultTypography = androidx.compose.material.Typography()

val AppTypography = androidx.compose.material.Typography(
    h1 = DefaultTypography.h1.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    h2 = DefaultTypography.h2.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    h3 = DefaultTypography.h3.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    h4 = DefaultTypography.h4.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    h5 = DefaultTypography.h5.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    h6 = DefaultTypography.h6.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),

    subtitle1 = DefaultTypography.subtitle1.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    subtitle2 = DefaultTypography.subtitle2.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),

    body1 = DefaultTypography.body1.copy(fontFamily = FontFamily.SansSerif),
    body2 = DefaultTypography.body2.copy(fontFamily = FontFamily.SansSerif),

    button = DefaultTypography.button.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    caption = DefaultTypography.caption.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium),
    overline = DefaultTypography.overline.copy(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium)
)