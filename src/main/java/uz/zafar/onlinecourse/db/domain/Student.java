package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "students")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "student")
    private User user;

}
