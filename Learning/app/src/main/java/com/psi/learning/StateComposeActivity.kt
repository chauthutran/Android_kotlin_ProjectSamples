package com.psi.learning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import kotlin.math.round

class StateComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                TiptimeLayoutPreview()
            }
        }

    }
}


@Composable
fun TiptimeLayout() {
    var amount: String by remember { mutableStateOf("") }
    var tipPercent: String by remember { mutableStateOf("") }
    var checked: Boolean by remember { mutableStateOf(false) }

    var total by remember { mutableStateOf("0.0") }

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState()) //  enable the column to scroll vertically. The rememberScrollState() creates and automatically remembers the scroll state.
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Calculate Tip",
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )

        EditNumberField (
            value = amount,
            icon = Icons.Default.Menu,
            hint = "Amount",
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ){
            amount = it
        }

        EditNumberField (
            value = tipPercent,
            icon = Icons.Default.Star,
            hint = "% Tip",
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ){
            tipPercent = it
        }


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .size(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Round up tip ?",
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(0.5f)
            )

            Switch(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )

        }

        Text(
            text = "Total : ${total}",
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Button(onClick = {
           total = caculateTip(amount.toDouble(), tipPercent.toDouble(), checked )

        }) {
            Text( "Calculate", fontSize = 24.sp)
        }
    }
}

@Composable
fun EditNumberField(value: String, icon: ImageVector, hint: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit ){
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(text = hint) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            IconButton(onClick = { /* Handle icon click if needed */ }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
    )
}


internal fun caculateTip( amount: Double, tipPercent: Double, isRoundUp: Boolean ) : String {
    var total: Float = ( amount + amount * ( tipPercent/100 )).toFloat()
    if( isRoundUp ) total = round(total)
    return NumberFormat.getCurrencyInstance().format(total)
}

@Preview(showBackground = true, name="TipTime Review")
@Composable
fun TiptimeLayoutPreview() {
    TiptimeLayout()
}