package com.doc.mgt.system.docmgt.tempStorage.controller;

import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempPerformActionDTO;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempResponseDTO;
import com.doc.mgt.system.docmgt.tempStorage.service.TempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/temps")
public class TempController {

    private final TempService tempService;
    private final GeneralService generalService;

    public TempController(TempService tempService, GeneralService generalService) {
        this.tempService = tempService;
        this.generalService = generalService;
    }

//    @PostMapping("/all/{tableName}")
//    public Response getAllTempsByTableName(@RequestBody TempListRequestDTO request,
//                                           @PathVariable TableName tableName,
//                                           @RequestParam(required = false, defaultValue = "ALL") TempStatus status) {
//
//        TempListDTO data = tempService.getTempListByTableName(request, tableName, status);
//        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
//    }


    @PostMapping("/performAction/{documentId}")
    public Response performTempAction(@RequestBody TempPerformActionDTO performActionDTO, @PathVariable Long documentId, Principal principal) {
        String loggedInUser = principal.getName();

        TempResponseDTO data = tempService.performTempActionForDoc(performActionDTO, documentId, loggedInUser);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }


}
