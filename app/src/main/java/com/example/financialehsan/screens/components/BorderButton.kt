package com.example.financialehsan.screens.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BorderButton(onClick:()->Unit, onLongClick:()->Unit = {}, contentPadding:PaddingValues = PaddingValues(6.dp), content: @Composable ()->Unit) {
    Row(modifier = Modifier
        .height(32.dp)
        .clip(RoundedCornerShape(6.dp))
        .border(
            1.dp,
            Color.White.copy(.6f),
            RoundedCornerShape(6.dp),
        )
        .combinedClickable (
            onClick = onClick,
            onLongClick = onLongClick
        )
        .padding(contentPadding)
        , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
        content()
    }
}