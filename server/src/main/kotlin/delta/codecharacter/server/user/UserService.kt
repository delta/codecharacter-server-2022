package delta.codecharacter.server.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {

    @Autowired private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(email: String?): UserDetails {
        return userRepository.findFirstByEmail(email)
    }
}
