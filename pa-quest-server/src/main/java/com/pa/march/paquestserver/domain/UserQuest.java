package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_quest")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UserQuest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_user_quest_id")
    @SequenceGenerator(name = "gen_user_quest_id", sequenceName = "user_quest_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "finish")
    private LocalDateTime finish;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = {
            CascadeType.ALL
        },
        fetch = FetchType.LAZY,
        mappedBy = "userQuest")
    private List<UserQuestion> userQuestions = new ArrayList<>();

}
