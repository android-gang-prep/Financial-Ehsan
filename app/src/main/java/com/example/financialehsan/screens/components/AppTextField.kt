package com.example.financialehsan.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.financialehsan.ui.theme.Primary
import com.example.financialehsan.ui.theme.diroozFont

const val APP_TEXT_FIELD_DEFAULT_HEIGHT:Int = 56

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    height:Dp = APP_TEXT_FIELD_DEFAULT_HEIGHT.dp,
    value:String,
    onValueChange:(String)->Unit,
    placeholder:String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Box(modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .height(height)
        .background(Primary)
    ){
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier= Modifier.fillMaxSize(),
            placeholder = {
                Text(text = placeholder, fontFamily = diroozFont)
            },
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
    }
}