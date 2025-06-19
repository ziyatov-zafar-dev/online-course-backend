package uz.zafar.onlinecourse.db.repository;

//import com.example.onlinecoursemain.db.domain.Type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.zafar.onlinecourse.db.domain.Type;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Short> {
    @Query("""
            select t from Type t where t.active=:active order by t.id""")
    public List<Type> findAllByActive(boolean active);

    public Type findByNameAndActive(String name, Boolean active);
}
