package com.trackswiftly.vehicle_service.aspectj;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trackswiftly.vehicle_service.annotations.LogUserOperation;
import com.trackswiftly.vehicle_service.dao.repositories.UserLogRepo;
import com.trackswiftly.vehicle_service.entities.UserLog;

import jakarta.servlet.http.HttpServletRequest;


@Slf4j
@Aspect
@Component
public class UserOperationLoggerAspect {

    private UserLogRepo userLogRepository;

    @Autowired
    UserOperationLoggerAspect(
        UserLogRepo userLogRepository
    ) {
        this.userLogRepository = userLogRepository;
    }

    
    @Around("@annotation(com.trackswiftly.vehicle_service.annotations.LogUserOperation)")
	public void logUserOperation(ProceedingJoinPoint joinPoint) throws Throwable {

		log.debug("Hello 🔖") ;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String userId = null;
        String username = null;


        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            // Extract the user ID from the JWT token's "sub" claim
            Jwt jwt = (Jwt) authentication.getPrincipal();
            userId = jwt.getSubject(); // "sub" claim
            username = jwt.getClaim("preferred_username"); // Optional: Extract username from another claim
        }

        else {
            userId = "Anonymous";
            username = "Anonymous";
        }

        log.info("UserName : {} ✅" , username);
        log.info("UserId : {} ✅" , userId);



        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LogUserOperation annotation = signature.getMethod().getAnnotation(LogUserOperation.class);


        String operationDescription = annotation.value().isEmpty() ? signature.getMethod().getName() : annotation.value();


        /**
         *  Get the IP address of the user
         */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();


        log.info("Ip Address : {}" , ipAddress);


        UserLog userLog = UserLog.builder()
        .userId(userId)
        .username(username)
        .operation(operationDescription)
        .method(signature.getMethod().getName())
        .ipAddress(ipAddress)
        .status("STARTED") // Initial status
        .build();

        userLogRepository.save(userLog);


        try {
            // Proceed with the method execution
           joinPoint.proceed();

            // Update the log entry for successful completion
            userLog.setStatus("SUCCESS");
            userLog.setUpdatedAt(Instant.now());
            userLogRepository.update(userLog); // Update the log

        } catch (Exception e) {

            // Update the log entry for failure
            userLog.setStatus("FAILURE");
            userLog.setUpdatedAt(Instant.now());
            userLogRepository.update(userLog); // Update the log

            throw e;
        }

    }
}
