package edu.umich.aehill.reminiscetest
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


// trip image class for current trip image arr
class TripImage(var imageId: String?,
                var tripId: String?,
                var coords: String? ,
                var URI: String){
}

// trip class, represents the trips stored in the DB
class Trip(var tripId: String? = null,
           var userId: String? = null,
           var destination: String? = null,
           var startDate: String? = null,
           var endDate: String? = null,
           var ownerUsername: String? = null,
           var description: String? = null,
           var friends: String? = "",
           tripFriend1Images: MutableList<TripImage>? = null,
           tripFriend2Images: MutableList<TripImage>? = null,
           tripImages: MutableList<TripImage>? = null,
           var thumbnailUri: String? = null) {

    var imageURIs: MutableList<TripImage>? by TripPropDelegate(tripImages)
    var friendOneImageURIs: MutableList<TripImage>? by TripPropDelegate(tripFriend1Images)
    var friendTwoImageURIs: MutableList<TripImage>? by TripPropDelegate(tripFriend2Images)


}

// for updating trip images & friend trip images arrs
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