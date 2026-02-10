package com.example.a528_lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import android.util.Log
import androidx.compose.ui.platform.LocalContext

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListScreen()
        }
    }
}

@Composable
fun ListScreen() {
    Column(modifier = Modifier.fillMaxSize().background(Color.Red).padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize().background(Color.Gray).padding(16.dp)) {
            LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
                items(allKantoPokemon) { item ->
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Text(text= item.number.toString())
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text= item.name)

                        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${item.number}.png"

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .listener(
                                    onStart = {
                                        Log.d("AsyncImage", "Start loading: $imageUrl")
                                    },
                                    onError = { _, result ->
                                        Log.e("AsyncImage", "Error loading: $imageUrl", result.throwable)
                                    },
                                    onSuccess = { _, _ ->
                                        Log.d("AsyncImage", "Success loading: $imageUrl")
                                    }
                                )
                                .build(),
                            contentDescription = "Sprite of ${item.name}",
                            modifier = Modifier.size(64.dp),
                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                            error = painterResource(id = R.drawable.ic_launcher_background)
                        )
                    }
                }
            }
        }
    }
}

data class Pokemon(
    val name: String,
    val number: Int
)

val allKantoPokemon = listOf(
    Pokemon("Acheron", 1),
    Pokemon("Aglaea", 2),
    Pokemon("Black Swan", 3),
    Pokemon("Bronya", 4),
    Pokemon("Castorice", 5),
    Pokemon("Cyrence", 6),
    Pokemon("Dan Heng IL", 7),
    Pokemon("Dr.Ratio", 8),
    Pokemon("Firefly", 9),
    Pokemon("Jing Yuan", 10),
    Pokemon("Kafka", 11),
    Pokemon("Lingsha", 12),
    Pokemon("Evernight", 13),
    Pokemon("Phainon", 14),
    Pokemon("Robin", 15),
    Pokemon("Ruan Mei", 16),
    Pokemon("Seele", 17),
    Pokemon("Sunday", 18),
    Pokemon("The Dahlia", 19),
    Pokemon("Fugue", 20),
    Pokemon("Trailblazer", 21),
    Pokemon("Yao Guang", 22),
    Pokemon("Anaxa", 23),
    Pokemon("Archer", 24),
    Pokemon("Ashveil", 25),
    Pokemon("Gepard", 26),
    Pokemon("Cerydra", 27),
    Pokemon("Hyacine", 28),
    Pokemon("Hysilens", 29),
    Pokemon("Mydei", 30),
    Pokemon("Silver Wolf", 31),
    Pokemon("Evanescia", 32),
    Pokemon("Nihilux", 33),
    Pokemon("Sparxie", 34),
    Pokemon("Yunli", 35),
)

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    ListScreen()
}
