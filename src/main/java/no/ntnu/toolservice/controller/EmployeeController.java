package no.ntnu.toolservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.toolservice.entity.Employee;
import no.ntnu.toolservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("employees")
public class EmployeeController {
	private EmployeeRepository repository;
	private ObjectMapper mapper;

	@Autowired
	public EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
		this.mapper = new ObjectMapper();
	}

	@RequestMapping(value = "", method = GET)
	public ResponseEntity<String> getAllEmployees() {
		String resp = "";
		try {
			resp = mapper.writeValueAsString(repository.findAll());
		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>(e.getMessage(), BAD_REQUEST);
		}
		return new ResponseEntity<String>(resp, OK);
	}

	@RequestMapping(value = "/{id}", method = GET)
	public ResponseEntity<String> getSingleEmployee(@PathVariable(value = "id") long employee_id) {
		String resp = "";

		try {
			Employee employee = repository.findEmployeeById(employee_id);
			resp = mapper.writeValueAsString(employee);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<String>("Employee does not exist", BAD_REQUEST);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Something went wrong...", INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(resp, OK);
	}
}