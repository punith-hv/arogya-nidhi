package com.arogyanidhi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavHostController) {
    var currentQuestion by remember { mutableStateOf(0) }
    var monthlyIncome by remember { mutableStateOf("") }
    var isBPL by remember { mutableStateOf(false) }

    val questions = listOf(
        "What is your family's monthly income?",
        "Do you have a BPL (Below Poverty Line) Card?"
    )

    Scaffold(topBar = { TopAppBar(title = { Text("Eligibility Quiz") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            LinearProgressIndicator(
                progress = { (currentQuestion + 1) / questions.size.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = questions[currentQuestion],
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (currentQuestion) {
                0 -> {
                    OutlinedTextField(
                        value = monthlyIncome,
                        onValueChange = { monthlyIncome = it },
                        label = { Text("Monthly Income (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                1 -> {
                    Row {
                        RadioButton(selected = isBPL, onClick = { isBPL = true })
                        Text("Yes")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = !isBPL, onClick = { isBPL = false })
                        Text("No")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (currentQuestion < questions.size - 1) {
                        currentQuestion++
                    } else {
                        navController.navigate("result/$monthlyIncome/$isBPL")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (currentQuestion < questions.size - 1) "Next" else "See Results")
            }
        }
    }
}