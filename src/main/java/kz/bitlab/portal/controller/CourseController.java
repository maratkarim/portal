package kz.bitlab.portal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.bitlab.portal.model.dto.courseDto.CreateCourseDto;
import kz.bitlab.portal.model.dto.courseDto.GetCourseDto;
import kz.bitlab.portal.model.dto.courseDto.UpdatedCourseDto;
import kz.bitlab.portal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/all")
    @Operation(summary = "Получить список всех курсов", description = "Отображение полного списка курсов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все курсы успешно отображены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public List<GetCourseDto> getAllCourse(){
        return courseService.getAllCourse();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить курсы по ID", description = "Запрос для получения информации о курсах по уникальному идентификатору (ID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о курсы успешно отображены"),
            @ApiResponse(responseCode = "404", description = "Курс с таким ID не найден"),
            @ApiResponse(responseCode = "500" , description = "Внутренняя ошибка сервера")
    })
    public GetCourseDto getCourseDto(@PathVariable("id") Long id){
        return courseService.getCourseById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание и добавление нового курса", description = "Создание нового курса и его добавление в список")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "404", description = "Создание курса не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> createCourse(@RequestBody CreateCourseDto createCourseDto) {
        courseService.createCourse(createCourseDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обнавление курса", description = "Обновление данных о курсе в списке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Обновление курса не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void updateCourse(@PathVariable("id") Long id,
                             @RequestBody UpdatedCourseDto updatedCourseDto){
        courseService.updateCourse(id, updatedCourseDto);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление курса", description = "Удаление данных о курсе в списке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно удалел"),
            @ApiResponse(responseCode = "404", description = "Удаление курса не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void deleteCourse(@PathVariable("id") Long id){
        courseService.deleteCourse(id);
    }
}
