/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.genesis.usermgt.ws.service.impl;

import org.genesis.usermgt.ws.io.repositories.UserRepository;
import org.genesis.usermgt.ws.io.entity.UserEntity;
import org.genesis.usermgt.ws.service.UserService;
import org.genesis.usermgt.ws.shared.Utils;
import org.genesis.usermgt.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto user) {

        UserEntity existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email address : " + user.getEmail() + " already registered");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setUserId(utils.generateUUID());
        userEntity.setHashedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserEntity persistedUser = userRepository.save(userEntity);
        user = new UserDto();
        BeanUtils.copyProperties(persistedUser, user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(email, userEntity.getHashedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("Email address : " + email + " not found");
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }
}
