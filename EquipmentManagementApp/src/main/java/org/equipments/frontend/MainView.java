package org.equipments.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H1("Welcome to the Equipment Management App!"));

        Button goToAddEquipment = new Button("Add equipment", event->
                UI.getCurrent().navigate("addEquipment"));

        add(goToAddEquipment);
    }}
