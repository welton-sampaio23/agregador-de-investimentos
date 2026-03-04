package com.welton.agregadorinvestimentos.repository;

import com.welton.agregadorinvestimentos.entity.BillingAddress;
import com.welton.agregadorinvestimentos.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {
}
