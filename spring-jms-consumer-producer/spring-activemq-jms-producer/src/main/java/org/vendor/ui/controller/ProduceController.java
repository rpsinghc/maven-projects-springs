package org.vendor.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.vendor.backend.service.MessagingService;
import org.vendor.ui.model.Vendor;

@Controller
public class ProduceController {
	Logger log = LoggerFactory.getLogger(ProduceController.class);
	
	@Autowired
	MessagingService messagingService;
	
	
	@GetMapping("/")
	public String renderVenderPage(Model model) {
		model.addAttribute("vendor", new Vendor()); 
	    return "index"; 
	}
	@RequestMapping(value="/vendor", method=RequestMethod.POST)
	public ModelAndView processRequest(@ModelAttribute("vendor") Vendor vendor, Model model){
		System.out.println("ddddddddddddd");
		messagingService.process(vendor);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		mv.addObject("message", "Message Added Successfully");
		return mv;
	}
	

}
