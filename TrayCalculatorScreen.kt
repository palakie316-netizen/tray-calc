package com.edpalakie.freezedrytraycalc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.floor
import kotlin.math.max

private const val TRAYS = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrayCalculatorScreen() {
    val start = remember { List(TRAYS) { mutableStateOf(0.0) } }
    val end = remember { List(TRAYS) { mutableStateOf(0.0) } }

    val portionDryGrams = remember { mutableStateOf(50.0) }   // user can change
    val portionCount = remember { mutableStateOf(10.0) }      // user can change

    val totalStart = start.sumOf { it.value }
    val totalEnd = end.sumOf { it.value }
    val waterRemoved = max(0.0, totalStart - totalEnd)
    val waterRemovedPct = if (totalStart <= 0.0) 0.0 else (waterRemoved / totalStart) * 100.0

    // Simple rehydration estimate:
    // If you add back the water removed, final rehydrated weight ~= starting weight.
    // Water to add (grams) ~= waterRemoved (grams). (1g water ~= 1mL)
    val waterToAddGrams = waterRemoved
    val waterToAddMl = waterRemoved

    val numPortionsBySize = if (portionDryGrams.value <= 0.0) 0.0 else floor(totalEnd / portionDryGrams.value)
    val dryPerPortionByCount = if (portionCount.value <= 0.0) 0.0 else (totalEnd / portionCount.value)
    val waterPerPortionByCount = if (portionCount.value <= 0.0) 0.0 else (waterRemoved / portionCount.value)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Freeze-Dry Tray Calculator") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter weights (grams)", fontWeight = FontWeight.Bold)
                    repeat(TRAYS) { i ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Tray ${i + 1}", modifier = Modifier.weight(0.7f))
                            NumberField(
                                label = "Start g",
                                value = start[i].value,
                                onValue = { start[i].value = it },
                                modifier = Modifier.weight(1f)
                            )
                            NumberField(
                                label = "End g",
                                value = end[i].value,
                                onValue = { end[i].value = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = {
                            start.forEach { it.value = 0.0 }
                            end.forEach { it.value = 0.0 }
                        }) { Text("Clear") }

                        Button(onClick = {
                            // Example quick-fill: copies tray1 to all trays (handy if you do equal loads)
                            val s = start[0].value
                            val e = end[0].value
                            for (i in 1 until TRAYS) {
                                start[i].value = s
                                end[i].value = e
                            }
                        }) { Text("Copy Tray 1 → All") }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Batch totals", fontWeight = FontWeight.Bold)
                    Text("Total start: ${fmt(totalStart)} g")
                    Text("Total end (dry): ${fmt(totalEnd)} g")
                    Text("Water removed: ${fmt(waterRemoved)} g (${fmt(waterRemovedPct)}%)")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Rehydration estimate", fontWeight = FontWeight.Bold)
                    Text("Estimated water to add back: ${fmt(waterToAddGrams)} g (~${fmt(waterToAddMl)} mL)")
                    Text("Rule of thumb: add ~water removed, then adjust to taste/texture.")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Portioning (dry)", fontWeight = FontWeight.Bold)

                    Text("Option A: pick a dry portion size → how many portions?")
                    NumberField(
                        label = "Dry grams per portion",
                        value = portionDryGrams.value,
                        onValue = { portionDryGrams.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Portions you can make: ${fmt(numPortionsBySize)}")

                    Spacer(Modifier.height(6.dp))

                    Text("Option B: pick number of portions → grams per portion + water per portion")
                    NumberField(
                        label = "Number of portions",
                        value = portionCount.value,
                        onValue = { portionCount.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Dry per portion: ${fmt(dryPerPortionByCount)} g")
                    Text("Water per portion: ${fmt(waterPerPortionByCount)} g (~${fmt(waterPerPortionByCount)} mL)")
                }
            }
        }
    }
}

@Composable
private fun NumberField(
    label: String,
    value: Double,
    onValue: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val textState = remember(value) { mutableStateOf(if (value == 0.0) "" else fmt(value)) }
    OutlinedTextField(
        value = textState.value,
        onValueChange = { raw ->
            textState.value = raw
            val cleaned = raw.replace(",", ".").trim()
            onValue(cleaned.toDoubleOrNull() ?: 0.0)
        },
        label = { Text(label) },
        modifier = modifier
    )
}

private fun fmt(v: Double): String = String.format(Locale.US, "%.2f", v)
