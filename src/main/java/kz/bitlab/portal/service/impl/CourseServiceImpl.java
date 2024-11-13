package kz.bitlab.portal.service.impl;

import kz.bitlab.portal.exception.CourseNotFoundException;
import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.dto.courseDto.UpdatedCourseDto;
import kz.bitlab.portal.model.entities.Chapter;
import kz.bitlab.portal.model.mapper.ChapterMapper;
import kz.bitlab.portal.model.mapper.CourseMapper;
import kz.bitlab.portal.model.entities.Course;
import kz.bitlab.portal.repository.ChapterRepository;
import kz.bitlab.portal.repository.CourseRepository;
import kz.bitlab.portal.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    @Override
    public void createCourse(CreateCourseDto createCourseDto) {
        log.info("Метод createCourse");

        Course course = courseMapper.createCourseDtoToEntity(createCourseDto);
        log.debug("Преобразование из dto к entity");

        LocalDateTime localDateTime = LocalDateTime.now();
        course.setCreatedTime(localDateTime);
        course.setUpdatedTime(localDateTime);
        log.info("Сеттим для create time и updated time для создание курса");

        courseRepository.save(course);
        log.info("Курс создан!");
    }

    @Override
    public void updateCourse(Long id, UpdatedCourseDto updatedCourseDto) {
        log.info("Метод updateCourse");

        Course course = courseRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Курсы не найден: {}", id);
                    return new CourseNotFoundException("Курс не найден");
                });

        LocalDateTime localCreateTime = course.getCreatedTime();
        LocalDateTime localUpdateTime = LocalDateTime.now();

        if (updatedCourseDto.getName() != null){
            course.setName(updatedCourseDto.getName());
            log.debug("Обновляем назмение курса");
        }
        if (updatedCourseDto.getDescription() != null){
            course.setDescription(updatedCourseDto.getDescription());
            log.debug("Обновляем описание курса");
        }
        course.setCreatedTime(localCreateTime);
        course.setUpdatedTime(localUpdateTime);
        log.debug("Обновляем измененное время курса");

        courseMapper.toGetCourseDto(courseRepository.save(course));
        log.info("Курс обновлен!");
    }

    @Override
    public void deleteCourse(Long id) {
        log.info("Метод deleteCourse");

        Course course = courseRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Курсы не найден: {}", id);
                    return new CourseNotFoundException("Курс не найден");
                });
        log.debug("Ищем курс по ID {}", id);

        courseRepository.delete(course);
        log.info("Курс удален!");
    }

    @Override
    public GetCourseDto getCourseById(Long id) {
        log.info("Метод getCourseById");

        Course course = courseRepository.findById(id)
                .orElseThrow(()-> {
                    log.error("Курсы не найден: {}", id);
                    return new CourseNotFoundException("Курс не найден");
                });
        log.debug("Ищем курс по ID {}", id);

        List<Chapter> chapterList = chapterRepository.findAllByCourseIdOrderByOrder(id);
        log.debug("Находим все разделы глав в упорядоченном виде");

        GetCourseDto dto = courseMapper.toGetCourseDto(course);
        log.debug("Преобразование из entity к dto");

        dto.setChapters(chapterMapper.toDtoList(chapterList));
        log.debug("Сеттим главы курсов в упорядоченном виде");

        log.info("Курс по ID: {}!", id);
        return dto;
    }

    @Override
    public List<GetCourseDto> getAllCourse() {
        log.info("Метод getAllCourse");

        List<Course> courseList = courseRepository.findAll();
        log.debug("Находим все курсы");

        List<GetCourseDto> courseDtoList = new ArrayList<>();

        for(Course course : courseList){
            courseDtoList.add(courseMapper.toGetCourseDto(course));
            log.debug("Преобразуем entity курсы в dto");
        }

        log.info("Все курсы!");
        return courseDtoList;
    }
}
