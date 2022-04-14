package az.washing.carservice.ui.order

interface BookingView {
    fun showSuccessMessage(message:String)
    fun getTimeText(timeText: String)
    fun goBack()
}