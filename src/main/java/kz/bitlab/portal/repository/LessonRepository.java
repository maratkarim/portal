package kz.bitlab.portal.repository;

import kz.bitlab.portal.model.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByChapterIdOrderById(Long chapterId);
    int countByChapterId(Long chapterId);
}