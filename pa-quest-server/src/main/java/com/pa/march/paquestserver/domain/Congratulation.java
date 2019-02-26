package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "congratulation")
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Congratulation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_congratulation_id")
    @SequenceGenerator(name = "gen_congratulation_id", sequenceName = "congratulation_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "video_id", unique = true)
    @NotNull
    @Size(max = 100)
    private String videoId;
}
