package az.washing.carservice.ui.auth

interface AuthView {
    fun otpCode()
    fun nextPage()
    fun goRegister()
    fun showErrorMessage(message: String)
}