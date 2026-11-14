package egovframework.com.uat.uia.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@UtilityClass
@Slf4j
public class EgovClientIP {

    /**
     * 요청에서 실제 클라이언트 IP 추출.
     * 프록시/게이트웨이 환경에서 X-Forwarded-For, X-Real-IP 헤더를 우선 확인하고,
     * 없을 경우 HttpServletRequest.getRemoteAddr()을 폴백으로 사용.
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return fallbackLocalIp();
        }

        // 1. X-Forwarded-For: 프록시 체인의 첫 번째(원본 클라이언트) IP 사용
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)) {
            String firstIp = Arrays.stream(xff.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .orElse(null);
            if (StringUtils.hasText(firstIp) && !isInternalIp(firstIp)) {
                return firstIp;
            }
        }

        // 2. X-Real-IP: 단일 프록시가 설정하는 헤더
        String xri = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xri) && !isInternalIp(xri)) {
            return xri;
        }

        // 3. 폴백: 서블릿 컨테이너가 보는 원격 주소
        String remote = request.getRemoteAddr();
        if (StringUtils.hasText(remote) && !isInternalIp(remote)) {
            return remote;
        }

        // 4. 최후 폴백: 로컬 호스트 IP
        return fallbackLocalIp();
    }

    /**
     * 로컬/내부 IP 여부 확인 (127.x, 10.x, 192.168.x, 172.16-31.x, ::1 등)
     */
    private static boolean isInternalIp(String ip) {
        if (!StringUtils.hasText(ip)) return true;
        // IPv6 loopback
        if (ip.equals("::1") || ip.equals("0:0:0:0:0:0:0:1")) return true;
        // IPv4 loopback
        if (ip.startsWith("127.")) return true;
        // Private ranges
        if (ip.startsWith("10.")) return true;
        if (ip.startsWith("192.168.")) return true;
        // 172.16-31.x.x
        if (ip.startsWith("172.")) {
            String[] parts = ip.split("\\.");
            if (parts.length >= 2) {
                try {
                    int second = Integer.parseInt(parts[1]);
                    if (second >= 16 && second <= 31) return true;
                } catch (NumberFormatException ignored) {}
            }
        }
        // Link-local
        if (ip.startsWith("169.254.")) return true;
        return false;
    }

    private static String fallbackLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("EgovClientIP.getClientIp Client Ip Invalid", e);
            return "0.0.0.0";
        }
    }

}
