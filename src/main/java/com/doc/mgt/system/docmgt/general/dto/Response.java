package com.doc.mgt.system.docmgt.general.dto;

import lombok.Data;

@Data
public class Response {
    private String responseCode;

    private String responseMessage;

    private Object data;
}
