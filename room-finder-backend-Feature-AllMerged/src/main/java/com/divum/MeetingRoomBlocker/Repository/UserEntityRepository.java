package com.divum.MeetingRoomBlocker.Repository;

import com.divum.MeetingRoomBlocker.Entity.Enum.RoleEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.divum.MeetingRoomBlocker.DTO.InternalUserDTO;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String emailname);

    List<UserEntity> findByEmailIn(List<String> emailList);


    List<UserEntity> findByRole(RoleEntity roleEntity);

    Optional<UserEntity> findUserNameById(Long userId);
}

