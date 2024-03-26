package com.psi.learning.days30app

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psi.learning.days30app.models.DayList
import com.psi.learning.days30app.models.OneDay
import com.psi.learning.superheroes.HeroCard
import com.psi.learning.ui.themehero.SuperheroesTheme

@Composable
fun DaysInMonthScreen(
    dayList: List<OneDay>,
    contentPadding: PaddingValues = PaddingValues(8.dp),
) {
    LazyColumn(contentPadding = contentPadding) {
        itemsIndexed(dayList) {index, oneDay ->
            DaysInMonthScreen(
                oneDay = oneDay,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
//                    // Animate each list item to slide in vertically
//                    .animateEnterExit(
//                        enter = slideInVertically(
//                            animationSpec = spring(
//                                stiffness = Spring.StiffnessVeryLow,
//                                dampingRatio = Spring.DampingRatioLowBouncy
//                            ),
//                            initialOffsetY = { it * (index + 1) } // staggered entrance
//                        )
//                    )
            )
        }
    }
}

@Composable
fun DaysInMonthScreen(oneDay: OneDay, modifier: Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Column (
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = "Day ${oneDay.dayIdx}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(id = oneDay.resAction),
                style = MaterialTheme.typography.displaySmall
            )

            Image(
                painter = painterResource(id = oneDay.resImageId),
                contentDescription = null
            )

            Text(
                text = stringResource(id = oneDay.resDescription),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun DaysInMonthScreenReview() {
    SuperheroesTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            DaysInMonthScreen(
                dayList = DayList.days
            )
        }
    }

}