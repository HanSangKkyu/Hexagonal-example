package com.example.hexagonal.adapter.`in`.web

import com.example.hexagonal.domain.model.Member
import com.example.hexagonal.domain.port.`in`.MemberUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@Tag(name = "Member", description = "회원 관리 API")
@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberUseCase: MemberUseCase
) {

    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "회원 생성 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MemberResponse::class))]),
        ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = [Content()]),
        ApiResponse(responseCode = "409", description = "이미 존재하는 이메일", content = [Content()])
    ])
    @PostMapping
    fun createMember(@Valid @RequestBody request: CreateMemberRequest): ResponseEntity<MemberResponse> {
        val member = memberUseCase.createMember(request.name, request.email)
        val response = MemberResponse.from(member)
        return ResponseEntity.created(URI.create("/api/members/${response.id}")).body(response)
    }

    @Operation(summary = "회원 조회", description = "ID로 회원을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원 조회 성공",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MemberResponse::class))]),
        ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음", content = [Content()])
    ])
    @GetMapping("/{id}")
    fun getMember(
        @Parameter(description = "회원 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MemberResponse> {
        return memberUseCase.getMemberById(id)
            ?.let { ResponseEntity.ok(MemberResponse.from(it)) }
            ?: ResponseEntity.notFound().build()
    }

    @Schema(description = "회원 생성 요청")
    data class CreateMemberRequest(
        @field:NotBlank(message = "이름은 비워둘 수 없습니다.")
        @Schema(description = "회원 이름", example = "홍길동")
        val name: String,
        @field:NotBlank(message = "이메일은 비워둘 수 없습니다.")
        @field:Email(message = "유효한 이메일 형식이 아닙니다.")
        @Schema(description = "회원 이메일", example = "hong@example.com")
        val email: String
    )

    @Schema(description = "회원 응답")
    data class MemberResponse(
        @Schema(description = "회원 ID", example = "1")
        val id: Long,
        @Schema(description = "회원 이름", example = "홍길동")
        val name: String,
        @Schema(description = "회원 이메일", example = "hong@example.com")
        val email: String
    ) {
        companion object {
            fun from(member: Member): MemberResponse {
                return MemberResponse(
                    id = member.id!!,
                    name = member.name,
                    email = member.email
                )
            }
        }
    }
}