package mainMenuScreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import data.model.Usuario

@Composable
fun MainMenu() {
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
            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                //shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(8.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3C))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.Gray.copy(alpha = 0.40f)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 24.sp)
                    }

                    Text(
                        text = "Tokens: ${userInfo?.tokens ?: 0}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ActionButton("Add tokens") { /* TODO */ }
                        ActionButton("Retire tokens") { /* TODO */ }
                    }
                }
            }

            // User info
            CardSection(title = "User Information") {
                Text(
                    text = "Username: ${userInfo?.username}\nID: ${userInfo?._id}",
                    color = Color.White
                )
            }

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonCard(Modifier.weight(1f),text = "Search a table to join!") {
                    // TODO
                }
                ButtonCard(Modifier.weight(1f),text = "View profile: ${userInfo?.username}") {
                    // TODO
                }
            }
        }
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        //colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E4E68))
    ) {
        Text(text, color = Color.White)
    }
}

@Composable
fun CardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        //shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(6.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3C)),
        modifier = Modifier.fillMaxWidth().background(Color.Gray.copy(alpha = 0.40f)).clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            content()
        }
    }
}

@Composable
fun ButtonCard(modifier: Modifier,text: String, onClick: () -> Unit) {
    Column(
        modifier = modifier.background(Color.Gray.copy(alpha = 0.40f)).clip(RoundedCornerShape(16.dp)),
        //shape = RoundedCornerShape(16.dp),
        //elevation = CardDefaults.cardElevation(6.dp),
        //colors = CardDefaults.cardColors(containerColor = Color(0xFF3B3B5B))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(onClick = onClick)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}
