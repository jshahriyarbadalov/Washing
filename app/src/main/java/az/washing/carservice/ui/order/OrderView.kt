package az.washing.carservice.ui.order

interface OrderView {
    fun editOrder(orderId: Int)
    fun cancelOrder(orderId: Int)
    fun showSuccessMessage(message: String)

}