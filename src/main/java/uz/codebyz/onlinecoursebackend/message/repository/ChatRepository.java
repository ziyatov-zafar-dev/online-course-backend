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
                where (
                        (c.user1Id = :u1 and c.user2Id = :u2)
                     or (c.user1Id = :u2 and c.user2Id = :u1)
                  )
            """)
    Optional<Chat> findPrivateChatBetweenUsers(@Param("u1") UUID user1Id, @Param("u2") UUID user2Id);

    @Query("""
                select c
                from Chat c
                join c.messages m
                where (c.user1Id = :u or c.user2Id = :u)
                  and c.deleted = false
                  and m.active = true
                group by c
                order by max(m.createdAt) desc
            """)
    List<Chat> getChatsByUsers(@Param("u") UUID userId);

}
