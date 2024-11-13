package kz.bitlab.portal.controller;

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
    public List<GetLessonDto> getAllLessons(){
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public GetLessonDto getLesson(@PathVariable("id") Long id){
        return lessonService.getLessonById(id);
    }

    @PostMapping("/create")
    public void createLesson(@RequestBody CreateLessonDto createLessonDto){
        lessonService.createLesson(createLessonDto);
    }

    @PutMapping("/update/{id}")
    public void updateLesson(@PathVariable("id") Long id,
                             @RequestBody UpdatedLessonDto updatedLessonDto){
        lessonService.updateLesson(id, updatedLessonDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLesson(@PathVariable("id") Long id){
        lessonService.deleteLesson(id);
    }

}
