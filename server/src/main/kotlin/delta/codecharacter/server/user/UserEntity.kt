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
    val loginType: LoginType,
    private val password: String,
    val isProfileComplete: Boolean,
    val tutorialLevel: Int?,
    private val isEnabled: Boolean = false,
    private val isCredentialsNonExpired: Boolean = true,
    private val isAccountNonExpired: Boolean = true,
    private val isAccountNonLocked: Boolean = true,
    val userAuthorities: MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_USER")),
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = userAuthorities
    override fun getPassword(): String = password
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isEnabled(): Boolean = isEnabled
}
