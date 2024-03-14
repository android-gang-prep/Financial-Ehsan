package com.example.financialehsan.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectableButton(text: String, selected: Boolean, onSelect: () -> Unit,testTag:String? = null, onLongClick:()->Unit) {
    BorderButton(
        contentPadding = PaddingValues(horizontal = 16.dp), onClick = onSelect, onLongClick = onLongClick, testTag = testTag
    ) {
        Text(text = text, fontSize = 13.sp)
        AnimatedVisibility(visible = selected) {
            Row {
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier=Modifier.size(20.dp),
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null
                )
            }
        }
    }
}