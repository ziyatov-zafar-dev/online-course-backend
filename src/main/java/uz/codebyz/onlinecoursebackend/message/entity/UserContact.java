package uz.codebyz.onlinecoursebackend.message.entity;

import jakarta.persistence.*;
import uz.codebyz.onlinecoursebackend.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_contacts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"owner_user_id", "contact_user_id"})
        }
)
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /**
     * Kontaktni saqlagan user
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User ownerUser;

    /**
     * Kontakt bo‘lgan user
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_user_id", nullable = false)
    private User contactUser;

    /**
     * Kontaktning username'i (snapshot sifatida saqlanadi)
     */
    @Column(name = "contact_username", nullable = false, length = 64)
    private String contactUsername;

    /**
     * User kontaktni qanday nom bilan saqlagan
     */
    @Column(name = "display_name", length = 255)
    private String displayName;

    /**
     * Kontakt bloklanganmi
     */
    @Column(name = "is_blocked", nullable = false)
    private boolean blocked = false;

    /**
     * Kontakt yaratilgan vaqt
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /* ==========================
       JPA CALLBACKS
       ========================== */

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    /* ==========================
       BUSINESS METHODS
       ========================== */

    public void block() {
        this.blocked = true;
    }

    public void unblock() {
        this.blocked = false;
    }

    /* ==========================
       GETTERS & SETTERS
       ========================== */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public User getContactUser() {
        return contactUser;
    }

    public void setContactUser(User contactUser) {
        this.contactUser = contactUser;
    }

    public String getContactUsername() {
        return contactUsername;
    }

    public void setContactUsername(String contactUsername) {
        this.contactUsername = contactUsername;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
