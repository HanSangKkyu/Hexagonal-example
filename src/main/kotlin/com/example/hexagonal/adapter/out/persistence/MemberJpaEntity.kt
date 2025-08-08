package com.example.hexagonal.adapter.out.persistence

import com.example.hexagonal.domain.model.Member
import jakarta.persistence.*

@Entity
@Table(name = "members")
class MemberJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String
) {
    fun toDomain(): Member {
        return Member(
            id = this.id,
            name = this.name,
            email = this.email
        )
    }

    companion object {
        fun from(member: Member): MemberJpaEntity {
            return MemberJpaEntity(
                id = member.id,
                name = member.name,
                email = member.email
            )
        }
    }
}