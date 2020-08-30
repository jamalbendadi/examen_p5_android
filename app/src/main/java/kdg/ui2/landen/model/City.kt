package kdg.ui2.landen.model

data class City(val name:String, val province:String){
    override fun toString(): String {
        return "Name: \n\t$name\nProvince: \n\t$province"
    }
}
