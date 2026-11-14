package egovframework.com.uat.uia.web;

import egovframework.com.uat.uia.service.LoginVO;
import egovframework.com.uat.uia.util.EgovJwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller("uiaEgovLoginManageController")
@RequestMapping("/uat/uia")
@RequiredArgsConstructor
public class EgovLoginManageController {

    private final EgovJwtProvider jwtProvider;

    @GetMapping(value="/index")
    public String login(LoginVO loginVO, Model model, HttpServletRequest request) {
        return this.loginView(loginVO, model, request);
    }

    @RequestMapping(value="/loginView", method={RequestMethod.GET, RequestMethod.POST})
    public String loginView(LoginVO loginVO, Model model, HttpServletRequest request) {
        String accessToken = jwtProvider.getCookie(request, "accessToken");
        if (ObjectUtils.isEmpty(accessToken)) {
            loginVO = new LoginVO();
            model.addAttribute("loginVO", loginVO);
            return "uat/uia/login";
        }
        try {
<<<<<<< HEAD
            // accessToken은 accessSecret으로 서명되므로 accessExtractClaims 사용
=======
            // accessToken은 accessSecret으로 서명되므로 accessExtractClaims 사용 accessExtractClaims  사용
>>>>>>> c4c88aa (fix(login): extract real client IP from request (X-Forwarded-For, X-Real-IP) for IP login policy)
            Claims claims = jwtProvider.accessExtractClaims(accessToken);
            String userId = jwtProvider.decrypt(ObjectUtils.isEmpty(claims.get("userId")) ? "" : claims.get("userId").toString());
            String userNm = jwtProvider.decrypt(ObjectUtils.isEmpty(claims.get("userNm")) ? "" : claims.get("userNm").toString());
            loginVO.setUserInfo(userNm + "(" + userId + ")");
            model.addAttribute("loginVO", loginVO);
            return "uat/uia/content";
        } catch (JwtException | IllegalArgumentException e) {
<<<<<<< HEAD
            // 만료·서명 불일치 등 잘못된 accessToken 쿠키는 로그인 화면으로 폴백
=======
            // 리팩토링된 인증 로직으로 accessToken 검증 과정 단순화 및 가독성 향상
>>>>>>> c4c88aa (fix(login): extract real client IP from request (X-Forwarded-For, X-Real-IP) for IP login policy)
            loginVO = new LoginVO();
            model.addAttribute("loginVO", loginVO);
            return "uat/uia/login";
        }
    }

    @RequestMapping(value="/loginForbidden", method={RequestMethod.GET, RequestMethod.POST})
    public String loginForbidden(@RequestParam(value = "pathCode", required = false, defaultValue = "1") String pathCode, Model model) {
        model.addAttribute("pathCode", pathCode);
        return "uat/uia/forbidden";
    }

}
