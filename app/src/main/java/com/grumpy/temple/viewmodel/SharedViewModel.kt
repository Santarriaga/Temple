package com.grumpy.temple.viewmodel

import androidx.lifecycle.ViewModel
import com.grumpy.temple.models.Item

class SharedViewModel : ViewModel() {

   val cart = mutableListOf<Item>()

}