package com.projectboated.backend.task.task.entity;

import com.projectboated.backend.account.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataAccountTask.ACCOUNT_TASK_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountTask : Entity 단위 테스트")
class AccountTaskTest {

    @Test
    void 생성자_AccountTask생성_return_생성된AccountTask() {
        // Given
        Account account = Account.builder()
                .build();
        Task task = Task.builder()
                .build();

        // When
        AccountTask accountTask = new AccountTask(ACCOUNT_TASK_ID, account, task);

        // Then
        assertThat(accountTask.getAccount()).isEqualTo(account);
        assertThat(accountTask.getTask()).isEqualTo(task);
    }

}