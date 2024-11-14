package kz.bitlab.portal.service.impl;

import kz.bitlab.portal.exception.ChapterNotFoundException;
import kz.bitlab.portal.exception.CourseNotFoundException;
import kz.bitlab.portal.exception.LessonNotFoundException;
import kz.bitlab.portal.model.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.UpdatedLessonDto;
import kz.bitlab.portal.model.entities.Chapter;
import kz.bitlab.portal.model.entities.Lesson;
import kz.bitlab.portal.model.mapper.LessonMapper;
import kz.bitlab.portal.repository.ChapterRepository;
import kz.bitlab.portal.repository.LessonRepository;
import kz.bitlab.portal.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final ChapterRepository chapterRepository;

    @Override
    public void createLesson(CreateLessonDto createLessonDto) {
        log.info("Метод createLesson");

        Lesson lesson = lessonMapper.createLessonDtoToEntity(createLessonDto);
        log.debug("Преобразование из dto к entity");

        LocalDateTime localCreateTime = LocalDateTime.now();
        LocalDateTime localUpdateTime = LocalDateTime.now();

        Chapter chapter = chapterRepository.findById(createLessonDto.getChapterId())
                .orElseThrow(()->{
                    log.error("Глава внутри уроков не найден: {}", createLessonDto.getChapterId());
                    return new CourseNotFoundException("Глава внутри уроков не найден");
                });
        log.debug("Находим главу внутри уроков");

        lesson.setOrder(lessonRepository.countByChapterId(createLessonDto.getChapterId()) + 1);
        log.debug("Выдаем новый порядок для созданного урока");

        lesson.setChapter(chapter);
        log.debug("Выдаем новый главу для созданного курса");

        lesson.setCreatedTime(localCreateTime);
        lesson.setUpdatedTime(localUpdateTime);
        log.info("Сеттим для create time и updated time для создание урока");

        lessonRepository.save(lesson);
        log.info("Глава создана!");
    }

    @Override
    public void updateLesson(Long id, UpdatedLessonDto updatedLessonDto) {
        log.info("Метод updateLesson");

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Урок не найден: {}", id);
                    return new ChapterNotFoundException("Урок не найден");
                });

        LocalDateTime localCreateTime = LocalDateTime.now();
        LocalDateTime localUpdateTime = LocalDateTime.now();

        if (updatedLessonDto.getName() != null){
            lesson.setName(updatedLessonDto.getName());
            log.debug("Обновляем назмение урока");
        }
        if (updatedLessonDto.getDescription() != null){
            lesson.setDescription(updatedLessonDto.getDescription());
            log.debug("Обновляем описание урока");
        }
        if (updatedLessonDto.getContext() != null){
            lesson.setContext(updatedLessonDto.getContext());
            log.debug("Обновляем контент урока");
        }
        if (updatedLessonDto.getId() != null){
            Chapter chapter = chapterRepository.findById(updatedLessonDto.getId())
                    .orElseThrow(()->{
                        log.error("Глава не найдена: {}", updatedLessonDto.getId());
                        return new CourseNotFoundException("Глава не найдена");
                    });
            lesson.setChapter(chapter);
            log.debug("Обновили главу для урока");
        }

        lesson.setCreatedTime(localCreateTime);
        lesson.setUpdatedTime(localUpdateTime);
        log.debug("Обновляем измененное время урока");

        lessonRepository.save(lesson);
        log.info("Урок обновлен!");
    }

    @Override
    public void deleteLesson(Long id) {
        log.info("Метод deleteLesson");

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Урок не найден: {}", id);
                    return new ChapterNotFoundException("Урок не найден");
                });
        log.debug("Ищем урок по ID {}", id);

        lessonRepository.delete(lesson);
        log.info("Урок удален!");
    }

    @Override
    public GetLessonDto getLessonById(Long id) {
        log.info("Метод getLessonById");

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Урок не найден: {}", id);
                    return new LessonNotFoundException("Урок не найден");
                });
        System.out.println(lesson.getId());
        System.out.println(lesson.getName());
        log.debug("Ищем урок по ID {}", id);

        log.info("Урок по ID: {}!", id);
        GetLessonDto lessonDto = lessonMapper.toGetLessonDto(lesson);
        System.out.println(lessonDto.getName());
        System.out.println(lessonDto.getId());
        return lessonDto;

    }

    @Override
    public List<GetLessonDto> getAllLessons() {
        log.info("Метод getAllLessons");

        List<Lesson> lessonList = lessonRepository.findAll();
        log.debug("Находим все уроки");

        List<GetLessonDto> lessonDtos = new ArrayList<>();

        for(Lesson lesson : lessonList){
            lessonDtos.add(lessonMapper.toGetLessonDto(lesson));
            log.debug("Преобразуем entity уроков в dto");
        }

        log.info("Уроки главы!");
        return lessonDtos;
    }
}
