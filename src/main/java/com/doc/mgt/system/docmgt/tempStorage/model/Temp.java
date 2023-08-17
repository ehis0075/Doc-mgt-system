//package com.doc.mgt.system.docmgt.tempStorage.model;
//
//
//import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
//import com.doc.mgt.system.docmgt.tempStorage.enums.TempAction;
//import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.hibernate.annotations.Where;
//
//import javax.persistence.*;
//
////A temporary storage for all record, pending approval
//@Data
//@Entity
//public class Temp {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    private TableName tableName;
//
//    private String serviceName;
//
//    @Column(name = "new_data")
//    private String newData;
//
//    private Long oldDataId;
//
//    @Enumerated(EnumType.STRING)
//    private TempAction action;
//
//    @Enumerated(EnumType.STRING)
//    private TempStatus status;
//
//    private String reason;
//
//    private String fileId;
//
//    private String url;
//
//    private String modifiedBy;
//
//    private String createdBy;
//
//}
