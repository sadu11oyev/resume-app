package org.example.resumeorg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Experience {

    private String name;
    private LocalDate froms;
    private LocalDate tos;

}
