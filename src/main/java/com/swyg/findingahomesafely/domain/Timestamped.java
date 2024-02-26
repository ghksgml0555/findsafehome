package com.swyg.findingahomesafely.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass // 상속했을 때, 컬럼으로 인식하게 합니다.
@EntityListeners(AuditingEntityListener.class) // 생성/수정 시간을 자동으로 반영하도록 설정
@Getter // Getter가 있어야 jackson을 사용하여 객체를 반환할 때 같이 반환된다
public abstract class Timestamped { // abstract는 상속으로만 사용할 수 있다

    @CreatedDate // 생성일자임을 나타냅니다.
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //TODO 형식 프론트랑 맞추기.
    @LastModifiedDate // 마지막 수정일자임을 나타냅니다.
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

//    @PrePersist
//    public void onPrePersist(){
//        this.createdAt = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
//        this.modifiedAt = this.modifiedAt;
//    }
//
//    @PreUpdate
//    public void onPreUpdate(){
//        this.modifiedAt = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
//    }


}