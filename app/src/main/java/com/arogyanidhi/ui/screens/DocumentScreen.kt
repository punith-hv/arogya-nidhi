package com.arogyanidhi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(navController: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Document Checklist") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Please keep these documents ready:", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            val docs = listOf(
                "Aadhaar Card (All family members)",
                "BPL Card / Ration Card",
                "Income Certificate",
                "Bank Account Passbook",
                "Recent Passport Size Photo"
            )

            docs.forEach { doc ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text(
                        text = "• $doc",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Back to Home")
            }
        }
    }
}