package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "teachers")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "teacher")
    private User user;
}
