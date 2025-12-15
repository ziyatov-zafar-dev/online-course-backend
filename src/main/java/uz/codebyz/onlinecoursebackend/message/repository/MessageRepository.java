package uz.codebyz.onlinecoursebackend.message.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.codebyz.onlinecoursebackend.message.entity.BlockUser;
import uz.codebyz.onlinecoursebackend.message.entity.Message;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findAllByChatId(UUID chatId, Pageable pageable);

    /**
     * PRIVATE chat uchun:
     * requester yubormagan va hali o‘qilmagan xabarlarni o‘qilgan deb belgilash
     */
    @Modifying
    @Query("""
                update Message m
                set m.read = true
                where m.chat.id = :chatId
                  and m.sender.id <> :userId
                  and m.read = false
            """)
    void markMessagesAsRead(@Param("chatId") UUID chatId,
                            @Param("userId") UUID userId);

    Optional<Message> findTopByChatIdOrderByCreatedAtDesc(UUID chatId);

    @Query("""
                select count(m)
                from Message m
                where m.chat.id = :chatId
                  and m.sender.id <> :userId
                  and m.read = false
            """)
    long countUnreadMessages(@Param("chatId") UUID chatId,
                             @Param("userId") UUID userId);

}
