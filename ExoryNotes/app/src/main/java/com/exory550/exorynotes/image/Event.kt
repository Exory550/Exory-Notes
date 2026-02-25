package com.exory550.exorynotes.image

class Event<T>(val data: T) {

    private var isHandled = false

    fun handle(function: (data: T) -> Unit) {
        if (!isHandled) {
            function(data)
            isHandled = true
        }
    }
}
