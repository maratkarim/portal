package kz.bitlab.portal.repository;

import kz.bitlab.portal.model.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findAllByCourseIdOrderByOrder(Long courseId);
    int countByCourseId(Long courseId);
}
