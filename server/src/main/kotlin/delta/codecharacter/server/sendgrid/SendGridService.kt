package delta.codecharacter.server.sendgrid

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import delta.codecharacter.server.exception.CustomException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SendGridService {

    private val logger: Logger = LoggerFactory.getLogger(SendGridService::class.java)

    @Autowired private lateinit var sendGrid: SendGrid

    @Value("\${spring.sendgrid.template-id}") private lateinit var templateId: String

    @Value("\${spring.sendgrid.sender-email}") private lateinit var senderEmail: String

    @Value("\${base-url}") private lateinit var baseUrl: String

    fun activateUserEmail(userId: UUID, token: String, name: String, email: String) {
        val link = "$baseUrl/activate?id=$userId&token=$token"
        val linkName = "User Activation link"
        val message = "Please click the button to activate your account"
        val buttonName = "Activate"
        val subjectInfo = "CodeCharacter Account Activation Link"
        sendTemplateEmail(email, name, linkName, link, message, buttonName, subjectInfo)
    }

    fun resetPasswordEmail(userId: UUID, token: String, name: String, email: String) {
        val link = "$baseUrl/reset-password?id=$userId&token=$token"
        val linkName = "Reset-Password link"
        val message = "Please click the button to reset your password"
        val buttonName = "Reset Password"
        val subjectInfo = "CodeCharacter Reset-Password Link"
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
        val mail = Mail()
        val personalization = DynamicTemplatePersonalization()
        personalization.addTo(Email(emailTo))
        mail.setFrom(Email(senderEmail))
        mail.setSubject(subjectInfo)
        personalization.addDynamicTemplateData("name", name)
        personalization.addDynamicTemplateData("link_name", linkName)
        personalization.addDynamicTemplateData("message", message)
        personalization.addDynamicTemplateData("link", link)
        personalization.addDynamicTemplateData("button_name", buttonName)
        personalization.addDynamicTemplateData("subject", subjectInfo)
        mail.addPersonalization(personalization)
        mail.setTemplateId(templateId)
        val request = Request()
        try {
            request.apply {
                method = Method.POST
                endpoint = "mail/send"
                body = mail.build()
            }
            val response: Response = sendGrid.api(request)
            if (response.statusCode >= 400) {
                logger.error(
                    "Error while sending email with status: ${response.statusCode}. Error: ${response.body}"
                )
                throw CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error. Please contact event admins."
                )
            }
        } catch (e: Exception) {
            logger.error("Error while sending email: ${e.message}")
            throw CustomException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error. Please contact event admins."
            )
        }
    }
}
