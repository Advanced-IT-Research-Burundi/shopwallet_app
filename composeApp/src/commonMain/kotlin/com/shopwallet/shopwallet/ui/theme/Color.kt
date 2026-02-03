package com.shopwallet.shopwallet.ui.theme

import androidx.compose.ui.graphics.Color

// Light Colors
val PrimaryLight = Color(0xFF0066FF)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFD6E4FF) // Generated/Approximated
val OnPrimaryContainerLight = Color(0xFF001B3D) // Generated/Approximated

val SecondaryLight = Color(0xFFF5F5F5)
val OnSecondaryLight = Color(0xFF0A0A0A)
val SecondaryContainerLight = Color(0xFFFFFFFF) // Using Card white as container often
val OnSecondaryContainerLight = Color(0xFF0A0A0A)

val BackgroundLight = Color(0xFFFAFAFA)
val OnBackgroundLight = Color(0xFF0A0A0A)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF0A0A0A)

val ErrorLight = Color(0xFFEF4444)
val OnErrorLight = Color(0xFFFFFFFF)

val OutlineLight = Color(0xFFE5E7EB)
val SurfaceVariantLight = Color(0xFFF5F5F5) // Muted
val OnSurfaceVariantLight = Color(0xFF6B7280) // Muted Foreground

// Dark Colors (Approximated from oklch values in CSS or standard mapping)
// CSS: --background: oklch(0.145 0 0) -> Very Dark Grey almost Black
// CSS: --foreground: oklch(0.985 0 0) -> White
// CSS: --primary: oklch(0.985 0 0) -> White (In dark mode, primary is white in the CSS)
// CSS: --primary-foreground: oklch(0.205 0 0) -> Dark Grey

val BackgroundDark = Color(0xFF1A1A1A) // Approx for oklch(0.145 0 0)
val OnBackgroundDark = Color(0xFFFCFCFC)

val SurfaceDark = Color(0xFF1A1A1A)
val OnSurfaceDark = Color(0xFFFCFCFC)

val PrimaryDark = Color(0xFFFFFFFF) // CSS maps primary to white in dark mode
val OnPrimaryDark = Color(0xFF333333)

val SecondaryDark = Color(0xFF444444) // Approx
val OnSecondaryDark = Color(0xFFFCFCFC)

val ErrorDark = Color(0xFFEF4444)
val OnErrorDark = Color(0xFFFFFFFF)

val OutlineDark = Color(0xFF444444)
val SurfaceVariantDark = Color(0xFF444444)
val OnSurfaceVariantDark = Color(0xFFAAAAAA)
