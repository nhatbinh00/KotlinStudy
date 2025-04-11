package com.example.internalsdk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.internalsdk.ui.theme.KotlinKnowledgeTheme

class InternalSDKActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinKnowledgeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android123",
                        onPop = {
                            val resultIntent = Intent()
                            resultIntent.putExtra("result_key", "This is the result string")
                            setResult(RESULT_OK, resultIntent)
                            finish() // This pops the activity
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String,onPop : () -> Unit, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Hello") }

    Column {
        Text(
            text = "Hello $name!",
            modifier = Modifier
                .padding(top = 116.dp)
                .fillMaxSize(),
            color = Color.Red
        )
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )

        Button(onClick = {
           onPop()
        }) {
            Text("Filled")
        }

    }
}
