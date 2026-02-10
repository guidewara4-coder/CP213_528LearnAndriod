package com.example.a528_lablearnandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RPGCardView()
        }
    }
    override fun onStart() {
        super.onStart()
        Log.i("Lifecycle", "MainActivity : onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Lifecycle", "MainActivity : onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Lifecycle", "MainActivity : onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Lifecycle", "MainActivity : onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Lifecycle", "MainActivity : onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("Lifecycle", "MainActivity : onRestart")
    }

    @Composable
    fun RPGCardView() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
                .padding(32.dp)) {
            // hp
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(color = Color.White)
            ) {
                Text(
                    text = "hp",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterStart)
                        .fillMaxWidth(fraction = 0.55f)
                        .background(color = Color.DarkGray)
                        .padding(8.dp)
                )
            }
            // image
            Image(
                painter = painterResource(R.drawable.aglaea),
                contentDescription = "My Image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 32.dp)
                    .clickable {
                        startActivity(Intent(this@MainActivity, ListActivity::class.java))
                    }
            )

            var str by remember { mutableStateOf(8) }
            var agi by remember { mutableStateOf(10) }
            var int by remember { mutableStateOf(15) }
            // status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Button(onClick = {
                        str = str + 1
                    }) {
                        Text(text = "+", fontSize = 32.sp)
                    }
                    Text(text = "Attack", fontSize = 32.sp)
                    Text(text = str.toString(), fontSize = 32.sp)
                    Text(text = "-", fontSize = 32.sp, modifier = Modifier.clickable {
                        str = str - 1
                    })
                }
                Column {
                    Button(onClick = {
                        agi = agi + 1
                    }) {
                        Text(text = "+", fontSize = 32.sp)
                    }
                    Text(text = "Defense", fontSize = 32.sp)
                    Text(text = agi.toString(), fontSize = 32.sp)
                    Text(text = "-", fontSize = 32.sp, modifier = Modifier.clickable {
                        agi = agi - 1
                    })
                }
                Column {
                    Button(onClick = {
                        int = int + 1
                    }) {
                        Text(text = "+", fontSize = 32.sp)
                    }
                    Text(text = "Speed", fontSize = 32.sp)
                    Text(text = int.toString(), fontSize = 32.sp)
                    Text(text = "-", fontSize = 32.sp, modifier = Modifier.clickable {
                        int = int - 1
                    })
                }
            }
        }
    }

    @Preview
    @Composable
    fun previewScreen() {
        RPGCardView()
    }
}