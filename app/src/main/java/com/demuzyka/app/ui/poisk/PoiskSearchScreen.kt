package com.demuzyka.app.ui.poisk

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.demuzyka.app.data.AppContainer
import com.demuzyka.app.data.poisk.Film
import com.demuzyka.app.ui.poisk.parts.FilmPosterTile

/**
 * Poisk "Поиск" screen. Layout follows the screenshot exactly:
 *  * Big rounded search field with filter button on the right.
 *  * "Советуем посмотреть" horizontal row.
 *  * "Смотрите в кино" + chip row "Фильмы / Онлайн-кинотеатр / Жанры…".
 */
@Composable
fun PoiskSearchScreen(container: AppContainer) {
    var query by remember { mutableStateOf("") }
    val results by remember(query) { container.filmProvider.search(query) }
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
    ) {
        item { SearchBar(query) { query = it } }
        if (query.isBlank()) {
            item { SuggestRow(container) }
            item { CinemaRow(container) }
            item { CategoriesGrid() }
        } else {
            items(results) { f -> SearchResultRow(f) }
        }
    }
}

@Composable
private fun SearchBar(query: String, onChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.Search, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = query,
                onValueChange = onChange,
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onBackground),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* no-op */ }),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            "Фильмы, сериалы, пер…",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    inner()
                },
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { /* TODO: filters */ },
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Tune, null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun SuggestRow(container: AppContainer) {
    val rows by remember { container.filmProvider.homeRows() }
        .collectAsState(initial = emptyList())
    val first = rows.firstOrNull() ?: return
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(first.title, modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall)
            Text("Все", color = com.demuzyka.app.ui.theme.DeOrange,
                style = MaterialTheme.typography.labelLarge)
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(first.items) { f -> FilmPosterTile(f) }
        }
    }
}

@Composable
private fun CinemaRow(container: AppContainer) {
    val rows by remember { container.filmProvider.homeRows() }
        .collectAsState(initial = emptyList())
    val cinema = rows.firstOrNull { it.cinema } ?: return
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(cinema.title, modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall)
            Text("Все", color = com.demuzyka.app.ui.theme.DeOrange,
                style = MaterialTheme.typography.labelLarge)
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(cinema.items) { f -> com.demuzyka.app.ui.poisk.parts.FilmCinemaTile(f) }
        }
    }
}

@Composable
private fun CategoriesGrid() {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            "Категории",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            style = MaterialTheme.typography.headlineSmall,
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            val rows = listOf(
                listOf("Фильмы", "Онлайн-кинотеатр"),
                listOf("Жанры", "Страны", "Годы"),
            )
            rows.forEach { row ->
                Row(modifier = Modifier.padding(bottom = 12.dp)) {
                    row.forEach { label ->
                        CategoryPill(label)
                        Spacer(Modifier.width(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryPill(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { /* TODO: open category */ }
            .padding(horizontal = 24.dp, vertical = 14.dp),
    ) {
        Text(label, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun SearchResultRow(f: Film) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: open film */ }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilmPosterTile(f)
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(f.title, style = MaterialTheme.typography.titleLarge)
            Text(
                listOfNotNull(f.year?.toString(), f.genres.firstOrNull()).joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
