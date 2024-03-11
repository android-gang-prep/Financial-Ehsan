package com.example.financialehsan.screens

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financialehsan.LocalAppState
import com.example.financialehsan.screens.components.AppTextField
import com.example.financialehsan.screens.components.BorderButton
import com.example.financialehsan.ui.theme.Green
import com.example.financialehsan.ui.theme.PrimaryVariant
import com.example.financialehsan.utils.formatPrice
import com.example.financialehsan.viewModels.ReminderViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financialehsan.R
import com.example.financialehsan.database.entities.Reminder
import com.example.financialehsan.database.entities.getTimeToPay
import com.google.gson.internal.GsonBuildConfig
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(viewModel: ReminderViewModel = viewModel()) {

    val context = LocalContext.current
    val appState = LocalAppState.current
    val reminders by viewModel.reminders.collectAsState()

    val reminderSheetOpen = remember {
        mutableStateOf(false)
    }
    val selectedReminder = remember {
        mutableStateOf<Reminder?>(null)
    }
    val reminderSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if (it.values.all { it }) {
                reminderSheetOpen.value = true
            }
        }
    )

    val alarmManager = remember {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    if (reminderSheetOpen.value) {
        val editMode = selectedReminder.value != null
        val reminderDescription = remember {
            mutableStateOf(selectedReminder.value?.description.orEmpty())
        }
        val reminderAmount = remember {
            mutableStateOf(selectedReminder.value?.amount?.toString().orEmpty())
        }
        val selectedDayOfMonth = remember {
            mutableIntStateOf(selectedReminder.value?.dayOfMonth ?: 1)
        }

        val datePickerOpen = remember {
            mutableStateOf(false)
        }

        val initialSelectedDayOfMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, selectedDayOfMonth.intValue)
        }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDayOfMonth.timeInMillis,
            initialDisplayedMonthMillis = System.currentTimeMillis(),
        )

        if (datePickerOpen.value) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                DatePickerDialog(
                    onDismissRequest = {
                        datePickerOpen.value = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { pickedDate ->
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = pickedDate
                                selectedDayOfMonth.intValue = calendar.get(Calendar.DAY_OF_MONTH)
                            }
                            datePickerOpen.value = false
                        }) {
                            Text(text = "تنظیم کردن")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            datePickerOpen.value = false
                        }) {
                            Text(text = "بازگشت")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }

        ModalBottomSheet(
            onDismissRequest = {
                reminderSheetOpen.value = false
            },
            sheetState = reminderSheetState,
            containerColor = PrimaryVariant,
            shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                        .navigationBarsPadding()
                ) {
                    Text(
                        text = if (!editMode) "اضافه کردن یادآور" else "ویرایش یادآور",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = reminderAmount.value,
                        onValueChange = {
                            reminderAmount.value = it
                        },
                        placeholder = "مبلغ یادآور (تومان)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "برای چندم هر ماه؟")
                        BorderButton(
                            onClick = {
                                datePickerOpen.value = true
                            },
                            contentPadding = PaddingValues(horizontal = 12.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${selectedDayOfMonth.intValue} ام", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clock),
                                    tint = Color.White,
                                    contentDescription = null,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        height = 200.dp,
                        value = reminderDescription.value,
                        onValueChange = {
                            reminderDescription.value = it
                        },
                        placeholder = "توضیحات یادآور"
                    )
                    Spacer(modifier = Modifier.heightIn(22.dp))
                    Button(onClick = {
                        if (reminderAmount.value.toLongOrNull() != null && reminderDescription.value.isNotEmpty()) {
                            if (editMode) {
                                viewModel.updateReminder(
                                    selectedReminder.value!!.copy(
                                        amount = reminderAmount.value.toLong(),
                                        description = reminderDescription.value,
                                        dayOfMonth = selectedDayOfMonth.intValue
                                    )
                                )
                            } else {
                                viewModel.addReminder(
                                    reminder = Reminder(
                                        amount = reminderAmount.value.toLong(),
                                        description = reminderDescription.value,
                                        dayOfMonth = selectedDayOfMonth.intValue,
                                        nextPayTime = getTimeToPay(selectedDayOfMonth.intValue)
                                    ),
                                    alarmManager = alarmManager,
                                    context = context,
                                    shouldRequestPermission = {
                                        Intent().also {
                                            it.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                                            context.startActivity(it)
                                        }
                                    }
                                )
                            }
                            appState.scope.launch {
                                reminderSheetState.hide()
                            }
                                .invokeOnCompletion {
                                    reminderSheetOpen.value = false
                                }
                        }

                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                        Text(text = if (!editMode) "اضافه کردن" else "ویرایش")
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.BottomStart
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reminders) { reminder ->
                ReminderItem(reminder = reminder, onClick = {
                    selectedReminder.value = reminder
                    reminderSheetOpen.value = true
                }, onLongClick = {
                    viewModel.deleteReminder(
                        reminder = reminder,
                        alarmManager = alarmManager,
                        context = context
                    )
                }, viewModel = viewModel)
            }
        }
        FloatingActionButton(onClick = {
            selectedReminder.value = null
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                )
            )
        }, containerColor = PrimaryVariant) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReminderItem(
    reminder: Reminder,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    viewModel: ReminderViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryVariant
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "مبلغ یادآور:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = reminder.amount.formatPrice(), color = Green)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "تومان", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically,modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "برای ${reminder.dayOfMonth} ام هر ماه")

                    val nextTimeToPay = SimpleDateFormat("yyyy/MM/dd").format(Date(reminder.nextPayTime))
                    Text(text = "زمان پرداخت بعدی: $nextTimeToPay", fontSize = 12.sp, color = Color.White.copy(.6f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = reminder.description)
                val now = Calendar.getInstance()
                val nowMonth = now.get(Calendar.MONTH)
                val nowDay = now.get(Calendar.DAY_OF_MONTH)
                val reminderTimeToPay = Calendar.getInstance().apply { timeInMillis = reminder.nextPayTime }
                val reminderMonthToPay = reminderTimeToPay.get(Calendar.MONTH)
                val reminderDayToPay = reminderTimeToPay.get(Calendar.DAY_OF_MONTH)
                AnimatedVisibility(visible = nowMonth == reminderMonthToPay && nowDay == reminderDayToPay) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            viewModel.payReminder(reminder)
                        }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                            Text(text = "پرداخت")
                        }
                    }
                }
            }

        }

    }
}