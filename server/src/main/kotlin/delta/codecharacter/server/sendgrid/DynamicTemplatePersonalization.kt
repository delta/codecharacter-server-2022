package delta.codecharacter.server.sendgrid

import com.fasterxml.jackson.annotation.JsonProperty
import com.sendgrid.helpers.mail.objects.Personalization
import java.util.Collections

class DynamicTemplatePersonalization : Personalization() {
    @JsonProperty(value = "dynamic_template_data")
    private var dynamicTemplateData: HashMap<String, String>? = null

    @JsonProperty(value = "dynamic_template_data")
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
