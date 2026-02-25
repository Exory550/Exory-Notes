package com.exory550.exorynotes.room.livedata

import androidx.lifecycle.LiveData
import com.exory550.exorynotes.room.BaseNote
import com.exory550.exorynotes.room.Item

class Content(liveData: LiveData<List<BaseNote>>, transform: (List<BaseNote>) -> List<Item>) : LiveData<List<Item>>() {

    init {
        liveData.observeForever { list -> value = transform(list) }
    }
}
