package delta.codecharacter.server.user

import delta.codecharacter.server.exception.CustomException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {

    @Autowired private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(email: String?): UserEntity {
        if (email == null) {
            throw UsernameNotFoundException("User not found")
        }
        val user = userRepository.findFirstByEmail(email)
        if (user.isEmpty) {
            throw UsernameNotFoundException("User not found")
        } else if (!user.get().isEnabled) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Email not verified")
        } else if (!user.get().isAccountNonExpired) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Account expired")
        } else {
            return user.get()
        }
    }
}
