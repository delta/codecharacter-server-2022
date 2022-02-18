package delta.codecharacter.server.config

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CorsFilter : Filter {
    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(
        servletRequest: ServletRequest?,
        servletResponse: ServletResponse,
        filterChain: FilterChain
    ) {
        val response: HttpServletResponse = servletResponse as HttpServletResponse
        val request: HttpServletRequest? = servletRequest as HttpServletRequest?
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS,PATCH")
        response.setHeader("Access-Control-Allow-Headers", "*")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Max-Age", "180")
        filterChain.doFilter(servletRequest, servletResponse)
    }

    override fun destroy() {}
}
