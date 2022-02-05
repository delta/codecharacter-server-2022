package delta.codecharacter.server.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Document(collection = "user")
data class UserEntity(
    @Id val id: UUID,
    @Indexed(unique = true) val email: String,
    @Indexed(unique = true) private val username: String,
    private val password: String,
    private val isEnabled: Boolean = false,
    private val isCredentialsNonExpired: Boolean,
    private val isAccountNonLocked: Boolean,
) : UserDetails {
    override fun getAuthorities(): Set<GrantedAuthority> {
        return setOf(SimpleGrantedAuthority("ROLE_USER"))
    }
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isEnabled(): Boolean = isEnabled
}
