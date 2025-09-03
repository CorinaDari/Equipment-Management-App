package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository; 

    @InjectMocks
    private EquipmentServiceImpl service; 

    @Test
    public void testAdvancedMethods() {

        
        Equipment excavator = new Equipment(1, "Excavator", "Heavy Machinery", "Available", "Site A", 10000.0);
        Equipment bulldozer = new Equipment(2, "Bulldozer", "Heavy Machinery", "In Use", "Site B", 20000.0);
        Equipment drill = new Equipment(3, "Drill", "PowerTool", "Available", "Warehouse", 500.0);

      
        when(equipmentRepository.findAll()).thenReturn(Arrays.asList(excavator, bulldozer, drill));
        when(equipmentRepository.findById(1)).thenReturn(java.util.Optional.of(excavator));
        when(equipmentRepository.findById(2)).thenReturn(java.util.Optional.of(bulldozer));
        when(equipmentRepository.findById(3)).thenReturn(java.util.Optional.of(drill));
        when(equipmentRepository.findByLocation("Site A")).thenReturn(Arrays.asList(excavator));
        when(equipmentRepository.findByStatus("Available")).thenReturn(Arrays.asList(excavator, drill));

  
        service.addEquipment(excavator);
        service.addEquipment(bulldozer);
        service.addEquipment(drill);

  
        double totalValue = service.calculateTotalValue();
        assertEquals(30500.0, totalValue);

        Equipment mostExpensive = service.findMostExpensiveEquipment();
        assertNotNull(mostExpensive); 
        assertEquals(20000.0, mostExpensive.getPrice()); 

        assertTrue(service.isEquipmentAvailable(1)); 
        assertFalse(service.isEquipmentAvailable(2));
        
        
        List<Equipment> filtered = service.filterByPriceRange(1000.0, 20000.0);
        assertEquals(1, filtered.size()); 
        assertEquals("Bulldozer", filtered.get(0).getName());
    }
}

