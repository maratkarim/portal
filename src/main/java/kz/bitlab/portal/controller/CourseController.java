package kz.bitlab.portal.controller;

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
    public List<GetCourseDto> getAllCourse(){
        return courseService.getAllCourse();
    }

    @GetMapping("/{id}")
    public GetCourseDto getCourseDto(@PathVariable("id") Long id){
        return courseService.getCourseById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createCourse(@RequestBody CreateCourseDto createCourseDto) {
        courseService.createCourse(createCourseDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{id}")
    public void updateCourse(@PathVariable("id") Long id,
                             @RequestBody UpdatedCourseDto updatedCourseDto){
        courseService.updateCourse(id, updatedCourseDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCourse(@PathVariable("id") Long id){
        courseService.deleteCourse(id);
    }

}
