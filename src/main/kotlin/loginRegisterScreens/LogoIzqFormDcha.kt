package loginRegisterScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * funcion que tiene el logo a la izquierda y el registro o login a la derecha, tambien teien eun fondo
 * @param imgFondo la imagen de fondo que tendra la pantalla
 * @param content el contenido de el formulario de la derecha
 */
@Composable
fun LogoIzqFormDcha(
    imgFondo: String,
    content: @Composable () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(imgFondo),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LOGO al centro del eje vertical
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(4f),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource("icon_image.png"),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .scale(0.30f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))

                )
            }

            // FORMULARIO pegado a la derecha
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    //.width(350.dp)
                    .weight(2f),
                //  .padding(24.dp)

                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.40f))
                        .padding(24.dp)
                        .fillMaxSize()
                ) {
                    content()
                }

            }
        }
    }
}