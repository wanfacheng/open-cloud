package com.opencloud.base.provider.controller;

import com.opencloud.base.client.api.BaseUserAccountRemoteApi;
import com.opencloud.base.client.model.AppUser;

import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.UserInfo;
import com.opencloud.base.client.model.entity.BaseUserAccount;
import com.opencloud.base.provider.service.BaseUserAccountService;
import com.opencloud.base.provider.service.BaseUserService;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号管理
 *
 * @author liuyadu
 */
@Slf4j
@Api(tags = "系统用户账号管理")
@RestController
public class BaseUserAccountController implements BaseUserAccountRemoteApi {
    @Autowired
    private BaseUserAccountService baseUserAccountService;
    @Autowired
    private BaseUserService baseUserService;

    /**
     * 获取登录账号信息
     *
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @PostMapping("/account/localLogin")
    @Override
    public ResultBody<UserAccount> localLogin(@RequestParam(value = "username") String username) {
        UserAccount account = baseUserAccountService.login(username);
        return ResultBody.ok().data(account);
    }

    /**
     * 注册第三方登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @ApiOperation(value = "注册第三方登录账号", notes = "仅限系统内部调用")
    @PostMapping("/account/register/thirdParty")
    @Override
    public ResultBody registerThirdPartyAccount(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName
    ) {
        Long userId = null;
        BaseUserAccount baseUserAccount = baseUserAccountService.registerThirdAccount(account, password, accountType, nickName);
        if (baseUserAccount != null) {
            userId = baseUserAccount.getUserId();
        }
        return ResultBody.ok().data(userId);
    }


    /**
     * 重置密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PostMapping("/account/reset/password")
    @Override
    public ResultBody resetPassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword") String newPassword
    ) {
        baseUserAccountService.resetPassword(userId, oldPassword, newPassword);
        return ResultBody.ok();
    }

    /**
     * 获取用户详细信息
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取用户详细信息", notes = "获取用户详细信息")
    @PostMapping("/user/info")
    @Override
    public ResultBody<UserInfo> getUserInfo(@RequestParam(value = "userId") Long userId) {
        return ResultBody.ok().data(baseUserService.getUserWithAuthoritiesById(userId));
    }

    /**
     * 获取App用户详细信息
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取App用户详细信息", notes = "获取App用户详细信息")
    @PostMapping("/user/appInfo")
    @Override
    public ResultBody<AppUser> getAppUserInfo(@RequestParam(value = "userId") Long userId) {
        return ResultBody.ok().data(baseUserService.getAppUserWithByUserId(userId));
    }


    /**
     * App初始化登录
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "App初始化登录", notes = "App初始化登录")
    @PostMapping("/login/init")
    @Override
    public ResultBody<AppUser> loginInit() {
        return ResultBody.ok().data(baseUserService.loginInit());
    }

    /**
     * 获取app登录信息
     *
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "获取app登录信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path")})
    @PostMapping("/account/appLogin")
    @Override
    public ResultBody<UserAccount> appLogin(@RequestParam(value = "username") String username) {
        UserAccount account = baseUserAccountService.applogin(username);
        return ResultBody.ok().data(account);
    }


}
