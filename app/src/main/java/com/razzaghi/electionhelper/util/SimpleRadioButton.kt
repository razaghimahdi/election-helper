package com.razzaghi.electionhelper.util

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.radiobutton.MaterialRadioButton

class SimpleRadioButton: MaterialRadioButton {

    constructor(context: Context?) : super(context!!){init()}

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs){init()}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ){init()}

    fun init(){
        if (!isInEditMode){
            val tf = Typeface.createFromAsset(context.assets, "fonts/vazir.ttf");
            this.typeface = tf;
        }
    }


}