package delta.codecharacter.server.sendgrid

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
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

    @Value("\${spring.sendgrid.sender-email}") private lateinit var senderEmail: String
    //    @Value("\${spring.sendgrid.template-id}") private lateinit var templateId: String

    @Value("\${base-url}") private lateinit var baseUrl: String

    fun activateUserEmail(userId: UUID, token: String, name: String, email: String) {
        val link = "$baseUrl#/activate?id=$userId&token=$token"
        val linkName = "User Activation"
        val message = "You are just one step away from activating your CodeCharacter account"
        val disregardMessage = "If you did not create an account with us,please disregard this email"
        val buttonName = "Activate"
        val subjectInfo = "CodeCharacter Account Activation Link"
        sendTemplateEmail(
            email, name, linkName, link, message, disregardMessage, buttonName, subjectInfo
        )
    }

    fun resetPasswordEmail(userId: UUID, token: String, name: String, email: String) {
        val link = "$baseUrl#/reset-password?id=$userId&token=$token"
        val linkName = "Reset-Password link"
        val message = "Please click the button to reset your password"
        val disregardMessage = "If you did not request a password reset,please ignore this email."
        val buttonName = "Reset Password"
        val subjectInfo = "CodeCharacter Reset-Password Link"
        sendTemplateEmail(
            email, name, linkName, link, message, disregardMessage, buttonName, subjectInfo
        )
    }

    fun sendTemplateEmail(
        emailTo: String,
        name: String,
        linkName: String,
        link: String,
        message: String,
        disregardMessage: String,
        buttonName: String,
        subjectInfo: String
    ) {
        // Using DynamicTemplates are preferred but we dont have creds to access that so we used this :(
        //        val mail = Mail()
        //        val personalization = DynamicTemplatePersonalization()
        //        personalization.addTo(Email(emailTo))
        //        mail.setFrom(Email(senderEmail))
        //        mail.setSubject(subjectInfo)
        //        personalization.addDynamicTemplateData("name", name)
        //        personalization.addDynamicTemplateData("link_name", linkName)
        //        personalization.addDynamicTemplateData("message", message)
        //        personalization.addDynamicTemplateData("link", link)
        //        personalization.addDynamicTemplateData("button_name", buttonName)
        //        personalization.addDynamicTemplateData("subject", subjectInfo)
        //        mail.addPersonalization(personalization)
        //        mail.setTemplateId(templateId)
        val emailContent =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html data-editor-version=\"2\" class=\"sg-campaigns\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "    <head>\n" +
                "      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1\">\n" +
                "      <!--[if !mso]><!-->\n" +
                "      <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">\n" +
                "      <!--<![endif]-->\n" +
                "      <!--[if (gte mso 9)|(IE)]>\n" +
                "      <xml>\n" +
                "        <o:OfficeDocumentSettings>\n" +
                "          <o:AllowPNG/>\n" +
                "          <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "        </o:OfficeDocumentSettings>\n" +
                "      </xml>\n" +
                "      <![endif]-->\n" +
                "      <!--[if (gte mso 9)|(IE)]>\n" +
                "  <style type=\"text/css\">\n" +
                "    body {width: 620px;margin: 0 auto;}\n" +
                "    table {border-collapse: collapse;}\n" +
                "    table, td {mso-table-lspace: 0pt;mso-table-rspace: 0pt;}\n" +
                "    img {-ms-interpolation-mode: bicubic;}\n" +
                "  </style>\n" +
                "<![endif]-->\n" +
                "      <style type=\"text/css\">\n" +
                "    body, p, div {\n" +
                "      font-family: arial,helvetica,sans-serif;\n" +
                "      font-size: 14px;\n" +
                "    }\n" +
                "    body {\n" +
                "      color: #000000;\n" +
                "    }\n" +
                "    body a {\n" +
                "      color: #932A89;\n" +
                "      text-decoration: none;\n" +
                "    }\n" +
                "    p { margin: 0; padding: 0; }\n" +
                "    table.wrapper {\n" +
                "      width:100% !important;\n" +
                "      table-layout: fixed;\n" +
                "      -webkit-font-smoothing: antialiased;\n" +
                "      -webkit-text-size-adjust: 100%;\n" +
                "      -moz-text-size-adjust: 100%;\n" +
                "      -ms-text-size-adjust: 100%;\n" +
                "    }\n" +
                "    img.max-width {\n" +
                "      max-width: 100% !important;\n" +
                "    }\n" +
                "    .column.of-2 {\n" +
                "      width: 50%;\n" +
                "    }\n" +
                "    .column.of-3 {\n" +
                "      width: 33.333%;\n" +
                "    }\n" +
                "    .column.of-4 {\n" +
                "      width: 25%;\n" +
                "    }\n" +
                "    ul ul ul ul  {\n" +
                "      list-style-type: disc !important;\n" +
                "    }\n" +
                "    ol ol {\n" +
                "      list-style-type: lower-roman !important;\n" +
                "    }\n" +
                "    ol ol ol {\n" +
                "      list-style-type: lower-latin !important;\n" +
                "    }\n" +
                "    ol ol ol ol {\n" +
                "      list-style-type: decimal !important;\n" +
                "    }\n" +
                "    @media screen and (max-width:480px) {\n" +
                "      .preheader .rightColumnContent,\n" +
                "      .footer .rightColumnContent {\n" +
                "        text-align: left !important;\n" +
                "      }\n" +
                "      .preheader .rightColumnContent div,\n" +
                "      .preheader .rightColumnContent span,\n" +
                "      .footer .rightColumnContent div,\n" +
                "      .footer .rightColumnContent span {\n" +
                "        text-align: left !important;\n" +
                "      }\n" +
                "      .preheader .rightColumnContent,\n" +
                "      .preheader .leftColumnContent {\n" +
                "        font-size: 80% !important;\n" +
                "        padding: 5px 0;\n" +
                "      }\n" +
                "      table.wrapper-mobile {\n" +
                "        width: 100% !important;\n" +
                "        table-layout: fixed;\n" +
                "      }\n" +
                "      img.max-width {\n" +
                "        height: auto !important;\n" +
                "        max-width: 100% !important;\n" +
                "      }\n" +
                "      a.bulletproof-button {\n" +
                "        display: block !important;\n" +
                "        width: auto !important;\n" +
                "        font-size: 80%;\n" +
                "        padding-left: 0 !important;\n" +
                "        padding-right: 0 !important;\n" +
                "      }\n" +
                "      .columns {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "      .column {\n" +
                "        display: block !important;\n" +
                "        width: 100% !important;\n" +
                "        padding-left: 0 !important;\n" +
                "        padding-right: 0 !important;\n" +
                "        margin-left: 0 !important;\n" +
                "        margin-right: 0 !important;\n" +
                "      }\n" +
                "      .social-icon-column {\n" +
                "        display: inline-block !important;\n" +
                "      }\n" +
                "    }\n" +
                "  </style>\n" +
                "    <style>\n" +
                "      @media screen and (max-width:480px) {\n" +
                "        table\\0 {\n" +
                "          width: 480px !important;\n" +
                "          }\n" +
                "      }\n" +
                "    </style>\n" +
                "      <!--user entered Head Start--><!--End Head user entered-->\n" +
                "    </head>\n" +
                "    <body>\n" +
                "      <center class=\"wrapper\" data-link-color=\"#932A89\" data-body-style=\"font-size:14px; font-family:arial,helvetica,sans-serif; color:#000000; background-color:#f0f0f0;\">\n" +
                "        <div class=\"webkit\">\n" +
                "          <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" class=\"wrapper\" bgcolor=\"#f0f0f0\">\n" +
                "            <tr>\n" +
                "              <td valign=\"top\" bgcolor=\"#f0f0f0\" width=\"100%\">\n" +
                "                <table width=\"100%\" role=\"content-container\" class=\"outer\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                  <tr>\n" +
                "                    <td width=\"100%\">\n" +
                "                      <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "                        <tr>\n" +
                "                          <td>\n" +
                "                            <!--[if mso]>\n" +
                "    <center>\n" +
                "    <table><tr><td width=\"620\">\n" +
                "  <![endif]-->\n" +
                "                                    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%; max-width:620px;\" align=\"center\">\n" +
                "                                      <tr>\n" +
                "                                        <td role=\"modules-container\" style=\"padding:0px 10px 0px 10px; color:#000000; text-align:left;\" bgcolor=\"#F0F0F0\" width=\"100%\" align=\"left\"><table class=\"module preheader preheader-hide\" role=\"module\" data-type=\"preheader\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"display: none !important; mso-hide: all; visibility: hidden; opacity: 0; color: transparent; height: 0; width: 0;\">\n" +
                "    <tr>\n" +
                "      <td role=\"module-content\">\n" +
                "        <p></p>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table><table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"806c535f-a48e-47c3-84e1-e42f460fcd8b\" data-mc-module-version=\"2019-10-22\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"padding:5px 0px 5px 120px; line-height:12px; text-align:inherit; background-color:#f0f0f0;\" height=\"100%\" valign=\"top\" bgcolor=\"#f0f0f0\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: right\"><span style=\"font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; color: #9d9d9d; font-size: 10px\">Email not displaying correctly? </span><span style=\"font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; color: #932a89; font-size: 10px\"><strong>VIEW IT</strong></span><span style=\"font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; color: #9d9d9d; font-size: 10px\"> in your browser.</span></div><div></div></div></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" role=\"module\" data-type=\"columns\" style=\"padding:50px 20px 50px 20px;\" bgcolor=\"#1a1a1a\" data-distribution=\"1\">\n" +
                "    <tbody>\n" +
                "      <tr role=\"module-content\">\n" +
                "        <td height=\"100%\" valign=\"top\"><table width=\"460\" style=\"width:460px; border-spacing:0; border-collapse:collapse; margin:0px 50px 0px 50px;\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" border=\"0\" bgcolor=\"\" class=\"column column-0\">\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td style=\"padding:0px;margin:0px;border-spacing:0;\"><table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"efe5a2c4-1b11-49e7-889d-856d80b40f63\" data-mc-module-version=\"2019-10-22\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"padding:40px 0px 30px 0px; line-height:36px; text-align:inherit; background-color:#5e5959;\" height=\"100%\" valign=\"top\" bgcolor=\"#5e5959\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: center\"><span style=\"font-size: 36px\">$linkName</span></div><div></div></div></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table><table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"efe5a2c4-1b11-49e7-889d-856d80b40f63.2\" data-mc-module-version=\"2019-10-22\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"padding:50px 30px 30px 30px; line-height:28px; text-align:inherit; background-color:#ffffff;\" height=\"100%\" valign=\"top\" bgcolor=\"#ffffff\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 20px; font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; color: #656565\">Hi $name,</span></div>\n" +
                "<div style=\"font-family: inherit; text-align: inherit\"><br></div>\n" +
                "<div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 20px; font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; color: #656565\">$message,</span></div>\n" +
                "<div style=\"font-family: inherit; text-align: inherit\"><br></div>\n" +
                "<div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-size: 20px; font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; color: #656565\">$disregardMessage</span></div><div></div></div></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"module\" data-role=\"module-button\" data-type=\"button\" role=\"module\" style=\"table-layout:fixed;\" width=\"100%\" data-muid=\"7e9e5ded-d560-42a6-8e95-14ac7fdc0977\">\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td align=\"center\" bgcolor=\"#ffffff\" class=\"outer-td\" style=\"padding:0px 0px 0px 0px; background-color:#ffffff;\">\n" +
                "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"wrapper-mobile\" style=\"text-align:center;\">\n" +
                "              <tbody>\n" +
                "                <tr>\n" +
                "                <td align=\"center\" bgcolor=\"#333333\" class=\"inner-td\" style=\"border-radius:6px; font-size:16px; text-align:center; background-color:inherit;\">\n" +
                "                  <a href=\"${link}\" style=\"background-color:#333333; border:1px solid #000000; border-color:#000000; border-radius:6px; border-width:1px; color:#ffffff; display:inline-block; font-size:14px; font-weight:normal; letter-spacing:0px; line-height:normal; padding:12px 18px 12px 18px; text-align:center; text-decoration:none; border-style:solid; font-family:arial black, helvetica, sans-serif;\" target=\"_blank\">$buttonName</a>\n" +
                "                </td>\n" +
                "                </tr>\n" +
                "              </tbody>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table><table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"efe5a2c4-1b11-49e7-889d-856d80b40f63.1\" data-mc-module-version=\"2019-10-22\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"padding:40px 100px 50px 30px; line-height:26px; text-align:inherit; background-color:#FFFFFF;\" height=\"100%\" valign=\"top\" bgcolor=\"#FFFFFF\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; font-size: 16px; color: #656565\">With Regards!</span></div>\n" +
                "<div style=\"font-family: inherit; text-align: inherit\"><span style=\"font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; font-size: 16px; color: #656565\">CodeCharacter</span></div><div></div></div></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table></td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table><table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"13d36c46-b515-4bdf-ad3c-edafb5c1c151.1.1\" data-mc-module-version=\"2019-10-22\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"padding:0px 0px 25px 0px; line-height:32px; text-align:inherit; background-color:#000000;\" height=\"100%\" valign=\"top\" bgcolor=\"#000000\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: center\"><span style=\"font-family: &quot;trebuchet ms&quot;, helvetica, sans-serif; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: center; text-indent: 0px; text-transform: none; white-space: pre-wrap; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial; float: none; display: inline; font-size: 16px; color: #ffffff\">CodeCharatcer-2023</span></div><div></div></div></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table></td>\n" +
                "                                      </tr>\n" +
                "                                    </table>\n" +
                "                                    <!--[if mso]>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                              </table>\n" +
                "                            </center>\n" +
                "                            <![endif]-->\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </div>\n" +
                "      </center>\n" +
                "    </body>\n" +
                "  </html>"
        val content = Content("text/html", emailContent)
        val mail = Mail(Email(senderEmail), subjectInfo, Email(emailTo), content)
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
