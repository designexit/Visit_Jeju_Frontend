package com.example.visit_jeju_app.login.model

data class User(
    var username: String,
    var email: String,
    var uid: String,
    var password: String,
    var profile_id: String
){

    constructor(): this("","","")
}
