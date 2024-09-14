package com.bolsadeideas.springboot.backend.apirest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import com.bolsadeideas.springboot.backend.apirest.controllers.ClienteRestController;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
//import java.util.Map;
import java.util.Date;

import static org.mockito.Mockito.*;



@RunWith(SpringRunner.class)
@SpringBootTest

public class SpringBootBackendApirestApplicationTests {

    @InjectMocks
    private ClienteRestController clienteRestController; // Clase que estamos probando

    @Mock
    private IClienteService clienteService; // Mock del servicio

    private Cliente cliente; // Cliente de ejemplo para las pruebas

    @Before
    public void setUp() {
    	  MockitoAnnotations.openMocks(this); // Usa openMocks para inicializar los mocks
    	
    	  clienteService = Mockito.mock(IClienteService.class);
       clienteRestController = new ClienteRestController(clienteService);

        // Crear un cliente de ejemplo
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("John");
        cliente.setApellido("Doe");
        cliente.setEmail("john.doe@example.com");
        cliente.setCreateAt(new Date());
    }

    @Test
    public void testIndex() {
        // Depuración: Verificar que el mock no sea null
        System.out.println("clienteService: " + clienteService);
        assertNotNull(clienteService);

        // Preparar datos simulados
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.findAll()).thenReturn(clientes);

        // Llamar al método del controlador
        ResponseEntity<List<Cliente>> response = clienteRestController.index();

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John8", response.getBody().get(0).getNombre());

        // Verificar que se llamó al servicio
        verify(clienteService, times(1)).findAll();
    }
    
    // Prueba para el método create (POST /clientes)
    @Test
    public void testCreate() {
    	 // Preparar el comportamiento del mock para un método void
        doNothing().when(clienteService).save(any(Cliente.class));

        // Llamar al método del controlador
        Cliente result = clienteRestController.create(cliente);

        // Verificar que el cliente es el correcto
        assertNotNull(result);
        assertEquals("John", result.getNombre());

        // Verificar que se llamó al servicio
        verify(clienteService, times(1)).save(cliente);
    }
    
    // Prueba para el método update (PUT /clientes/{id})
    @Test
    public void testUpdate() {
        // Preparar el comportamiento del mock
        when(clienteService.findById(1L)).thenReturn(cliente);
     // Preparar el comportamiento del mock para un método void
        doNothing().when(clienteService).save(any(Cliente.class));

        // Llamar al método del controlador
        Cliente updatedCliente = new Cliente();
        updatedCliente.setNombre("Jane");
        updatedCliente.setApellido("Doe");
        updatedCliente.setEmail("jane.doe@example.com");

        Cliente result = clienteRestController.update(updatedCliente, 1L);

        // Verificar que el cliente fue actualizado correctamente
        assertNotNull(result);
        assertEquals("Jane", result.getNombre());

        // Verificar que se llamó al servicio
        verify(clienteService, times(1)).findById(1L);
        verify(clienteService, times(1)).save(any(Cliente.class));
    }
    
    
 // Prueba para el caso de cliente no encontrado en delete
    @Test
   public  void testDeleteNotFound() {
        // Simular que el cliente no existe
        when(clienteService.findById(1L)).thenReturn(null);

        // Llamar al método del controlador
        ResponseEntity<Map<String, Object>> response = clienteRestController.delete(1L);

        // Verificar el resultado
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verificar que el servicio no intentó eliminar nada
        verify(clienteService, never()).delete(any(Cliente.class));
    }


}
