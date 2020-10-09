package com.nt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nt.dto.EmployeeDTO;
import com.nt.model.Employee;
import com.nt.service.IEmployeeMgmtService;

@Controller
public class EmployeeController {
	@Autowired
	private  IEmployeeMgmtService service;
	
	
	@GetMapping("/welcome.htm")
	public  String showHome() {
		return "home";  //lnv for home
	}
	
	@GetMapping("/list_emps.htm")
	public String  showEmployees(Map<String,Object> map) {
		List<EmployeeDTO> listDTO=null;
		//use service
		listDTO=service.fetchAllEmployees();
		//keep results in model attriute
		map.put("empsInfo", listDTO);
		return "show_report";
	}
	
	@GetMapping("/saveEmp.htm")  //for initial request
	public String  showEmpRegistrationPage(@ModelAttribute("empFrm") Employee emp) {
		System.out.println("EmployeeController.showEmpRegistrationPage()");
		return "employee_register";
	}
	
	@PostMapping("/saveEmp.htm")  //for post back request
	public  String  saveEmployee(RedirectAttributes redirect, @ModelAttribute("empFrm") Employee emp){
		System.out.println("EmployeeController.saveEmployee()");
		EmployeeDTO dto=null;
		String result=null;
		List<EmployeeDTO> listDTO=null;
		//convert model to dto
		dto=new EmployeeDTO();
		BeanUtils.copyProperties(emp,dto);
		//use Service
		result=service.registerEmployee(dto);
		//keep in results  in flash attribute (special Map object)
		  redirect.addFlashAttribute("resultMsg",result);
		//return lvn
		return "redirect:list_emps.htm";
		
	}
	
	@GetMapping("/deleteEmp.htm")
	public   String   removeEmployee(RedirectAttributes redirect,@RequestParam int eno) {
		String result=null;
		 //use service
		result=service.removeEmpByNo(eno);
		//add result to flash attribute
		 redirect.addFlashAttribute("resultMsg",result);
		return "redirect:list_emps.htm";
	}
	
	@ModelAttribute("deptsInfo")  //constructing reference data/initial for select box
	public List<Integer>  populateDeptNos(){
		System.out.println("EmployeeController.populateDeptNos()");
		//use service
			return service.fetchAllDeptNos();
	}
	
	

}