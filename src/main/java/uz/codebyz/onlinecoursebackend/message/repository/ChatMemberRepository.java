package uz.codebyz.onlinecoursebackend.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.message.entity.Chat;
import uz.codebyz.onlinecoursebackend.message.entity.ChatMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatMemberRepository extends JpaRepository<ChatMember, UUID> {
    @Query("""
                select case when count(cm) > 0 then true else false end
                from ChatMember cm
                where cm.chat.id = :chatId
                  and cm.user.id = :userId
            """)
    boolean existsByChatIdAndUserId(
            @Param("chatId") UUID chatId,
            @Param("userId") UUID userId
    );


    @Query("""
                select cm
                from ChatMember cm
                where cm.chat.id = :chatId
                  and cm.user.id <> :senderId
                  and cm.leftAt is null
            """)
    Optional<ChatMember> findOtherPrivateMember(
            @Param("chatId") UUID chatId,
            @Param("senderId") UUID senderId
    );


    @Query("""
                select cm
                from ChatMember cm
                where cm.chat.id = :chatId
                  and cm.leftAt is null
                order by cm.joinedAt asc
            """)
    List<ChatMember> findActiveMembersByChatId(@Param("chatId") UUID chatId);

    boolean existsByChatIdAndUserIdAndLeftAtIsNull(UUID chatId, UUID userId);

    @Query("""
                select c
                from ChatMember cm
                join cm.chat c
                left join Message m on m.chat = c
                where cm.user.id = :userId
                  and cm.leftAt is null
                  and c.type = uz.codebyz.onlinecoursebackend.message.entity.ChatType.GROUP
                group by c.id
                order by coalesce(max(m.createdAt), c.createdAt) desc
            """)
    List<Chat> findActiveGroupChatsByUserId(@Param("userId") UUID userId);

    Optional<ChatMember> findByChatIdAndUserId(UUID chatId, UUID userId);

    @Query("""
                select cm
                from ChatMember cm
                where cm.chat.id = :chatId
                  and cm.user.id = :userId
                  and cm.leftAt is null
            """)
    Optional<ChatMember> findActiveMember(@Param("chatId") UUID chatId, @Param("userId") UUID userId);

}
