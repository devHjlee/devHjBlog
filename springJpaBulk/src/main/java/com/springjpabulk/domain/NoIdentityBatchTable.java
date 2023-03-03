package com.springjpabulk.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoIdentityBatchTable {
    @Id
    long id;
    @Column
    String name;

    @Builder
    public NoIdentityBatchTable(long id, String name){
        this.id = id;
        this.name = name;
    }
}
