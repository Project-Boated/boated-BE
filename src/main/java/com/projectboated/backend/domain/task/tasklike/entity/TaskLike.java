package com.projectboated.backend.domain.task.tasklike.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.task.task.entity.Task;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskLike extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_like_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id")
	private Task task;

	@Column(name = "order_index")
	private Integer order;
	
	@Builder
	public TaskLike(Long id, Account account, Task task, Integer order) {
		this.id = id;
		this.account = account;
		this.task = task;
		this.order = order;
	}

	public void changeOrder(Integer order) {
		this.order = order;
	}
}
