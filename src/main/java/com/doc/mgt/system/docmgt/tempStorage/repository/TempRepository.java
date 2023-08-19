//package com.doc.mgt.system.docmgt.tempStorage.repository;
//
//
//import com.doc.mgt.system.docmgt.tempStorage.enums.TableName;
//import com.doc.mgt.system.docmgt.tempStorage.enums.TempStatus;
//import com.doc.mgt.system.docmgt.tempStorage.model.Temp;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface TempRepository extends JpaRepository<Temp, Long> {
//    Page<Temp> findAllByTableName(TableName tableName, Pageable pageable);
//
//    Page<Temp> findAllByTableNameAndStatus(TableName tableName, TempStatus status, Pageable pageable);
//}
