package rest.services;

import org.equipments.classes.Equipment;
import org.equipments.dto.EquipmentDTO;
import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_EquipmentRestService {

    private static final Logger logger = Logger.getLogger(Test_EquipmentRestService.class.getName());
    private static final String BASE_URL = "/api/equipment";
    private static RestTemplate restTemplate = new RestTemplate();
    private static int testEquipmentId;

    @Test
    @Order(1)
    public void test_addEquipment() {
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setName("Test Equipment");
        equipmentDTO.setType("Type1");
        equipmentDTO.setStatus("Available");
        equipmentDTO.setPrice(1500.00);
        equipmentDTO.setLocation("Warehouse");

        ResponseEntity<EquipmentDTO> response = restTemplate.postForEntity(BASE_URL, equipmentDTO, EquipmentDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getEquipmentId());
    }

    @Test
    @Order(2)
    public void testGetEquipmentById() {
        logger.info("Testing: Get Equipment by ID");

        String url = BASE_URL + "/" + testEquipmentId;
        ResponseEntity<EquipmentDTO> response = restTemplate.getForEntity(url, EquipmentDTO.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testEquipmentId, response.getBody().getEquipmentId());
    }

    @Test
    @Order(3)
    public void testUpdateEquipment() {
        logger.info("Testing: Update Equipment");

        String url = BASE_URL + "/" + testEquipmentId;


        EquipmentDTO updatedEquipmentDTO = new EquipmentDTO("Updated Equipment", 2000.00, "In Use", "On Site");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EquipmentDTO> request = new HttpEntity<>(updatedEquipmentDTO, headers);


        ResponseEntity<EquipmentDTO> response = restTemplate.exchange(url, HttpMethod.PUT, request, EquipmentDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Equipment", response.getBody().getName());
    }

    @Test
    @Order(4)
    public void testGetAllEquipment() {
        logger.info("Testing: Get All Equipment");


        ResponseEntity<List> response = restTemplate.getForEntity(BASE_URL, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(5)
    public void testCalculateTotalValue() {
        logger.info("Testing: Calculate Total Value");


        String url = BASE_URL + "/total-value";
        ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);
    }

    @Test
    @Order(6)
    public void testDeleteEquipment() {
        logger.info("Testing: Delete Equipment");


        String url = BASE_URL + "/" + testEquipmentId;
        restTemplate.delete(url);

        ResponseEntity<EquipmentDTO> response = restTemplate.getForEntity(url, EquipmentDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }}
