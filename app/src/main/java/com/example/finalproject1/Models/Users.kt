package com.example.finalproject1.Models

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class Users(var aboutMe :String?,
            var localPlaceOne : String ?,
            var localPlaceTow : String?,
            var productBuy: List<ProductHomeModel>?,
            var userEmail: String?,
            var userImage: String?,
            var userMobile: String?,
            var username :String?

            ):Serializable {
  constructor():this("","","",null,"","","","")



}