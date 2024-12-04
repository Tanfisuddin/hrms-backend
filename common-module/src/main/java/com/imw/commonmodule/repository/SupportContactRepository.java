package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.SupportContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SupportContactRepository extends JpaRepository<SupportContact, Long> {

    @Query("SELECT c FROM SupportContact c ORDER BY c.id DESC")
    Page<SupportContact> findAllOrderByIdDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM SupportContact c WHERE c.id IN :ids")
    int deleteByIds(List<Long> ids);


}