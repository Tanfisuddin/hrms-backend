package com.imw.commonmodule.repository;

import com.imw.commonmodule.enums.accessories.DeviceType;
import com.imw.commonmodule.persistence.Accessory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    Page<Accessory> findAllByUserOrganizationId(Long organizationId, Pageable pageable);
    Page<Accessory> findAllByUserOrganizationIdAndUserFullNameContaining(Long organizationId,String username, Pageable pageable);
    Page<Accessory> findAllByUserOrganizationIdAndUserFullNameContainingAndDeviceType(Long organizationId,String username, DeviceType deviceType, Pageable pageable);
    Page<Accessory> findAllByUserOrganizationIdAndDeviceType(Long organizationId, DeviceType deviceType, Pageable pageable);

    Boolean existsByIdAndUserOrganizationId(Long id, Long organizationId);
    List<Accessory> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
