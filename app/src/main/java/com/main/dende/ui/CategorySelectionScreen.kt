package com.main.dende.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.main.dende.ui.component.DefaultButton

@Composable
fun CategorySelectionScreen(
    allCategories: List<String>,
    selectedCategories: List<String>,
    onCategoryToggle: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text("Select Categories", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        allCategories.forEach { category ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .toggleable(
                        value = selectedCategories.contains(category),
                        onValueChange = { onCategoryToggle(category) }
                    )
            ) {
                Checkbox(
                    checked = selectedCategories.contains(category),
                    onCheckedChange = null // Handled by toggleable modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(category)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        DefaultButton(onClick = onConfirm, enabled = selectedCategories.isNotEmpty()) {
            Text("Continue")
        }
    }
}
