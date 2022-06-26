package com.example.picturesshop.utils

class Constants {
    companion object {
        val EMAIL_REGEX_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
        val EMAIL = "email"
        val REGISTRATIONFEEDBACK = "registrationFeedBack"
        val PASSWORDCHANGEFEEDBACK = "passwordChangeFeedBack"
        val SHAREDPREF = "sharedpref"
        val TOKEN = "token"
        val USERNAME = "username"
        val RATING = "rating"
        val AMOUNTTYPE = "amountType"
        val PRICETYPE = "pryceType"
        val pictureID = "pictureId"
        val ISACTIVE = "isActive"
        val PRICEPERUNIT = "pricePerUnit"
        val UNITS = "units"
        val DESCRIPTION = "description"
        val TITLE = "title"
        val FIRSTNAME = "firstName"
        val LASTNAME = "lastName"
        val PASSWORD = "password"
        val ROLE = "role"
        val ID = "id"
        val CONTENT = "content"
        val PICTURENAME = "pictureName"
        val DISCOUNT = "discount"
        val PICTUREID = "pictureId"
        val BIRTHDATE = "birthDate"
        val IMAGEURL = "imageUrl"
        var mediaManagerInitialized = false
    }
}