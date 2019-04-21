package com.github.mgrzeszczak.stages.domain;

import java.time.ZonedDateTime;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@MappedSuperclass
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class BaseEntity<T> {

    @javax.persistence.Id
    @Embedded
    protected Id id = Id.create();

    @CreatedBy
    protected String createdBy;

    @LastModifiedBy
    protected String modifiedBy;

    @CreationTimestamp
    protected ZonedDateTime createdDate;

    @UpdateTimestamp
    protected ZonedDateTime modifiedDate;

}
