package com.starter.user.app.user.controller

import com.starter.core.common.response.ServiceResponse
import com.starter.core.common.response.SuccessResponse
import com.starter.core.domain.user.models.UserVO
import com.starter.user.app.user.facade.UserReadFacadeService
import com.starter.user.app.user.facade.UserResisterFacadeService
import com.starter.user.app.user.model.UserCreateRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val resisterFacadeService: UserResisterFacadeService,
    private val readFacadeService: UserReadFacadeService,
) {

    @PostMapping("/api/users")
    fun createMember(@RequestBody userCreateRequest: UserCreateRequest): SuccessResponse<UserVO> {
        val user = resisterFacadeService.resisterMember(userCreateRequest)
        return SuccessResponse.of(user)
    }

    @GetMapping("/api/users/{uuid}")
    fun getUsers(@PathVariable uuid: String): SuccessResponse<UserVO> {
        val member = readFacadeService.getUser(uuid)
        return SuccessResponse.of(member)
    }

    @GetMapping("/api/users/name")
    fun getMembersByName(@RequestParam name: String): ServiceResponse {
        val members = readFacadeService.getUsers(name);
        return SuccessResponse.of(members)
    }
}
