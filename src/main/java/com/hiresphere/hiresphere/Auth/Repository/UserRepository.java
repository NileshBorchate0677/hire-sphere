package com.hiresphere.hiresphere.Auth.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{

}
 