package com.example.hexagonal.domain.port.`in`

import com.example.hexagonal.domain.model.Member

interface MemberUseCase {
    fun createMember(name: String, email: String): Member
    fun getMemberById(id: Long): Member?
}