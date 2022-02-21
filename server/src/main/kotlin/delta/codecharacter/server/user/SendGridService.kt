package delta.codecharacter.server.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Collections
import java.util.UUID

@Suppress("NAME_SHADOWING")
@Service
class SendGridService {

    @Autowired private lateinit var sendGrid: SendGrid

    @Value("\${spring.sendgrid.api-key}") private lateinit var key: String

    @Value("\${spring.sendgrid.template-id}") private lateinit var templateId: String

    @Value("\${spring.sendgrid.sender-email}") private lateinit var senderEmail: String

    @Value("\${base-url}") private lateinit var BASE_URL: String
    fun activateUserEmail(userId: UUID, token: String, name: String, email: String) {
        val link = "$BASE_URL/activate?id=$userId&token=$token"
        val linkName = "User Activation link"
        val message = "Please! CLick the button to acitvate your account"
        val buttonName = "Activate"
        val subjectInfo = "CodeCharacter Account Activation Link"
        sendTemplateEmail(email, name, linkName, link, message, buttonName, subjectInfo)
    }

    fun forgotPasswordEmail(userId: UUID, token: String, name: String, email: String) {
        val link = "$BASE_URL/reset-password?id=$userId&token=$token"
        val linkName = "Reset-Password link"
        val message = "Please! CLick the button to reset your password"
        val buttonName = "Reset Password"
        val subjectInfo = "CodeCharacter Reset password Link"
        sendTemplateEmail(email, name, linkName, link, message, buttonName, subjectInfo)
    }

    fun sendTemplateEmail(
        emailTo: String,
        name: String,
        linkName: String,
        link: String,
        message: String,
        buttonName: String,
        subjectInfo: String
    ) {
        val emailFrom = Email(senderEmail)
        val emailTo = Email(emailTo)
        val mail = Mail()
        val personalization = DynamicTemplatePersonalization()
        personalization.addTo(emailTo)
        mail.setFrom(emailFrom)
        mail.setSubject(subjectInfo)
        personalization.addDynamicTemplateData("name", name)
        personalization.addDynamicTemplateData("link_name", linkName)
        personalization.addDynamicTemplateData("message", message)
        personalization.addDynamicTemplateData("link", link)
        personalization.addDynamicTemplateData("button_name", buttonName)
        personalization.addDynamicTemplateData("subject", subjectInfo)
        mail.addPersonalization(personalization)
        mail.setTemplateId(templateId)
        val sendGrid = SendGrid(key)
        val request = Request()
        try {
            request.apply {
                method = Method.POST
                endpoint = "mail/send"
                body = mail.build()
            }
            val response: Response = sendGrid.api(request)
        } catch (e: Exception) {
            throw Exception("Error occurred while sending email:" + e.message)
        }
    }
}

class DynamicTemplatePersonalization() : Personalization() {
    @JsonProperty(value = "dynamic_template_data")
    private var dynamicTemplateData: HashMap<String, String>? = null

    @JsonProperty("dynamic_template_data")
    override fun getDynamicTemplateData(): Map<String, String> {
        return dynamicTemplateData ?: (Collections.emptyMap())
    }

    fun addDynamicTemplateData(key: String, value: String) {
        if (dynamicTemplateData == null) {
            dynamicTemplateData = HashMap()
            dynamicTemplateData!![key] = value
        } else {
            dynamicTemplateData!![key] = value
        }
    }
}
