package com.doc.mgt.system.docmgt.tempStorage.controller;

import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.tempStorage.dto.*;
import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import com.doc.mgt.system.docmgt.tempStorage.service.TempService;
import com.doc.mgt.system.docmgt.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/all/{tableName}")
    public Response getAllTempsByTableName(@RequestBody TempListRequestDTO request,
                                           @PathVariable TableName tableName,
                                           @RequestParam(required = false, defaultValue = "ALL") TempStatus status) {

        TempListDTO data = tempService.getTempListByTableName(request, tableName, status);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping("/{id}")
    public Response getOneTemp(@PathVariable Long id) {
        TempDTO data = tempService.getTempDTO(id);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping("/oldData/{tempId}")
    public Response getOldData(@PathVariable Long tempId) {
        Object data = tempService.getOldData(tempId);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping("/performAction/{tempId}")
    public Response performTempAction(@RequestBody TempPerformActionDTO performActionDTO, @PathVariable Long tempId, Principal principal) {
        String loggedInUser = principal.getName();

        TempResponseDTO data = tempService.performTempAction(performActionDTO, tempId, loggedInUser);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }


}
