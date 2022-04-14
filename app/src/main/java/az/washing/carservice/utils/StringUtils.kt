package az.washing.carservice.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class StringUtils {
    companion object {
        private const val PHONE_COUNTRY_CODE = "994"
        private const val PHONE_WITH_COUNTRY_CODE = 12
        private const val PHONE_WITH_OPERATOR_CODE = 10
        private const val PHONE_OPERATOR_CODE = 9

        fun getPhone(phone: String): Boolean {

            val pattern: Pattern =
                Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{2}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
            val matcher: Matcher = pattern.matcher(phone)
            return matcher.matches()
        }

        fun phoneEdit(phoneNew: String): String {
            var newPhone = phoneNew
            if (newPhone.length > PHONE_OPERATOR_CODE && newPhone.length == PHONE_WITH_OPERATOR_CODE) {
                newPhone = newPhone.substring(1, newPhone.length)
            } else if (newPhone.length > PHONE_OPERATOR_CODE && newPhone.length == PHONE_WITH_COUNTRY_CODE) {
                newPhone = newPhone.substring(3, newPhone.length)
            }
            return PHONE_COUNTRY_CODE + newPhone
        }
    }
}