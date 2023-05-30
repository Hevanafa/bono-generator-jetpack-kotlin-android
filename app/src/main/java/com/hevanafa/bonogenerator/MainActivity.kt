package com.hevanafa.bonogenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class State (
    val count_str: String = ""
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BonoScaffold()
        }
    }

    private val _uiState = MutableStateFlow(State())
    private val uiStateFlow = _uiState.asStateFlow()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BonoScaffold() {
        val state = uiStateFlow.collectAsState()
        val count_str = state.value.count_str

        val pattern = remember { Regex("^\\d+\$") }

        Scaffold { _innerPadding ->
            Column (
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Bono Generator",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )

                Text("By Hevanafa, 30-05-2023",
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    textAlign = TextAlign.Center)

                Text("Bono count:")

                TextField(
                    value = count_str,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(pattern))
                            _uiState.update {
                                it.copy(count_str = newValue)
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                val count = try {
                    count_str.toInt()
                } catch (nfex: NumberFormatException) {
                    0
                }

                TextField(
                    getBonoStr(count), {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }

    fun getBonoStr(count: Int): String {
        if (count <= 0) return ""

        return (1..count).joinToString(" ") { if ((it and 1) == 1) { "BONK" } else { "BONO" } }
    }
}
