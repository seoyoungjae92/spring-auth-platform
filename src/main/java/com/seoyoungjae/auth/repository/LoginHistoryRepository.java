package com.seoyoungjae.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seoyoungjae.auth.domain.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

  Optional<LoginHistory> findTopByUserIdOrderByLoginAtDesc(Long userId);

  List<LoginHistory> findAllByUserId(Long userId);
}
