/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.3.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package delta.codecharacter.core

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.annotations.Authorization
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Validated
@Api(value = "Game", description = "The Game API")
@RequestMapping("\${api.base-path:}")
interface GameApi {

    @ApiOperation(
        value = "Get game logs by game ID",
        nickname = "getGameLogsByGameId",
        notes = "Get game logs by game ID",
        response = String::class,
        authorizations = [Authorization(value = "http-bearer")]
    )
    @ApiResponses(
        value = [ApiResponse(
            code = 200,
            message = "OK",
            response = String::class
        )]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/games/{gameId}/logs"],
        produces = ["application/json"]
    )
    fun getGameLogsByGameId(
        @ApiParam(
            value = "UUID of the game",
            required = true
        ) @PathVariable("gameId") gameId: java.util.UUID
    ): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
