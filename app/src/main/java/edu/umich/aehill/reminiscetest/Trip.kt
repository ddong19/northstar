package edu.umich.aehill.reminiscetest
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class TripImage(var imageId: String?,
                var tripId: String?,
                var coords: String? ,
                var URI: String){
}


// trip id, user id, destination, start date, end date, username, description
// [209, 3, "cancun", "03162023", "03192023", "alannaemmrie", "test trip description"],

class Trip(var tripId: String? = null,
           var userId: String? = null,
           var destination: String? = null,
           var startDate: String? = null,
           var endDate: String? = null,
           var ownerUsername: String? = null,
           var description: String? = null,
           var friends: String? = "",
           tripImages: MutableList<TripImage>? = null) {

    var imageURIs: MutableList<TripImage>? by TripPropDelegate(tripImages)
}

// pretty sure this just lets us change the image URIs - not sure if needed
class TripPropDelegate private constructor ():
    ReadWriteProperty<Any?, MutableList<TripImage>?> {
    private var _value: MutableList<TripImage>? = null
        set(newValue) {
            newValue ?: run {
                field = null
                return
            }
            field = if (newValue.isEmpty()) null else newValue
        }

    constructor(initialValue: MutableList<TripImage>?): this() { _value = initialValue }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = _value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableList<TripImage>?) {
        _value = value
    }
}