package org.equipments.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity
public class Report {
    @Id @GeneratedValue
    private int reportId;
    private String reportType;
    private Date generatedDate;
    private String content;
    @ManyToMany(mappedBy = "reports")
    private List<Equipment> equipmentList;

}
