package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.profileimage.repository.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileImageRepositoryTest extends AccountRepositoryTest{

    @Autowired
    protected ProfileImageRepository profileImageRepository;



}
