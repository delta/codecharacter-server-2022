package delta.codecharacter.server.code

import com.ninjasquad.springmockk.MockkBean
import delta.codecharacter.server.code.latest_code.LatestCodeRepository

internal class CodeServiceTest {

    @MockkBean private lateinit var codeRepository: LatestCodeRepository
}
