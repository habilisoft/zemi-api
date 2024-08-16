package org.habilisoft.zemi.shared.infra;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "use_case_executions")
@EqualsAndHashCode(of = "id", callSuper = false)
class UseCaseExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(name = "command")
    String command;
    @Type(JsonType.class)
    @Column(name = "result")
    String result;
    @Column(name = "execution_time")
    Long executionTime;
    @Column(name = "name")
    private String useCaseName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "error_message")
    private String error;
    private String idempotencyKey;
    public enum Status {
        SUCCESS, FAILURE
    }

    static UseCaseExecution newExecutionOf(String useCaseName, String command, String userName, LocalDateTime date) {
        UseCaseExecution useCaseExecution = new UseCaseExecution();
        useCaseExecution.useCaseName = useCaseName;
        useCaseExecution.command = command;
        useCaseExecution.userName = userName;
        useCaseExecution.date = date;
        return useCaseExecution;
    }
    void failed(String reason) {
        this.status = Status.FAILURE;
        this.error = reason;
    }

    void succeeded(String result, long executionTime) {
        this.status = Status.SUCCESS;
        this.result = result;
        this.executionTime = executionTime;
    }
}
