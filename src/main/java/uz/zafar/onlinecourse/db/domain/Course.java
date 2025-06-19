package uz.zafar.onlinecourse.db.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.zafar.onlinecourse.helper.TimeUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "courses")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String deletedName;
    private String description;
    private String telegramChannel;
    private Boolean hasTelegramChannel;
    private Date created = TimeUtil.currentTashkentTime();
    private Date updated = TimeUtil.currentTashkentTime();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "course", cascade = CascadeType.ALL)
    private List<Group> groups;
    private Boolean active = true;
}
