package com.example.nychighschools.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nychighschools.data.model.NYCHighSchools
import com.example.nychighschools.util.Result
import com.example.nychighschools.viewmodel.NYCSchoolViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighSchoolList() {
    val nycSchoolViewModel: NYCSchoolViewModel = viewModel()
    val state by nycSchoolViewModel.nycSchoolsData.collectAsState(initial = Result.Loading)
    val refreshing by nycSchoolViewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = refreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { nycSchoolViewModel.refreshSchoolData() }) {

        when (state) {
            is Result.Loading -> MyCircularProgressScreen()

            is Result.Success -> {
                val schools =
                    (state as Result.Success<List<NYCHighSchools>>).data
                if (schools.isNullOrEmpty()) {
                    NoSchoolsError()
                } else {
                    SchoolsList(schools)
                }
            }

            is Result.Error -> LoadError()
        }
    }
}

@Composable
fun LoadError() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        text = "Failed to load schools",
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun NoSchoolsError() {
    Text(
        "No schools data available. Please try again later.",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun SchoolsList(schools: List<NYCHighSchools>) {
    LazyColumn {
        items(schools) { school ->
            SchoolItem(school)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolItem(school: NYCHighSchools) {
    var expand by rememberSaveable {
        mutableStateOf(false)
    }
    val rotationState by animateFloatAsState(
        targetValue = if (expand) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = { expand = !expand }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = school.schoolName ?: "No School Name Available",
                        style = MaterialTheme.typography.bodyLarge,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (!school.numOfSatTestTakers.isNullOrEmpty()) "Number of SAT takers: ${school.numOfSatTestTakers}" else "No SAT Takers",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(
                    onClick = { expand = !expand },
                    modifier = Modifier.rotate(rotationState)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expand) {
                Text(
                    text = if (!school.satMathAvgScore.isNullOrEmpty()) "Math Score: ${school.satMathAvgScore}" else "No Math Score Available",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (!school.satCriticalReadingAvgScore.isNullOrEmpty()) "Reading Score: ${school.satCriticalReadingAvgScore}" else "No Reading Score Available",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (!school.satWritingAvgScore.isNullOrEmpty()) "Writing Score: ${school.satWritingAvgScore}" else "No Writing Score Available",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MyCircularProgressScreen() {
    Box(contentAlignment = Alignment.Center) {
        // Adjust the size by changing the size parameter
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp), // Adjust the size as needed
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}