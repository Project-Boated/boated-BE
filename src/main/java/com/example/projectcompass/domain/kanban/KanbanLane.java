package com.example.projectcompass.domain.kanban;

import com.example.projectcompass.domain.common.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
public class KanbanLane extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_lane_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;
}
