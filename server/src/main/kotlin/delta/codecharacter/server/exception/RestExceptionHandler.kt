package delta.codecharacter.server.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatusCode

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatusCode, request: WebRequest): ResponseEntity<Any>? {
        val cause = ex.cause
        return if (cause is MissingKotlinParameterException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("message" to "${cause.parameter.name} is missing"))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to "Unknown error"))
        }
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatusCode, request: WebRequest): ResponseEntity<Any>? {
        return if (ex.bindingResult.fieldErrors.isNotEmpty()) {
            val fields = mutableListOf<String>()
            ex.bindingResult.fieldErrors.forEach { fieldError -> fields.add(fieldError.field) }
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapOf("message" to "Invalid ${fields.toSet().joinToString(", ")}"))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to "Invalid fields"))
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to ex.constraintViolations.joinToString(", ")))
    }

    @ExceptionHandler(CustomException::class)
    protected fun handleCustomException(ex: CustomException): ResponseEntity<Any> {
        return ResponseEntity.status(ex.status).body(mapOf("message" to ex.message))
    }
}
