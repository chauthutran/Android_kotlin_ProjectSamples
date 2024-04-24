package com.psi.learning.replyapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.reply.ui.ReplyApp
import com.psi.learning.R
import com.psi.learning.ui.theme.WoofTheme

class ReplyAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WoofTheme {
                Surface {
                    ReplyApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReplyAppCompactPreview() {
    WoofTheme {
        Surface {
            ReplyApp()
        }
    }
}