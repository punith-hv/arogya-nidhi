package com.arogyanidhi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arogyanidhi.util.DecisionTree

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavHostController, income: Int, isBPL: Boolean) {
    val schemes = DecisionTree.getEligibleSchemes(income, isBPL)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Your Eligible Schemes") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Based on your details:",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Income: ₹$income | BPL: ${if (isBPL) "Yes" else "No"}")

            Spacer(modifier = Modifier.height(24.dp))

            schemes.forEach { scheme ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(scheme.name, style = MaterialTheme.typography.titleMedium)
                        Text(scheme.description)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("documents") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Required Documents")
            }

            OutlinedButton(
                onClick = { navController.navigate("hospitals") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Find Hospitals")
            }
        }
    }
}