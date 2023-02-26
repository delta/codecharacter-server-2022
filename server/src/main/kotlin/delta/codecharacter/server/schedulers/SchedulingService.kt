package delta.codecharacter.server.schedulers

import delta.codecharacter.server.user.public_user.PublicUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service class SchedulingService(@Autowired private val publicUserService: PublicUserService)
