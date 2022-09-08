package com.projectboated.backend.domain.projectchatting.chatting.domain;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter
public class Chatting extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String body;

    @Builder
    public Chatting(Long id, ChattingRoom chattingRoom, Account account, String body) {
        this.id = id;
        this.chattingRoom = chattingRoom;
        this.account = account;
        this.body = body;
    }
}
