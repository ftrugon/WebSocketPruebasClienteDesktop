package welcomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import loginRegisterScreens.LoginScreen

class WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val noRipple = remember { MutableInteractionSource() }

        // vista del welcome, con su imagen y cosas

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    // Para que el boton no haga cosas raras, el hover o cuando lo pulsas
                    interactionSource = noRipple,
                    indication = null
                ) {
                    navigator.push(LoginScreen())
                }
        ) {
            Image(
                painter = painterResource("background_image_welcome_screen.png"),
                contentDescription = "Fondo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource("icon_image.png"),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .scale(0.90f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))
                        .weight(3f),
                    alignment = Alignment.Center,
                )
                Text(
                    text = "Welcome back, click anywhere to continue!",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 16.dp)
                        .fillMaxWidth()
                )

            }
        }
    }
}
