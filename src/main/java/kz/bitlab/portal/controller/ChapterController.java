package kz.bitlab.portal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.bitlab.portal.model.dto.chapterDto.CreateChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.GetChapterDto;
import kz.bitlab.portal.model.dto.chapterDto.UpdatedChapterDto;
import kz.bitlab.portal.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chapter")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/all")
    @Operation(summary = "Получить список всех глав", description = "Отображение полного списка глав")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все главы успешно отображены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public List<GetChapterDto> getAllChapter(){
        return chapterService.getAllChapter();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить главу по ID", description = "Запрос для получения информации о главы по уникальному идентификатору (ID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о главы успешно отображены"),
            @ApiResponse(responseCode = "404", description = "Глава с таким ID не найден"),
            @ApiResponse(responseCode = "500" , description = "Внутренняя ошибка сервера")
    })
    public GetChapterDto getChapter(@PathVariable("id") Long id){
        return chapterService.getChapterById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание и добавление нового главы", description = "Создание нового главы и его добавление в список")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно создан"),
            @ApiResponse(responseCode = "404", description = "Создание главы не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void createChapter(@RequestBody CreateChapterDto createChapterDto){
        chapterService.createChapter(createChapterDto);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обнавление главы", description = "Обновление данных о главы в списке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Обновление главы не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void updateChapter(@PathVariable("id") Long id,
                              @RequestBody UpdatedChapterDto updatedChapterDto){
        chapterService.updateChapter(id, updatedChapterDto);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление главы", description = "Удаление данных о главы в списке")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно удалел"),
            @ApiResponse(responseCode = "404", description = "Удаление главы не удалось"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public void deleteChapter(@PathVariable("id") Long id){
        chapterService.deleteChapter(id);
    }
}
