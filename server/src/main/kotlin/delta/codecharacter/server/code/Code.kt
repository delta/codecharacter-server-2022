package delta.codecharacter.server.code

import java.time.Instant

data class Code(val code: String, val language: LanguageEnum, val lastSavedAt: Instant? = null)
