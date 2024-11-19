package com.istafrica.signature.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.istafrica.signature.domain.entity.Company;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
