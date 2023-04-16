package edu.umich.aehill.reminiscetest

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SingleUser(var userId: String = "", var username: String = ""){
}

class Users(var currentUser: SingleUser? = null, usersArr: MutableList<SingleUser>? = null){
    var allUsers: MutableList<SingleUser>? by UserInfoPropDelegate(usersArr)
}

// for updating the users arr
class UserInfoPropDelegate private constructor ():
    ReadWriteProperty<Any?, MutableList<SingleUser>?> {
    private var _value: MutableList<SingleUser>? = null
        set(newValue) {
            newValue ?: run {
                field = null
                return
            }
            field = if (newValue.isEmpty()) null else newValue
        }

    constructor(initialValue: MutableList<SingleUser>?): this() { _value = initialValue }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = _value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableList<SingleUser>?) {
        _value = value
    }
}