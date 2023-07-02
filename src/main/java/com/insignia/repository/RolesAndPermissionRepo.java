package com.insignia.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insignia.entity.RolesAndPermissions;

public interface RolesAndPermissionRepo extends JpaRepository<RolesAndPermissions, Serializable> {

}
