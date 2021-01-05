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

package org.genesis.usermgt.ws.ui.controller;

import org.genesis.usermgt.ws.service.UserService;
import org.genesis.usermgt.ws.shared.dto.UserDto;
import org.genesis.usermgt.ws.ui.model.request.UserRequestModel;
import org.genesis.usermgt.ws.ui.model.response.UserResponseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public void getUsers() {
        System.out.println("get users");
    }

    @PutMapping
    public void updateUsers() {
        System.out.println("update users");
    }

    @PostMapping
    public UserResponseModel createUser(@RequestBody UserRequestModel userRequestModel) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);

        UserResponseModel userResponse = new UserResponseModel();
        BeanUtils.copyProperties(createdUser, userResponse);

        return userResponse;
    }
}
