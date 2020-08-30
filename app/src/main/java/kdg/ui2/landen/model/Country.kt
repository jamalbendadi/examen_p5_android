package kdg.ui2.landen.model

import android.graphics.Bitmap

data class Country(val id:Int,val name:String,val fullname:String,val dateOfIndependence : String,val populationInMillions:Int,val image:String,var flagBitmap:Bitmap ) {}
