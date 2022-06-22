package my.sleepydeveloper.projectcompass.domain.kanban.entity;

public enum KanbanLaneType {
    READY("대기중"), PROCESS("진행중"), CHECKING("확인중"), COMPLETE("완료");

    String name;

    KanbanLaneType(String name) {
        this.name = name;
    }

}
