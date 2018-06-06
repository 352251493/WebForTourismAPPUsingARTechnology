package com.gxg.controller;

import com.gxg.services.PostCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 郭欣光 on 2018/6/4.
 */

@RestController
@RequestMapping(value = "/post_card")
public class PostCardController {

    @Autowired
    private PostCardService postCardService;

    @PostMapping(value = "/add")
    public String addPostCard(@RequestParam("city_id") String cityId, @RequestParam("image") MultipartFile image, @RequestParam("send_word") String sendWord, HttpServletRequest request) {
        return postCardService.addPostCard(cityId, image, sendWord, request);
    }

    @GetMapping(value = "/all")
    public String getAllPostCard(HttpServletRequest request) {
        return postCardService.getAllPostCard(request);
    }

    @GetMapping(value = "/pc_id/{pcId}")
    public String getPostCard(@PathVariable String pcId, HttpServletRequest request) {
        return postCardService.getPostCard(pcId, request);
    }

    @PostMapping(value = "/update")
    public String updatePostCard(@RequestParam("pc_id") String pcId, @RequestParam("city_id") String cityId, @RequestParam("send_word") String sendWord, HttpServletRequest request) {
        return postCardService.updatePostCard(pcId, cityId, sendWord, request);
    }

    @PostMapping(value = "/update_image")
    public String updatePostCardImage(@RequestParam("pc_id") String pcId, @RequestParam("image") MultipartFile image, HttpServletRequest request) {
        return postCardService.updatePostCardImage(pcId, image, request);
    }

    @PostMapping(value = "/delete/pc_id/{pcId}")
    public String deletePostCard(@PathVariable String pcId, HttpServletRequest request) {
        return postCardService.deletePostCard(pcId, request);
    }
}
