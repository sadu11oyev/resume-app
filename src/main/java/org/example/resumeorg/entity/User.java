package org.example.resumeorg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String firstName;
    private String lastName;
    private String fatherName;
    private Integer age;
    private String phoneNumber;

    private List<Technology> technologies;
    private List<Education> educations;
    private List<Experience> experiences;


}
