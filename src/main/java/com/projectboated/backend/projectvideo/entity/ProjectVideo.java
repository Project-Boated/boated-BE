package com.projectboated.backend.projectvideo.entity;

import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.project.entity.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class ProjectVideo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_video_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_file_id")
    private UploadFile uploadFile;

    @Builder
    public ProjectVideo(Long id, Project project, UploadFile uploadFile) {
        this.id = id;
        this.project = project;
        this.uploadFile = uploadFile;
    }

    public String getKey() {
        return "projects/" + project.getId() + "/video/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectVideo that = (ProjectVideo) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
