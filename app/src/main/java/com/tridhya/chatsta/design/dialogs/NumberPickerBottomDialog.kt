package com.tridhya.chatsta.design.dialogs

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.tridhya.chatsta.R
import com.tridhya.chatsta.base.ActivityBase
import com.tridhya.chatsta.design.BaseBottomSheetDialogFragment


class NumberPickerBottomDialog(context: Context) :
    BaseBottomSheetDialogFragment(), View.OnClickListener {

    companion object {
        private lateinit var listener: OptionListener
        var selectedValue = 0

        fun newInstance(
            context: Context,
            currentValue: Int,
            listener: OptionListener,
        ): NumberPickerBottomDialog {
            this.listener = listener
            this.selectedValue = currentValue
            return NumberPickerBottomDialog(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_number_picker, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberPicker = view.findViewById(R.id.numberPicker) as NumberPicker
        val btnOk = view.findViewById(R.id.btnOk) as AppCompatTextView

        numberPicker.maxValue = 305
        numberPicker.minValue = 50
        numberPicker.wrapSelectorWheel = false
        numberPicker.value = selectedValue

        btnOk.setOnClickListener(this)

        numberPicker.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(numberPicker: NumberPicker?, oldValue: Int, newValue: Int) {
                selectedValue = newValue
            }

        })
    }

    override fun onClick(view: View) {
        (context as ActivityBase).preventDoubleClick(view)
        dismissDialog()
        listener.onOptionSelected(view, selectedValue)
    }

    interface OptionListener {
        fun onOptionSelected(view: View, selectedValue: Int)
    }
}