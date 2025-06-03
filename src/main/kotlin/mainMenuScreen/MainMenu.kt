package mainMenuScreen

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.model.BetDocument
import data.model.Usuario
import findGameScreen.FindGameScreen
import userProfileScreen.UserProfileScreen

/**
 * funcion con el contenido del main menu
 */
@Composable
fun MainMenu() {
    val navigator = LocalNavigator.currentOrThrow
    var userInfo by remember { mutableStateOf<Usuario?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        userInfo = ApiData.getUserData().await()
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color(0xFF1E1E2E))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.40f))
                    .clip(RoundedCornerShape(16.dp)),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .clickable {
                            navigator.push(UserProfileScreen())
                        }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 24.sp)
                }

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Tokens: ${userInfo?.tokens ?: 0}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.40f))
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "User Information", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        text = "Username: ${userInfo?.username}\nID: ${userInfo?._id}",
                        color = Color.White
                    )
                }


                ButtonAlgo(Modifier
                    .padding(vertical = 8.dp)
                    .height(48.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                    "View profile: ${userInfo?.username ?: ""}",
                    ){
                    navigator.push(UserProfileScreen())
                }


            }


            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ButtonAlgo(
                    modifier = Modifier.weight(1f),
                    "View profile",
                ){
                    navigator.push(UserProfileScreen())
                }

                ButtonAlgo(
                    modifier = Modifier.weight(1f),
                    "Search a game to join",
                ){
                    navigator.push(FindGameScreen())
                }

            }


            var isLoadingBets by remember { mutableStateOf(false) }
            var listOfBets by remember { mutableStateOf(mapOf<String, List<BetDocument>>()) }

            LaunchedEffect(Unit) {
                isLoadingBets = true
                listOfBets = ApiData.getMyBets().await().groupBy { it.tableId ?: "" }
                isLoadingBets = false
            }

            if (isLoadingBets) {
                CircularProgressIndicator()
            } else {
                DrawBets(listOfBets)
            }
        }
    }
}

