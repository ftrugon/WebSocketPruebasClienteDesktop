package mainMenuScreen

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import data.model.BetDocument
import kotlin.collections.component1
import kotlin.collections.component2


/**
 * funciuon que dibuja todas las apuestas del usuario
 * @param listOfBets todas las apuetas
 */
@Composable
fun DrawBets(listOfBets: Map<String, List<BetDocument>>) {
    val verticalScrollState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.40f))
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        LazyColumn(
            state = verticalScrollState,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.40f))
                .padding(12.dp)
        ) {
            items(listOfBets.entries.toList()) { (tableId, bets) ->
                val horizontalScrollState = rememberLazyListState()

                Column(
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "Table: ${bets.firstOrNull()?.tableName ?: ""}, id: $tableId",
                        style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Box {
                        LazyRow(
                            state = horizontalScrollState,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(bets) { bet ->
                                ItemBox(bet)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        HorizontalScrollbar(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth(),
                            adapter = rememberScrollbarAdapter(scrollState = horizontalScrollState)
                        )
                    }
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = verticalScrollState)
        )
    }
}


