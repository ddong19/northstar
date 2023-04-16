package edu.umich.aehill.reminiscetest

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SpotifySong(var artist: String, var name: String, var songImageURI: String? = null){}

class Spotify(var playlistName: String? = null, var songs: MutableList<SpotifySong>? = null) {}

// for updating song arr
class SpotifyPropDelegate private constructor ():
    ReadWriteProperty<Any?, MutableList<SpotifySong>?> {
    private var _value: MutableList<SpotifySong>? = null
        set(newValue) {
            newValue ?: run {
                field = null
                return
            }
            field = if (newValue.isEmpty()) null else newValue
        }

    constructor(initialValue: MutableList<SpotifySong>?): this() { _value = initialValue }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = _value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableList<SpotifySong>?) {
        _value = value
    }
}