//package com.example.ddemo.controller;
//
//import com.example.ddemo.Service.UserService;
//import com.example.ddemo.common.Result;
//import com.example.ddemo.entity.NewUser;
//import com.example.ddemo.mapper.UserMapper;
//import com.example.ddemo.utils.TokenUtils;
//import com.example.ddemo.vo.Page;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class userController {
//
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/alluser")
//    public List<NewUser> query(){
//        List<NewUser> list = userMapper.selectList(null);
//        System.out.println(list);
//        return list;
//    }
//
//    @PostMapping("/newuser")
//    public String save(NewUser user){
//        int i = userMapper.insert(user);
//        if (i > 0){
//            return "Success!";
//        }else return "Error!";
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String deleteUser(@PathVariable("id") long id){
//        userMapper.deleteById(id);
//        return "Succcess!";
//    }
//
//    @GetMapping("/query")
//    public Result queryUser(@RequestAttribute("userId") String userId){
//        NewUser user = userMapper.selectById(userId);
//        return Result.success(user);
//    }
//
//    @GetMapping("/page")
//    public Page<NewUser> findByPage(@RequestParam(defaultValue = "1") Integer pageNum,
//                                 @RequestParam(defaultValue = "10") Integer pageSize){
//        Integer offset = (pageNum - 1) * pageSize;  // 偏移量
//        List<NewUser> list = userMapper.findByPage(offset, pageSize);
//        Page<NewUser> page = new Page<>();
//        page.setData(list);
//        page.setTotal(userMapper.userCount());
//        page.setPageNum(pageNum);
//        page.setPageSize(pageSize);
//        return page;
//    }
//
////    @CrossOrigin
////    @PostMapping("/login")
////    public ResponseEntity<?> loginUser(@RequestBody LoginUser loginuser) {
////        String username = loginuser.getUsername();
////        String password = loginuser.getPassword();
////        System.out.println(username);
////        // 假设你有一个查询数据库的方法来验证用户名和密码
////        NewUser user = userMapper.findUserByUsernameAndPassword(username, password);
////        System.out.println(user);
////        if (user != null) {
////            // 用户验证成功
////            // 你可能不想返回密码，所以将它设置为null或者移除
////            user.setPassword(null);
////            return ResponseEntity.ok(user);
////        } else {
////            // 用户验证失败
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
////        }
////    }
//
//    @CrossOrigin
//    @PostMapping("/login")
//    public Result login(@RequestBody NewUser user) {
////        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
////            return Result.error("数据输入不合法");
////        }
//        user = userService.login(user);
//        return Result.success(user);
//    }
//}
