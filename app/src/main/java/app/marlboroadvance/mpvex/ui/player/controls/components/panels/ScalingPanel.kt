package app.marlboroadvance.mpvex.ui.player.controls.components.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import app.marlboroadvance.mpvex.ui.player.PlayerViewModel
import app.marlboroadvance.mpvex.ui.player.controls.CARDS_MAX_WIDTH
import app.marlboroadvance.mpvex.ui.player.controls.panelCardsColors
import app.marlboroadvance.mpvex.ui.theme.spacing

@Composable
fun ScalingPanel(
    viewModel: PlayerViewModel,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaleX by viewModel.videoScaleX.collectAsState()
    val scaleY by viewModel.videoScaleY.collectAsState()

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium),
    ) {
        val settingsCard = createRef()

        Card(
            modifier = Modifier
                .constrainAs(settingsCard) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .widthIn(max = CARDS_MAX_WIDTH),
            colors = panelCardsColors(),
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Custom Scaling",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Vertical Scaling (Y)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Vertical Scale (Y)", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalIconButton(onClick = { viewModel.setVideoScaleY(scaleY - 0.01f) }) {
                            Icon(Icons.Default.Remove, "Decrease Height")
                        }
                        Text(text = "%.2f".format(scaleY))
                        FilledTonalIconButton(onClick = { viewModel.setVideoScaleY(scaleY + 0.01f) }) {
                            Icon(Icons.Default.Add, "Increase Height")
                        }
                    }
                }

                // Horizontal Scaling (X)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Horizontal Scale (X)", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalIconButton(onClick = { viewModel.setVideoScaleX(scaleX - 0.01f) }) {
                            Icon(Icons.Default.Remove, "Decrease Width")
                        }
                        Text(text = "%.2f".format(scaleX))
                        FilledTonalIconButton(onClick = { viewModel.setVideoScaleX(scaleX + 0.01f) }) {
                            Icon(Icons.Default.Add, "Increase Width")
                        }
                    }
                }
                
                // Fill Screen Mode
                 Button(
                    onClick = { viewModel.setVideoScaleFill() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Fill Screen Mode")
                }
                
                // Reset Button
                Button(
                    onClick = { viewModel.resetVideoScale() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Reset Scaling")
                }
            }
        }
    }
}
