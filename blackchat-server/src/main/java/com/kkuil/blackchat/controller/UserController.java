package com.kkuil.blackchat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kkuil.blackchat.dao.UserDao;
import com.kkuil.blackchat.model.bo.MapDataInToken;
import com.kkuil.blackchat.model.common.page.PageRes;
import com.kkuil.blackchat.model.dto.user.AddUserDTO;
import com.kkuil.blackchat.model.dto.user.UpdateUserDTO;
import com.kkuil.blackchat.model.dto.user.UserLoginDTO;
import com.kkuil.blackchat.model.dto.user.UserRegistryDTO;
import com.kkuil.blackchat.model.entity.User;
import com.kkuil.blackchat.utils.JwtUtils;
import com.kkuil.blackchat.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kkuil.blackchat.constant.UserConst.*;

/**
 * @Author kkuil
 * @Date 2023/07/29 20:00
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserDao userDao;

    /**
     * 获取用户列表
     *
     * @param id       用户id
     * @param current  当前页
     * @param pageSize 每页大小
     * @return 用户列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取用户", description = "分页获取用户")
    @Parameters({
            @Parameter(name = "id", description = "用户id"),
            @Parameter(name = "current", description = "当前页"),
            @Parameter(name = "pageSize", description = "每页大小")
    })
    public ResultUtil<PageRes<List<User>>> page(String id, int current, int pageSize) {
        Page<User> pageInfo = new Page<>();
        pageInfo.setCurrent(current);
        pageInfo.setSize(pageSize);
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.like("id", id);
        IPage<User> page = userDao.page(pageInfo, userWrapper);
        PageRes<List<User>> pageRes = new PageRes<>();
        pageRes
                .setPageSize(page.getSize())
                .setCurrent(page.getCurrent())
                .setTotal(page.getTotal())
                .setData(page.getRecords());
        return ResultUtil.success("获取成功", pageRes);
    }

    /**
     * 添加用户
     *
     * @param addUserDTO 添加用户数据传输对象
     * @return 是否添加成功
     */
    @PostMapping("/")
    @Operation(summary = "添加用户", description = "添加用户")
    @Parameter(name = "addUserDTO", description = "添加用户数据传输对象")
    public ResultUtil<Boolean> add(@RequestBody AddUserDTO addUserDTO) {
        User tbUser = BeanUtil.copyProperties(addUserDTO, User.class);
        boolean isSave = userDao.save(tbUser);
        if (isSave) {
            return ResultUtil.success("添加成功", true);
        } else {
            return ResultUtil.error("添加失败", false);
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除用户")
    @Parameter(name = "id", description = "用户id")
    public ResultUtil<Boolean> del(@PathVariable("id") Long id) {
        boolean isSave = userDao.removeById(id);
        if (isSave) {
            return ResultUtil.success("删除成功", true);
        } else {
            return ResultUtil.error("删除失败", false);
        }
    }

    /**
     * 删除用户
     *
     * @param ids 用户id
     * @return 是否删除成功
     */
    @DeleteMapping("/")
    @Operation(summary = "删除一批用户", description = "删除一批用户")
    @Parameter(name = "ids", description = "用户ids")
    public ResultUtil<Boolean> del(String ids) {
        boolean isSave = userDao.removeByIds(Collections.singleton(ids));
        if (isSave) {
            return ResultUtil.success("删除成功", true);
        } else {
            return ResultUtil.error("删除失败", false);
        }
    }

    /**
     * @param updateUserDTO 修改用户数据传输对象
     * @return 是否修改成功
     * 修改用户
     */
    @PutMapping("/")
    @Operation(summary = "修改用户", description = "修改用户")
    @Parameter(name = "updateUserDTO", description = "修改用户数据传输对象")
    public ResultUtil<Boolean> update(@RequestBody UpdateUserDTO updateUserDTO) {
        User tbUser = BeanUtil.copyProperties(updateUserDTO, User.class);
        boolean isSave = userDao.updateById(tbUser);
        if (isSave) {
            return ResultUtil.success("修改成功", true);
        } else {
            return ResultUtil.error("修改失败", false);
        }
    }

    /**
     * @param id 用户id
     * @return 查询的数据
     * 查询用户
     */
    @GetMapping("/")
    @Operation(summary = "查询用户", description = "查询用户")
    @Parameter(name = "id", description = "查询用户数据传输对象")
    public ResultUtil<User> select(Long id) {
        return ResultUtil.success("查询成功", userDao.getById(id));
    }

    /**
     * 登录接口
     *
     * @param userLoginDTO 用户登录信息
     * @return token
     */
    @PostMapping("/login")
    @Operation(summary = "登录", description = "登录")
    @Parameter(name = "userLoginDTO", description = "用户登录信息")
    public ResultUtil<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userDao.getOne(userQueryWrapper);
        if (user == null) {
            return ResultUtil.error("用户不存在", null);
        }
        String pwdInTable = user.getPassword();
        String pwdInForm = DigestUtil.md5Hex(password + USER_ENCRYPT_VALUE);
        log.info("pwdInTable: {}", pwdInTable);
        log.info("pwdInForm: {}", pwdInForm);
        MapDataInToken mapDataInToken = MapDataInToken.builder().username(username).build();
        if (!pwdInTable.equals(pwdInForm)) {
            return ResultUtil.error("密码错误", null);
        }
        Map<String, Object> tokenMap = new HashMap<>(8);
        tokenMap = BeanUtil.beanToMap(mapDataInToken, tokenMap, false, true);
        String token = JwtUtils.create(tokenMap, USER_TOKEN_SECRET, USER_TOKEN_TTL);
        return ResultUtil.success("登录成功", token);
    }

    /**
     * 登录接口
     *
     * @param userRegistryDTO 用户登录信息
     * @return token
     */
    @PostMapping("/registry")
    @Operation(summary = "注册", description = "注册")
    @Parameter(name = "userRegistryDTO", description = "用户注册信息")
    public ResultUtil<Boolean> login(@Valid @RequestBody UserRegistryDTO userRegistryDTO) {
        String username = userRegistryDTO.getUsername();
        String password = userRegistryDTO.getPassword();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userDao.getOne(userQueryWrapper);
        if (user != null) {
            return ResultUtil.error("该用户名已被注册，请重新输入", false);
        }
        // md5(password + 盐值)
        password = DigestUtil.md5Hex(password + USER_ENCRYPT_VALUE);
        User newUser = User.builder()
                .username(username)
                .password(password)
                .build();
        userDao.save(newUser);
        return ResultUtil.success("注册成功", true);
    }

}
