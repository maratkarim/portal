package kz.bitlab.portal.controller;

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
    public List<GetChapterDto> getAllChapter(){
        return chapterService.getAllChapter();
    }

    @GetMapping("/{id}")
    public GetChapterDto getChapter(@PathVariable("id") Long id){
        return chapterService.getChapterById(id);
    }

    @PostMapping("/create")
    public void createChapter(@RequestBody CreateChapterDto createChapterDto){
        chapterService.createChapter(createChapterDto);
    }

    @PutMapping("/update/{id}")
    public void updateChapter(@PathVariable("id") Long id,
                              @RequestBody UpdatedChapterDto updatedChapterDto){
        chapterService.updateChapter(id, updatedChapterDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteChapter(@PathVariable("id") Long id){
        chapterService.deleteChapter(id);
    }
}
