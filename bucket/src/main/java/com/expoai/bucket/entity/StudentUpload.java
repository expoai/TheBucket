package com.expoai.bucket.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idExterne;

    @OneToOne
    @JoinColumn(name = "team_id") // references User.id
    private User team ;

    private String url ;

    private String tag1 ;
    private String tag2 ;
    private String tag3 ;

}
