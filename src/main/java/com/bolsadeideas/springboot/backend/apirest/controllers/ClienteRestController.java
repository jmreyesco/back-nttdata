package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

//Manage errors
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.LoggerFactory;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);

	@GetMapping("/clientes")
	public ResponseEntity<List<Cliente>> index() {
		try {
			//return clienteService.findAll();
			log.info("Ingreso a metodo index");
			  List<Cliente> clientes = clienteService.findAll();
	            return ResponseEntity.ok(clientes); // 200 OK
		} catch (Exception e) {
			// TODO: handle exception
			// Captura y maneja cualquier excepción inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
		}
		
	}

	@GetMapping("/clientes/{id}")
	public Cliente show(@PathVariable Long id) {
		return this.clienteService.findById(id);
	}

	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente create(@RequestBody Cliente cliente) {
		cliente.setCreateAt(new Date());
		this.clienteService.save(cliente);
		return cliente;
	}

	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente currentCliente = this.clienteService.findById(id);
		currentCliente.setNombre(cliente.getNombre());
		currentCliente.setApellido(cliente.getApellido());
		currentCliente.setEmail(cliente.getEmail());
		this.clienteService.save(currentCliente);
		return currentCliente;
	}

	/*@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		Cliente currentCliente = this.clienteService.findById(id);
		this.clienteService.delete(currentCliente);
	}*/
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
	    Cliente currentCliente = this.clienteService.findById(id);
	    
	    if (currentCliente == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
	    }
	    
	    this.clienteService.delete(currentCliente);
	    response.put("mensaje", "El cliente eliminado con éxito!");
	    return ResponseEntity.status(HttpStatus.OK).body(response); // 200 OK
	}

	
	
}
