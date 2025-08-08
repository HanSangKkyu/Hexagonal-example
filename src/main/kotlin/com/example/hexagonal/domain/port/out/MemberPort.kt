package com.example.hexagonal.domain.port.out

import com.example.hexagonal.domain.model.Member

interface MemberPort {
    fun save(member: Member): Member
    fun findById(id: Long): Member?
    fun existsByEmail(email: String): Boolean
}