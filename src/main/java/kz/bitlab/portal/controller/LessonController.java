package kz.bitlab.portal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.bitlab.portal.model.dto.lessonDto.CreateLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.GetLessonDto;
import kz.bitlab.portal.model.dto.lessonDto.UpdatedLessonDto;
import kz.bitlab.portal.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/all")
    @Operation(summary = "Получить список всех уроков", description = "Отображение полного списка уроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все уроки успешно отображены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public List<GetLessonDto> getAllLessons(){
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить урока по ID", description = "Запрос для получения информации о уроке по уникальному идентификатору (ID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о уроке успешно отображены"),
            @ApiResponse(responseCode = "404", description = "Урок с таким ID не найден"),
            @ApiResponse(responseCode = "500" , description = "Внутренняя ошибка сервера")
    })
    public GetLessonDto getLesson(@PathVariable("id") Long id){
        return lessonService.getLessonById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание и добавление нового урока", description = "Создание нового урока и его добавление в список")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урока успешно создан"),
            @ApiResponse(responseCode = "404", description = "Создание урока не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void createLesson(@RequestBody CreateLessonDto createLessonDto){
        lessonService.createLesson(createLessonDto);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обнавление урока", description = "Обновление данных о уроке в списке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урок успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Обновление урока не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void updateLesson(@PathVariable("id") Long id,
                             @RequestBody UpdatedLessonDto updatedLessonDto){
        lessonService.updateLesson(id, updatedLessonDto);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление урока", description = "Удаление данных о уроке в списке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урок успешно удалел"),
            @ApiResponse(responseCode = "404", description = "Удаление урока не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void deleteLesson(@PathVariable("id") Long id){
        lessonService.deleteLesson(id);
    }

}
