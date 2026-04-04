package com.example.festimob.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.festimob.data.api.User
import com.example.festimob.ui.AppViewModelProvider
import com.example.festimob.ui.festival.ErrorView
import com.example.festimob.ui.festival.LoadingView

@Composable
fun AdminScreen(
    viewModel: AdminScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.refreshUsers()
    }

    when (state) {
        is AdminScreenState.Loading -> LoadingView()
        is AdminScreenState.Error -> ErrorView(message = (state as AdminScreenState.Error).message)
        is AdminScreenState.Success -> AdminUsersList(users = (state as AdminScreenState.Success).users)
    }
}

@Composable
private fun AdminUsersList(users: List<User>) {
    LazyColumn {
        items(items = users, key = { user -> user.id_u }) { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user.role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
