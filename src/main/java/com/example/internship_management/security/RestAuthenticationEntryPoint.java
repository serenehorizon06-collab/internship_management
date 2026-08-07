package com.example.internship_management.security;

import com.example.internship_management.common.ErrorResponse;
import com.example.internship_management.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	public static final String AUTH_ERROR_CODE_ATTRIBUTE = "authErrorCode";
	public static final String AUTH_ERROR_MESSAGE_ATTRIBUTE = "authErrorMessage";

	private final ObjectMapper objectMapper;

	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		ErrorCode errorCode = getAttribute(request, AUTH_ERROR_CODE_ATTRIBUTE, ErrorCode.INVALID_JWT_TOKEN);
		String message = getAttribute(
				request,
				AUTH_ERROR_MESSAGE_ATTRIBUTE,
				"Token xác thực không hợp lệ hoặc bị thiếu");

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(
				new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, errorCode, message, List.of())));
	}

	@SuppressWarnings("unchecked")
	private <T> T getAttribute(HttpServletRequest request, String name, T defaultValue) {
		Object value = request.getAttribute(name);
		if (value == null) {
			return defaultValue;
		}
		return (T) value;
	}
}
