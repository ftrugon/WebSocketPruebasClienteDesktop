package gameScreen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import gameScreen.wsComm.PlayerInfoMessage

class GameScreen(val idTable: String, val playerInfo: PlayerInfoMessage, val tableTitle: String) : Screen {
    @Composable
    override fun Content() {
        App(idTable, playerInfo,tableTitle)
    }
}
