package com.LRG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.LRG.service.IndexService;
import com.LRG.service.ReportService;

@Controller
public class YouTubeVideoController {
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private ReportService reportService;

	@RequestMapping(value = {"/insert/youtubeVideoId"}, method = RequestMethod.GET)
	public String youtubeVideoForm(ModelMap modelMap) {
		modelMap.addAttribute("status", "");
		return "youtubeVideoForm";
	}
	
	@RequestMapping(value = {"/insert/youtubeVideoId"}, method = RequestMethod.POST)
	public String addVideoId(ModelMap modelMap, @RequestParam("videoId") String videoId) {
		if(!videoId.equals("") && videoId!=null) {
			indexService.addVideoId(videoId);
			modelMap.addAttribute("status", "true");
		}else {
			modelMap.addAttribute("status", "false");
		}
		return "youtubeVideoForm";
	}
	
	@RequestMapping(value = {"/insert/report"}, method = RequestMethod.GET)
	public String reportForm(ModelMap modelMap) {
		System.out.println("showing report insert page....");
		modelMap.addAttribute("status", "");
		return "addReport";
	}
	
	@RequestMapping(value = {"/insert/report"}, method = RequestMethod.POST)
	public String addReport(ModelMap modelMap, @RequestParam("p") String p,@RequestParam("d") String d,@RequestParam("y") int y,
			@RequestParam("l") String l) {
		if(p!=null && d!=null && l!=null) {
			reportService.addReport(p,d,y,l);
			modelMap.addAttribute("status", "true");
		}else {
			modelMap.addAttribute("status", "false");
		}
		return "addReport";
	}
}
