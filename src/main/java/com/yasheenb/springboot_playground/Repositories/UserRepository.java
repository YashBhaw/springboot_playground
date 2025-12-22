package com.yasheenb.springboot_playground.Repositories;

import com.yasheenb.springboot_playground.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
