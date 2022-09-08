package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.invitation.repository.InvitationRepository;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.repository.ChattingRoomRepository;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.domain.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;

public class ChattingRoomRepositoryTest extends ProjectVideoRepositoryTest {

    @Autowired
    protected ChattingRoomRepository projectChattingRoomRepository;

    protected ChattingRoom insertChattingRoom() {
        return projectChattingRoomRepository.save(createChattingRoom());
    }

}
