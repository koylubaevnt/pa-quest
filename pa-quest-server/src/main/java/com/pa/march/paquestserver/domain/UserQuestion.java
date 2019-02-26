package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_question")
@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@ToString(exclude = "userQuest")
public class UserQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_user_question_id")
    @SequenceGenerator(name = "gen_user_question_id", sequenceName = "user_question_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "finish")
    private LocalDateTime finish;

    //@OneToOne(fetch = FetchType.LAZY,
    //    mappedBy = "answer",
    //    optional = false)
    //private Answer userAnswer;

    @Column(name = "count_attempts", nullable = false)
    private Integer numberOfAttempts;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "seq", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_quest_id", nullable = false)
    private UserQuest userQuest;

}
