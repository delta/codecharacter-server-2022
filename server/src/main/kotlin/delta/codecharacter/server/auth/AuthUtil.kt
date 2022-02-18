package delta.codecharacter.server.auth

import delta.codecharacter.server.user.UserEntity
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.Date
import java.util.function.Function

@Service
class AuthUtil : Serializable {

    private val secret: String = "secret"

    fun getUsernameFromToken(token: String?): String {
        return getClaimFromToken(token) { obj: Claims -> obj.subject }
    }

    fun getExpirationDateFromToken(token: String?): Date {
        return getClaimFromToken(token) { obj: Claims -> obj.expiration }
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String?): Claims {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        } catch (e: Exception) {
            throw e
        }
    }

    private fun isTokenExpired(token: String?): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(userDetails: UserEntity): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.email)
    }
    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        val jwt =
            Jwts.builder()
                .apply {
                    setClaims(claims)
                    setSubject(subject)
                    setIssuedAt(Date(System.currentTimeMillis()))
                    setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 24))
                    signWith(SignatureAlgorithm.HS512, secret)
                }
                .compact()
        return jwt
    }

    fun validateToken(token: String?, userDetails: UserEntity): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.email && !isTokenExpired(token)
    }
}
