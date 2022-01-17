package br.com.biblioteca.presentation.ext

import androidx.appcompat.widget.AppCompatEditText

const val LINE_SIZE = 40

fun AppCompatEditText.setTextAndFit(text : CharSequence){
    setText(text)
    var lines = text.length.div(LINE_SIZE)+1
    if (lines > maxLines){
        lines = maxLines
    }
    setLines(lines)
    minLines = lines
}