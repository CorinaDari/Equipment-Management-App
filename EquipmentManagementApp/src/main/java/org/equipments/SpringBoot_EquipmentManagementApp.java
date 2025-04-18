package org.equipments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.logging.Logger;

@SpringBootApplication(scanBasePackages = "org.equipments")
public class SpringBoot_EquipmentManagementApp
{
	private static Logger logger = Logger.getLogger(SpringBoot_EquipmentManagementApp.class.getName());

	public static void main(String[] args) {
		logger.info("Loading ... :: SpringBoot - Equipment Application Default Settings ...  ");
		ApplicationContext ctx = SpringApplication.run(SpringBoot_EquipmentManagementApp.class, args);
	}
	
}