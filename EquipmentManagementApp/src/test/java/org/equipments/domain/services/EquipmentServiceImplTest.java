package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EquipmentServiceImplTest {

    @Test
    public void testAdvancedMethods() {
        EquipmentService service = new EquipmentServiceImpl();


        service.addEquipment(new Equipment(1, "Excavator", "Heavy Machinery", "Available", "Site A", 10000.0));
        service.addEquipment(new Equipment(2, "Bulldozer", "Heavy Machinery", "In Use", "Site B", 20000.0));
        service.addEquipment(new Equipment(3, "Drill", "PowerTool", "Available", "Warehouse", 500.0));

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
