package com.example.hexagonal.domain.service

import com.example.hexagonal.domain.model.Member
import com.example.hexagonal.domain.port.`in`.MemberUseCase
import com.example.hexagonal.domain.port.out.MemberPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberPort: MemberPort
) : MemberUseCase {

    @Transactional
    override fun createMember(name: String, email: String): Member {
        if (memberPort.existsByEmail(email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }
        val member = Member(name = name, email = email)
        return memberPort.save(member)
    }

    override fun getMemberById(id: Long): Member? {
        return memberPort.findById(id)
    }
}