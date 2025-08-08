package com.example.hexagonal.adapter.out.persistence

import com.example.hexagonal.domain.model.Member
import com.example.hexagonal.domain.port.out.MemberPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository
) : MemberPort {

    override fun save(member: Member): Member {
        val memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.from(member))
        return memberJpaEntity.toDomain()
    }

    override fun findById(id: Long): Member? {
        return memberJpaRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun existsByEmail(email: String): Boolean {
        return memberJpaRepository.existsByEmail(email)
    }
}