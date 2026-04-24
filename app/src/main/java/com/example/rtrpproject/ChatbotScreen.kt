package com.example.rtrpproject

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

@Composable
fun ChatbotScreen(navController: NavHostController) {
    var userInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                text = "Hi, I am your Health Assistant. Ask me about wellness, food, water, exercise, or general health tips.",
                isUser = false
            )
        )
    }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isLoading) {
        val extraItem = if (isLoading) 1 else 0
        val lastIndex = messages.size + extraItem - 1
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8F2))
            .padding(12.dp)
    ) {
        Text(
            text = "Health Chatbot 🤖",
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "General wellness information only. Not a substitute for medical advice.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }

            if (isLoading) {
                item {
                    ChatBubble(
                        message = ChatMessage(
                            text = "Thinking...",
                            isUser = false
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                label = { Text("Type a message") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val text = userInput.trim()
                    if (text.isNotEmpty() && !isLoading) {
                        messages.add(ChatMessage(text = text, isUser = true))
                        userInput = ""
                        isLoading = true

                        sendMessageToBackend(text) { reply ->
                            isLoading = false
                            messages.add(ChatMessage(text = reply, isUser = false))
                        }
                    }
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Send")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate("healthhome") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7E57C2)
            )
        ) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (message.isUser) Color(0xFF7E57C2) else Color.White,
            shape = RoundedCornerShape(18.dp),
            tonalElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else Color.Black,
                modifier = Modifier.padding(12.dp),
                fontSize = 16.sp
            )
        }
    }
}

fun sendMessageToBackend(message: String, onResult: (String) -> Unit) {
    val client = OkHttpClient()

    val json = JSONObject().apply {
        put("message", message)
    }

    val body = json.toString()
        .toRequestBody("application/json".toMediaType())

    val request = Request.Builder()
        .url("http://10.70.162.115:8001/chat")
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Handler(Looper.getMainLooper()).post {
                onResult("Failed: ${e.message}")
            }
        }

        override fun onResponse(call: Call, response: Response) {
            val res = response.body?.string().orEmpty()

            val reply = try {
                JSONObject(res).getString("reply")
            } catch (e: Exception) {
                "Invalid response from backend"
            }

            Handler(Looper.getMainLooper()).post {
                onResult(reply)
            }
        }
    })
}