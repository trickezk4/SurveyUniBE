package com.example.survey_uni.service;

import com.example.survey_uni.dto.response.LoginResponse;
import com.example.survey_uni.model.User;
import com.example.survey_uni.repository.UserRepository;
import com.example.survey_uni.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

//    public Integer register(RegisterRequest request){
//        User user = new User();
//
//        if(userRepository.findByUsername(request.getUsername()).isPresent()){
//            return 0; // Đã có username này
//        } else {
//            user.setUsername(request.getUsername());
//            //Mã hóa mật khẩu trước khi lưu
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            user.setRole(User.Role.valueOf(request.getRole()));
//            user.setIsActive(true);
//
//            userRepository.save(user);
//
//            if(userRepository.findByUsername(request.getUsername()).isPresent()){
//                return 1; // Tạo tài khoản thành công
//            }
//            return -1; // Tạo tài khoản lỗi
//        }
//    }

//    public Integer login(LoginRequest request){
//        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
//
//        if(userOpt.isPresent()){
//            User user = userOpt.get();
//            //So sánh mật khẩu đã nhập với mật khẩu đã mã hóa trong DB
//            Integer result = passwordEncoder
//                                .matches(request.getPassword(),  user.getPassword())
//                                ? 1 : 0;
//            return result;
//        }
//        return -1;
//    }

    //Đăng nhập có xác thực với jwt
    public LoginResponse login(String email, String rawPassword) {
        try{
            Optional<User> user = userRepository.findByEmail(email);

            if (passwordEncoder.matches(rawPassword, user.get().getPassword())) {
                return new LoginResponse("Đăng nhập thành công!", jwtUtil.generateToken(user.get()));
            }
            return new LoginResponse("Đăng nhập thất bại", "");
        } catch (UsernameNotFoundException e){
            return new LoginResponse("Không tìm thấy tài khoản", "");
        }
    }
}
