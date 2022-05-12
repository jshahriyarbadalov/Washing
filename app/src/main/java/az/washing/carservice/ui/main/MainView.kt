package az.washing.carservice.ui.main

interface MainView {
    fun goLogout()
    fun goBooking(id: Int)
    fun goMap(address: String)
    fun showUpdateMessage(message: String)
}