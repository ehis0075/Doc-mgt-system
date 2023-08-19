package com.doc.mgt.system.docmgt.tempStorage.controller;

import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempPerformActionDTO;
import com.doc.mgt.system.docmgt.tempStorage.dto.TempResponseDTO;
import com.doc.mgt.system.docmgt.tempStorage.service.TempService;
import com.doc.mgt.system.docmgt.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/temps")
public class TempController {

    private final TempService tempService;
    private final GeneralService generalService;

    private final UserService userService;

    public TempController(TempService tempService, GeneralService generalService, UserService userService) {
        this.tempService = tempService;
        this.generalService = generalService;
        this.userService = userService;
    }

    @PostMapping("/performAction/{documentId}")
    public Response performTempAction(@RequestBody TempPerformActionDTO performActionDTO, @PathVariable Long documentId) {

        String user = userService.getLoggedInUser();

        TempResponseDTO data = tempService.performTempActionForDoc(performActionDTO, documentId, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
