package com.service.department.repository;

import com.service.department.entity.Department;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
             SELECT d
             FROM Department d
             WHERE d.id = :id
            """)
    Optional<Department> findByIdForUpdate(Long id);

}
