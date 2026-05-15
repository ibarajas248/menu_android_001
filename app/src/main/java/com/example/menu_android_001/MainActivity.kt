package com.example.menu_android_001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.menu_android_001.ui.theme.Menu_android_001Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //Prepara la pantalla para usar mejor el espacio.
        setContent { //Activa Compose y define que se va a mostrar.
            Menu_android_001Theme { // aplica el tema
                DiscoveryMenu() //Dibuja tu interfaz principal.
            }
        }
    }
}

private val Golden = Color(0xFFFFCE39)
private val Ink = Color(0xFF262626)
private val Muted = Color(0xFF9E9E9E)
private val Panel = Color(0xFFF8F8F8)

private enum class MenuIcon {
    Hotel, Dining, Cafe, Nearby, FastFood, Featured
}

private data class MenuItem(
    val title: String,
    val places: String,
    val icon: MenuIcon,
    val highlighted: Boolean = false
)

private val menuItems = listOf(
    MenuItem("Bares y Hoteles", "42 lugares", MenuIcon.Hotel),
    MenuItem("Comida Fina", "15 lugares", MenuIcon.Dining),
    MenuItem("Cafes", "28 lugares", MenuIcon.Cafe),
    MenuItem("Cerca de Ti", "34 lugares", MenuIcon.Nearby, highlighted = true),
    MenuItem("Comidas Rapidas", "29 lugares", MenuIcon.FastFood),
    MenuItem("Destacados", "21 lugares", MenuIcon.Featured)
)

private val bottomItems = listOf("Home", "Discover", "Bookmark", "Top Foodie", "Profile")

@Composable
fun DiscoveryMenu() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Panel
    ) { innerPadding ->
        MainMenuScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun MainMenuScreen(
    modifier: Modifier = Modifier
) {    //columna principa
    Column(
        modifier = modifier.background(Panel),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { //columna texto Discovery
        Column( //columna discovery
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Discovery",
                color = Ink,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
        Column( //contenedor de botones
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Panel)
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp, bottom = 14.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), //numero de columnas
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                userScrollEnabled = true
            ) {
                items(menuItems) { item ->
                    CategoryCard(item = item) //funcion que define las cards
                }
            }
        }
        BottomNavigation()
    }
}

@Composable
private fun CategoryCard(item: MenuItem) {
    val background = if (item.highlighted) Golden else Color.White
    val content = if (item.highlighted) Ink else Color(0xFF303030)
    val supporting = if (item.highlighted) Ink.copy(alpha = 0.68f) else Muted

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.94f),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) { //metodo donde se dibuja el icono
            MenuIllustration(
                icon = item.icon,
                tint = content,
                accent = if (item.highlighted) Color.White.copy(alpha = 0.55f) else Golden
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = item.title,
                color = content,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = item.places,
                modifier = Modifier.padding(top = 3.dp),
                color = supporting,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable // se dibujan los iconos
private fun MenuIllustration(icon: MenuIcon, tint: Color, accent: Color) {
    Canvas(modifier = Modifier.size(58.dp)) {
        when (icon) {
            MenuIcon.Hotel -> drawHotelIcon(tint, accent)
            MenuIcon.Dining -> drawDiningIcon(tint, accent)
            MenuIcon.Cafe -> drawCafeIcon(tint, accent)
            MenuIcon.Nearby -> drawNearbyIcon(tint)
            MenuIcon.FastFood -> drawFastFoodIcon(tint, accent)
            MenuIcon.Featured -> drawFeaturedIcon(tint, accent)
        }
    }
}

@Composable
private fun BottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(Color.White)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bottomItems.forEachIndexed { index, label ->
            val selected = index == 1
            Column(
                modifier = Modifier
                    .width(58.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomIcon(index = index, selected = selected)
                Text(
                    text = label,
                    color = if (selected) Golden else Muted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BottomIcon(index: Int, selected: Boolean) {
    val color = if (selected) Golden else Muted
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (index) {
            0 -> {
                val roof = Path().apply {
                    moveTo(size.width * 0.18f, size.height * 0.5f)
                    lineTo(size.width * 0.5f, size.height * 0.18f)
                    lineTo(size.width * 0.82f, size.height * 0.5f)
                }
                drawPath(roof, color, style = stroke)
                drawRoundRect(color, Offset(size.width * 0.28f, size.height * 0.48f), Size(size.width * 0.44f, size.height * 0.34f), style = stroke)
            }
            1 -> drawLocationPin(color, Offset(size.width / 2, size.height * 0.46f), size.minDimension * 0.18f, stroke.width)
            2 -> drawRoundRect(color, Offset(size.width * 0.3f, size.height * 0.18f), Size(size.width * 0.4f, size.height * 0.64f), style = stroke)
            3 -> {
                drawArcIn(color, 0f, 180f, Rect(size.width * 0.25f, size.height * 0.2f, size.width * 0.75f, size.height * 0.62f), style = stroke)
                drawLine(color, Offset(size.width * 0.28f, size.height * 0.62f), Offset(size.width * 0.72f, size.height * 0.62f), stroke.width, StrokeCap.Round)
                drawLine(color, Offset(size.width * 0.5f, size.height * 0.62f), Offset(size.width * 0.5f, size.height * 0.84f), stroke.width, StrokeCap.Round)
            }
            else -> {
                drawCircle(color, size.minDimension * 0.15f, Offset(size.width / 2, size.height * 0.32f), style = stroke)
                drawArcIn(color, 205f, 130f, Rect(size.width * 0.22f, size.height * 0.5f, size.width * 0.78f, size.height), style = stroke)
            }
        }
    }
}

private fun DrawScope.drawHotelIcon(tint: Color, accent: Color) {
    val stroke = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawLine(tint, Offset(size.width * 0.18f, size.height * 0.34f), Offset(size.width * 0.18f, size.height * 0.78f), stroke.width, StrokeCap.Round)
    drawLine(tint, Offset(size.width * 0.52f, size.height * 0.34f), Offset(size.width * 0.52f, size.height * 0.78f), stroke.width, StrokeCap.Round)
    drawRoundRect(tint, Offset(size.width * 0.2f, size.height * 0.34f), Size(size.width * 0.32f, size.height * 0.4f), style = stroke)
    drawArcIn(tint, 200f, 140f, Rect(size.width * 0.08f, size.height * 0.08f, size.width * 0.63f, size.height * 0.45f), style = stroke)
    drawRoundRect(tint, Offset(size.width * 0.58f, size.height * 0.28f), Size(size.width * 0.23f, size.height * 0.5f), style = stroke)
    drawLine(accent, Offset(size.width * 0.28f, size.height * 0.42f), Offset(size.width * 0.28f, size.height * 0.69f), 3f, StrokeCap.Round)
    drawLine(accent, Offset(size.width * 0.4f, size.height * 0.42f), Offset(size.width * 0.4f, size.height * 0.69f), 3f, StrokeCap.Round)
}

private fun DrawScope.drawDiningIcon(tint: Color, accent: Color) {
    val stroke = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawArcIn(accent, 180f, 180f, Rect(size.width * 0.24f, size.height * 0.08f, size.width * 0.72f, size.height * 0.5f), style = stroke)
    drawLine(accent, Offset(size.width * 0.48f, size.height * 0.08f), Offset(size.width * 0.48f, size.height * 0.02f), stroke.width, StrokeCap.Round)
    drawLine(tint, Offset(size.width * 0.18f, size.height * 0.58f), Offset(size.width * 0.48f, size.height * 0.58f), stroke.width, StrokeCap.Round)
    drawLine(tint, Offset(size.width * 0.48f, size.height * 0.58f), Offset(size.width * 0.84f, size.height * 0.44f), stroke.width, StrokeCap.Round)
    drawLine(tint, Offset(size.width * 0.28f, size.height * 0.76f), Offset(size.width * 0.6f, size.height * 0.76f), stroke.width, StrokeCap.Round)
    drawArcIn(tint, 200f, 110f, Rect(size.width * 0.24f, size.height * 0.48f, size.width * 0.66f, size.height * 0.9f), style = stroke)
}

private fun DrawScope.drawCafeIcon(tint: Color, accent: Color) {
    val stroke = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawRoundRect(tint, Offset(size.width * 0.18f, size.height * 0.3f), Size(size.width * 0.58f, size.height * 0.48f), style = stroke)
    drawLine(tint, Offset(size.width * 0.13f, size.height * 0.3f), Offset(size.width * 0.81f, size.height * 0.3f), stroke.width, StrokeCap.Round)
    drawLine(tint, Offset(size.width * 0.2f, size.height * 0.2f), Offset(size.width * 0.74f, size.height * 0.2f), stroke.width, StrokeCap.Round)
    drawArcIn(tint, -70f, 140f, Rect(size.width * 0.66f, size.height * 0.42f, size.width * 0.94f, size.height * 0.72f), style = stroke)
    drawCircle(accent, size.width * 0.07f, Offset(size.width * 0.46f, size.height * 0.57f), style = stroke)
}

private fun DrawScope.drawNearbyIcon(tint: Color) {
    val stroke = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawLocationPin(tint, Offset(size.width * 0.25f, size.height * 0.45f), size.width * 0.11f, stroke.width)
    drawLocationPin(tint, Offset(size.width * 0.72f, size.height * 0.24f), size.width * 0.09f, stroke.width)
    val path = Path().apply {
        moveTo(size.width * 0.34f, size.height * 0.65f)
        cubicTo(size.width * 0.45f, size.height * 0.9f, size.width * 0.78f, size.height * 0.74f, size.width * 0.58f, size.height * 0.55f)
        cubicTo(size.width * 0.46f, size.height * 0.42f, size.width * 0.6f, size.height * 0.34f, size.width * 0.72f, size.height * 0.4f)
    }
    drawPath(path, tint, style = Stroke(width = 2.7f, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.drawFastFoodIcon(tint: Color, accent: Color) {
    val stroke = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawLine(tint, Offset(size.width * 0.18f, size.height * 0.7f), Offset(size.width * 0.76f, size.height * 0.7f), stroke.width, StrokeCap.Round)
    drawRoundRect(tint, Offset(size.width * 0.2f, size.height * 0.48f), Size(size.width * 0.42f, size.height * 0.18f), style = stroke)
    drawArcIn(tint, 180f, 180f, Rect(size.width * 0.19f, size.height * 0.28f, size.width * 0.62f, size.height * 0.64f), style = stroke)
    drawLine(tint, Offset(size.width * 0.72f, size.height * 0.28f), Offset(size.width * 0.72f, size.height * 0.68f), stroke.width, StrokeCap.Round)
    drawLine(tint, Offset(size.width * 0.68f, size.height * 0.25f), Offset(size.width * 0.82f, size.height * 0.25f), stroke.width, StrokeCap.Round)
    drawLine(accent, Offset(size.width * 0.3f, size.height * 0.44f), Offset(size.width * 0.48f, size.height * 0.44f), 3f, StrokeCap.Round)
}

private fun DrawScope.drawFeaturedIcon(tint: Color, accent: Color) {
    val stroke = Stroke(width = 3.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val slice = Path().apply {
        moveTo(size.width * 0.18f, size.height * 0.28f)
        quadraticTo(size.width * 0.68f, size.height * 0.1f, size.width * 0.84f, size.height * 0.42f)
        lineTo(size.width * 0.24f, size.height * 0.78f)
        close()
    }
    drawPath(slice, tint, style = stroke)
    drawCircle(accent, size.width * 0.05f, Offset(size.width * 0.46f, size.height * 0.38f))
    drawCircle(accent, size.width * 0.04f, Offset(size.width * 0.62f, size.height * 0.5f))
    drawCircle(accent, size.width * 0.035f, Offset(size.width * 0.36f, size.height * 0.6f))
}

private fun DrawScope.drawLocationPin(color: Color, center: Offset, radius: Float, strokeWidth: Float) {
    val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawCircle(color, radius, center, style = stroke)
    val point = Path().apply {
        moveTo(center.x - radius * 0.72f, center.y + radius * 0.72f)
        quadraticTo(center.x, center.y + radius * 2.25f, center.x + radius * 0.72f, center.y + radius * 0.72f)
    }
    drawPath(point, color, style = stroke)
    drawCircle(color, radius * 0.32f, center)
}

private fun DrawScope.drawArcIn(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    rect: Rect,
    style: DrawStyle
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(rect.left, rect.top),
        size = Size(rect.width, rect.height),
        style = style
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 780)
@Composable
fun DiscoveryMenuPreview() {
    Menu_android_001Theme {
        DiscoveryMenu()
    }
}
