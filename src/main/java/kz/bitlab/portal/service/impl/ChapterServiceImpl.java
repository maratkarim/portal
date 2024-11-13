package kz.bitlab.portal.service.impl;

import kz.bitlab.portal.exception.ChapterNotFoundException;
import kz.bitlab.portal.exception.CourseNotFoundException;
import kz.bitlab.portal.model.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.UpdatedChapterDto;
import kz.bitlab.portal.model.entities.Chapter;
import kz.bitlab.portal.model.entities.Course;
import kz.bitlab.portal.model.entities.Lesson;
import kz.bitlab.portal.model.mapper.ChapterMapper;
import kz.bitlab.portal.model.mapper.LessonMapper;
import kz.bitlab.portal.repository.ChapterRepository;
import kz.bitlab.portal.repository.CourseRepository;
import kz.bitlab.portal.repository.LessonRepository;
import kz.bitlab.portal.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Override
    public void createChapter(CreateChapterDto createChapterDto) {
        log.info("Метод createChapter");

        Chapter chapter = chapterMapper.createChapterDtoToEntity(createChapterDto);
        log.debug("Преобразование из dto к entity");

        LocalDateTime localCreateTime = LocalDateTime.now();
        LocalDateTime localUpdateTime = LocalDateTime.now();

        Course course = courseRepository.findById(createChapterDto.getCourseId())
                        .orElseThrow(()->{
                            log.error("Курс внутри главы не найден: {}", createChapterDto.getCourseId());
                            return new ChapterNotFoundException("Курс внутри главы не найден");
                        });
        log.debug("Находим курс внутри главы");

        chapter.setOrder(chapterRepository.countByCourseId(createChapterDto.getCourseId()) + 1);
        log.debug("Выдаем новый порядок для созданного главы");

        chapter.setCourse(course);
        log.debug("Выдаем новый курс для созданного главы");

        chapter.setCreatedTime(localCreateTime);
        chapter.setUpdatedTime(localUpdateTime);
        log.info("Сеттим для create time и updated time для создание курса");

        chapterRepository.save(chapter);
        log.info("Глава создана!");
    }

    @Override
    public void updateChapter(Long id, UpdatedChapterDto updatedChapterDto) {
        log.info("Метод updateChapter");

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Глава не найдена: {}", id);
                    return new ChapterNotFoundException("Глава не найдена");
                });

        LocalDateTime localCreateTime = LocalDateTime.now();
        LocalDateTime localUpdateTime = LocalDateTime.now();

        if (updatedChapterDto.getName() != null){
            chapter.setName(updatedChapterDto.getName());
            log.debug("Обновляем назмение главы");
        }
        if (updatedChapterDto.getDescription() != null){
            chapter.setDescription(updatedChapterDto.getDescription());
            log.debug("Обновляем описание главы");
        }
        if (updatedChapterDto.getCourseId() != null){
            Course course = courseRepository.findById(updatedChapterDto.getCourseId())
                    .orElseThrow(()->{
                        log.error("Курс не найден: {}", updatedChapterDto.getCourseId());
                        return new CourseNotFoundException("Курс не найден");
                    });
            chapter.setCourse(course);
            log.debug("Обновили курс для главы");
        }

        chapter.setCreatedTime(localCreateTime);
        chapter.setUpdatedTime(localUpdateTime);
        log.debug("Обновляем измененное время курса");

        chapterRepository.save(chapter);
        log.info("Глава обновлена!");
    }

    @Override
    public void deleteChapter(Long id) {
        log.info("Метод deleteСhapter");

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Глава не найдена: {}", id);
                    return new ChapterNotFoundException("Глава не найдена");
                });
        log.debug("Ищем главу по ID {}", id);

        chapterRepository.delete(chapter);
        log.info("Глава удалена!");
    }

    @Override
    public GetChapterDto getChapterById(Long id) {
        log.info("Метод getChapterById");

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Глава не найдена: {}", id);
                    return new CourseNotFoundException("Глава не найдена");
                });
        log.debug("Ищем главу по ID {}", id);

        List<Lesson> lessonList = lessonRepository.findAllByChapterIdOrderById(id);
        log.debug("Находим все разделы уроков в упорядоченном виде");

        GetChapterDto chapterDto = chapterMapper.toGetChapterDto(chapter);
        log.debug("Преобразование из entity к dto");

        chapterDto.setLessons(lessonMapper.toDtoList(lessonList));
        log.debug("Сеттим уроки глав в упорядоченном виде");

        log.info("Глава по ID: {}!", id);
        return chapterDto;
    }

    @Override
    public List<GetChapterDto> getAllChapter() {
        log.info("Метод getAllChapter");

        List<Chapter> chapterList = chapterRepository.findAll();
        log.debug("Находим все главы");

        List<GetChapterDto> chapterDtoList = new ArrayList<>();

        for(Chapter chapter : chapterList){
            chapterDtoList.add(chapterMapper.toGetChapterDto(chapter));
            log.debug("Преобразуем entity главы в dto");
        }

        log.info("Все главы!");
        return chapterDtoList;
    }
}
