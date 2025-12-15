package uz.codebyz.onlinecoursebackend.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.message.entity.Chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("""
                select c
                from Chat c
                join ChatMember cm on cm.chat = c
                where c.type = uz.codebyz.onlinecoursebackend.message.entity.ChatType.PRIVATE
                  and cm.user.id in (:user1, :user2)
                group by c.id
                having count(cm.id) = 2
            """)
    Optional<Chat> findPrivateChatBetweenUsers(
            @Param("user1") UUID user1,
            @Param("user2") UUID user2
    );





    @Query("""
    select distinct c
    from Chat c
    join ChatMember cm on cm.chat.id = c.id
    where
        (
            lower(c.title) like lower(concat('%', :q, '%'))
            or lower(c.username) like lower(concat('%', :q, '%'))
        )
        and (
            c.type <> uz.codebyz.onlinecoursebackend.message.entity.ChatType.PRIVATE
            or not exists (
                select 1
                from ChatMember other
                where other.chat.id = c.id
                  and other.user.id <> :userId
                  and exists (
                      select 1
                      from BlockUser b
                      where b.active = true
                        and (
                            (b.from.id = :userId and b.to.id = other.user.id)
                            or
                            (b.from.id = other.user.id and b.to.id = :userId)
                        )
                  )
            )
        )
""")
    List<Chat> searchChats(
            @Param("q") String q,
            @Param("userId") UUID userId
    );



}
