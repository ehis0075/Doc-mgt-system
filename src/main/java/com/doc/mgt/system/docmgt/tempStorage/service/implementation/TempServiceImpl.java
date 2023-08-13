package com.doc.mgt.system.docmgt.tempStorage.service.implementation;

import com.doc.mgt.system.docmgt.document.dto.UploadDocumentDTO;
import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.tempStorage.dto.*;
import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempAction;
import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
import com.doc.mgt.system.docmgt.tempStorage.model.Temp;
import com.doc.mgt.system.docmgt.tempStorage.repository.TempRepository;
import com.doc.mgt.system.docmgt.tempStorage.service.TempActionService;
import com.doc.mgt.system.docmgt.tempStorage.service.TempService;
import com.doc.mgt.system.docmgt.user.service.UserService;
import com.doc.mgt.system.docmgt.util.GeneralUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TempServiceImpl implements TempService {

    private final Gson gson;

    private final UserService adminService;
    private final GeneralService generalService;
    private final TempRepository tempRepository;
    private final TempActionService tempActionService;

    public TempServiceImpl(Gson gson, UserService adminService, GeneralService generalService, TempRepository tempRepository, TempActionService tempActionService) {
        this.gson = gson;
        this.adminService = adminService;
        this.generalService = generalService;
        this.tempRepository = tempRepository;
        this.tempActionService = tempActionService;
    }

    @Override
    public TempListDTO getTempListByTableName(TempListRequestDTO requestDTO, TableName tableName, TempStatus status) {
        log.info("::Getting Temp List ");

        Pageable paged = generalService.getPageableObject(requestDTO.getSize(), requestDTO.getPage());

        Page<Temp> tempPage;

        if (status.equals(TempStatus.ALL)) {
            tempPage = tempRepository.findAllByTableName(tableName, paged);
        } else {
            tempPage = tempRepository.findAllByTableNameAndStatus(tableName, status, paged);
        }

        TempListDTO listDTO = new TempListDTO();

        List<Temp> tempList = tempPage.getContent();
        if (tempPage.getContent().size() > 0) {
            listDTO.setHasNextRecord(tempPage.hasNext());
            listDTO.setTotalCount((int) tempPage.getTotalElements());
        }

        List<TempDTO> tempDTOS = convertToTempDTOList(tempList);
        listDTO.setTempDTOList(tempDTOS);

        return listDTO;
    }

    @Override
    public TempDTO getTempDTO(Long tempId) {
        log.info("Getting Temp using temp ID => {}", tempId);

        Temp temp = getTemp(tempId);

        return getTempDTO(temp);
    }


    @Override
    public Object getOldData(Long tempId) {

        Temp temp = getTemp(tempId);

        if (!temp.getStatus().equals(TempStatus.PENDING)) {
            throw new GeneralException(ResponseCodeAndMessage.AN_ERROR_OCCURRED_96.responseCode,
                    "No previous data for this record, Record has already been " + temp.getStatus());
        }

        if (Objects.isNull(temp.getOldDataId())) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode,
                    "No previous data for this record");
        }

        String serviceName = temp.getServiceName();
        Long oldDataId = temp.getOldDataId();
        TableName tableName = temp.getTableName();

        switch (tableName) {
            case DOCUMENT:
                return tempActionService.getDocumentData(serviceName, oldDataId);
            default:
                throw new GeneralException(ResponseCodeAndMessage.INVALID_JSON_REQUEST_DATA_90.responseCode,
                        "Invalid table name");
        }
    }

    @Override
    public TempResponseDTO saveToTemp(Object newData, Long oldDataId, TempAction action, TableName tableName,
                                      Object service, String username, String fileId) {
        log.info("Preparing to save temp transaction with image for table => {} by user => {} and action => {}", tableName, username, action);

        Temp temp = new Temp();

        temp.setFileId(fileId);

        return getTempResponseDTO(newData, oldDataId, action, tableName, service, username, temp);

    }

    @Override
    public TempResponseDTO saveToTemp(Object newData, Long oldDataId, TempAction action, TableName tableName, Object service, String username) {
        log.info("Preparing to save temp transaction for table => {} by user => {} and action => {}", tableName, username, action);

        Temp temp = new Temp();

        return getTempResponseDTO(newData, oldDataId, action, tableName, service, username, temp);
    }


    @Override
    public TempResponseDTO performTempAction(TempPerformActionDTO performActionDTO, Long tempId, String username) {
        log.info("verifying request for temp action {}, by {}", performActionDTO, username);

        TempStatus status = performActionDTO.getStatus();
        String reason = performActionDTO.getReason();

        if (Objects.isNull(status)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91);
        } else {
            if (status.equals(TempStatus.DECLINED) && GeneralUtil.stringIsNullOrEmpty(reason)) {
                throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode,
                        "Every declined record must have a reason");
            }
        }

        log.info("Performing Temp action on id => {} and status => {}", tempId, status.name());

        //validate that id exist
        Temp temp = getTemp(tempId);

        //check if user is not one who created
        if (temp.getCreatedBy().equals(username)) {
            throw new GeneralException(ResponseCodeAndMessage.OPERATION_NOT_SUPPORTED_93.responseCode, "User who uploaded cannot perform action on it");
        }

        //verifies that status is approved and it is currently pending
        if (!temp.getStatus().equals(TempStatus.PENDING)) {
            log.info("Record already treated");

            throw new GeneralException(ResponseCodeAndMessage.AN_ERROR_OCCURRED_96.responseCode,
                    "Record had previously been " + temp.getStatus().name());

        } else if (status.equals(TempStatus.APPROVED)) {
            log.info("Performing approved action");

            try {
                performApproveAction(temp.getTableName(), temp);

                updateTempRecord(TempStatus.APPROVED, reason, username, temp);
            } catch (Exception e) {
                updateTempRecord(TempStatus.DECLINED, e.getCause().getMessage(), username, temp);

                throw new GeneralException(e.getMessage(), e.getCause().getMessage());
            }

        } else if (status.equals(TempStatus.DECLINED)) {
            updateTempRecord(TempStatus.DECLINED, reason, username, temp);
        } else {
            throw new GeneralException(ResponseCodeAndMessage.AN_ERROR_OCCURRED_96.responseCode,
                    "Record is already " + temp.getStatus().name());
        }

        return tempResponse(status);
    }

    private TempResponseDTO getTempResponseDTO(Object newData, Long oldDataId, TempAction action, TableName tableName, Object service, String username, Temp temp) {
        String newDataString = gson.toJson(newData);
        String serviceName = getServiceName(service.getClass());

        temp.setTableName(tableName);
        temp.setServiceName(serviceName);
        temp.setNewData(newDataString);
        temp.setOldDataId(oldDataId);
        temp.setAction(action);
        temp.setStatus(TempStatus.PENDING);

        temp.setCreatedBy(username);
        tempRepository.saveAndFlush(temp);

        return TempResponseDTO.builder().result("Record saved successfully, awaiting approval").build();
    }

    private void updateTempRecord(TempStatus status, String reason, String username, Temp temp) {
        log.info("Updating temp record with id => {}, status => {} and reason => {}",
                temp.getId(), status.name(), reason);

        temp.setModifiedBy(username);
        temp.setStatus(status);
        temp.setReason(reason);
        tempRepository.save(temp);
    }


    private void performApproveAction(TableName tableName, Temp temp) {
        if (Objects.requireNonNull(tableName) == TableName.DOCUMENT) {
            tempActionService.performActionForDocument(temp);
        }
    }

    private Temp getTemp(Long id) {
        if (Objects.isNull(id)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91);
        }
        return tempRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88));
    }

    private TempResponseDTO tempResponse(TempStatus status) {
        return TempResponseDTO.builder()
                .result(status.name() + " Successfully").build();
    }

    private String getServiceName(Class<?> serviceClass) {
        String serviceName = serviceClass.getSimpleName();

        log.info("Getting service name for {}", serviceName);

        //check if special character exist
        int indexOfSpecialCharacter = serviceName.indexOf("$");

        String toLowerCaseFirstCharacter = serviceName.substring(0, 1).toLowerCase();
        if (indexOfSpecialCharacter == -1) {
            return toLowerCaseFirstCharacter + serviceName.substring(1);
        }
        return toLowerCaseFirstCharacter + serviceName.substring(1, indexOfSpecialCharacter);
    }

    private List<TempDTO> convertToTempDTOList(List<Temp> tempList) {
        log.info("Converting Temp List to Temp DTO List");

        return tempList.stream().map(this::getTempDTO).collect(Collectors.toList());
    }

    private TempDTO getTempDTO(Temp temp) {

        TempDTO tempDTO = new TempDTO();
        generalService.createDTOFromModel(temp, tempDTO);

        if (!GeneralUtil.stringIsNullOrEmpty(temp.getNewData())) {
            tempDTO.setNewData(getNewDataObject(temp));
        }
        return tempDTO;
    }

    private Object getNewDataObject(Temp temp) {

        if (Objects.requireNonNull(temp.getTableName()) == TableName.DOCUMENT) {
            return gson.fromJson(temp.getNewData(), UploadDocumentDTO.class);
        }
        return temp.getNewData();
    }
}
