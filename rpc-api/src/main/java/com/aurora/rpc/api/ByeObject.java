package com.aurora.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 测试用api的实体
 *
 * @author lc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ByeObject implements Serializable {

    private Integer id;
    private String message;

}
