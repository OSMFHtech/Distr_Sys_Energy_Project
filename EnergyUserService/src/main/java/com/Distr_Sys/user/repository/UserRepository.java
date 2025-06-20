package com.Distr_Sys.user.repository;

import com.Distr_Sys.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserProfile, Long> { }
