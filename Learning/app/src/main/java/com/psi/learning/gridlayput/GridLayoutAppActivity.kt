package com.psi.learning.gridlayput

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.learning.ui.theme.WoofTheme
import com.psi.learning.R
import com.psi.learning.gridlayput.data.DataSource
import com.psi.learning.gridlayput.data.GridItem

class GridLayoutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WoofTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GridLayoutApp()
                }
            }
        }

    }
}

@Composable
fun GridLayoutApp() {
    GridLayout(
        dataItemList = DataSource.topics
    )
}


@Composable
private fun GridLayout( dataItemList : List<GridItem>, modifier: Modifier = Modifier ) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_grid_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_grid_small))
    ) {
        items(dataItemList) { gridItem ->
            GridItemLayout(
                item = gridItem
            )
        }
    }
}


@Composable
private fun GridItemLayout(item: GridItem) {
    Card {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {

                Image(
                    painter = painterResource(id = item.imageResourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(68.dp)
                        .height(68.dp)
                        .aspectRatio(1f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
            ) {
                Text(
                    text = stringResource(id = item.title),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_grain),
                        contentDescription = null,
                    )

                    Text(
                        text = item.availableCourses.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }


            }

        }
    }
}


@Preview
@Composable
private fun GridLayoutPreview() {
    GridLayoutApp()
}