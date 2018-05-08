package com.jason.workdemo.kotlin

import java.io.Serializable

class User : Serializable {
    var name: String? = null
    var id: String? = null

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, id: String) {
        this.name = name
        this.id = id
    }
}