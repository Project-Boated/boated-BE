package com.projectboated.backend.domain.task.taskfile.entity;

import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class TaskFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    @Builder
    public TaskFile(Long id, Task task, UploadFile uploadFile) {
        this.id = id;
        this.task = task;
        this.uploadFile = uploadFile;
    }

    public String getKey() {
        return "projects/" + "tasks/" + task.getId() + "/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt();
    }

}
