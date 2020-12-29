package com.example.finalproject1.Models

import java.io.Serializable

class ProductHomeModel(var id:String,var product_img_model:String?,var product_text_model:String?) :
    Serializable {
    constructor():this("","","")

}