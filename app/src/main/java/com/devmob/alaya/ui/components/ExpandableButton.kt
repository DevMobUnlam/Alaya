import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun ExpandableButton() {
    var isExpanded by remember { mutableStateOf(false) }

    val buttonWidth by animateDpAsState(if (isExpanded) 180.dp else 50.dp, label = "")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(buttonWidth)
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(ColorQuaternary)
                .clickable { isExpanded = !isExpanded },
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_music),
                contentDescription = "Main Button",
                tint = ColorText,
                modifier = if (isExpanded) Modifier.padding(start = 16.dp) else Modifier.align(
                    Alignment.Center
                )
            )

            if (isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 8.dp, end = 8.dp)
                        .align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically
                ) {
                    var isMusicPlaying by remember { mutableStateOf(true) }
                    IconButton(
                        onClick = { isMusicPlaying = !isMusicPlaying },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isMusicPlaying) R.drawable.music_on else R.drawable.music_off),
                            contentDescription = "Icon 1",
                            tint = ColorText
                        )
                    }
                    var isVoiceOn by remember { mutableStateOf(true) }
                    IconButton(
                        onClick = { isVoiceOn = !isVoiceOn }, modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isVoiceOn) R.drawable.voice_on else R.drawable.voice_off),
                            contentDescription = "Icon 2",
                            tint = ColorText
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun ExpandableButtonPreview() {
    ExpandableButton()
}