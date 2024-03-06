package com.example.workoutstatistics.ui.gallery

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutstatistics.R


@Composable
fun ChipGroupCompose(context: Context): String {

    var exerArray = context.getResources().getStringArray(R.array.exercises);
    var selected by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(start = 10.dp,top = 10.dp)
            .fillMaxWidth().horizontalScroll(ScrollState(0))
    ) {
        exerArray.forEach { it ->
            Chip(
                title = it,
                selected = selected,
                onSelected = {
                    selected = it
                }
            )
        }
    }
return selected
}


@Composable
fun Chip(
    title: String,
    selected: String,
    onSelected: (String) -> Unit
) {

    val isSelected = selected == title

    val myColor: Color = Color(0xFF7FDF92)
    val myColor2: Color = Color(0xFF84E0C4)
    val background = if (isSelected) myColor else myColor2//0x7FDF92 else 0x84E0C4
    val contentColor = if (isSelected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .padding(end = 10.dp)
            .height(35.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(
                onClick = {
                    onSelected(title)
                }
            )
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            AnimatedVisibility(visible = isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "check",
                    tint = Color.White
                )
            }

            Text(text = title, color = contentColor, fontSize = 16.sp)

        }
    }

}
