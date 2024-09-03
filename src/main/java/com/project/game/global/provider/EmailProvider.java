package com.project.game.global.provider;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProvider{

    private final JavaMailSender javaMailSender;
    private final String JOIN_SUBJECT = "[GameForge] 회원가입 인증메일 안내";
    private final String FIND_SUBJECT = "[GameForge] 비밀번호 재설정 안내";
    public boolean sendAuthenticationMail(String email, String authenticationCode){

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getAuthenticationMessage(authenticationCode);

            messageHelper.setTo(email);
            messageHelper.setSubject(JOIN_SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean sendPasswordResetMail(String email, String resetToken) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String resetLink = "http://localhost:3000/api/auth/reset-token?token=" + resetToken;
            String htmlContent = getResetPasswordMessage(resetLink);

            messageHelper.setTo(email);
            messageHelper.setSubject(FIND_SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String getAuthenticationMessage(String authenticationCode){

        String authenticationMessage = "";
        authenticationMessage += "<h1 style='text-align: center;'>[GameForge] 회원가입 인증메일</h1>";
        authenticationMessage += "<h3 style='text-align: center;'>인증 코드 :  " +
                "<strong style='font-size: 32px; letter-spacing: 8px;'>" +
                authenticationCode + "</strong></h3>";

        return authenticationMessage;
    }

    private String getResetPasswordMessage(String resetLink) {

        String resetMessage = "";
        resetMessage += "<h1 style='text-align: center;'>[GameForge] 비밀번호 재설정</h1>";
        resetMessage += "<p style='text-align: center;'>비밀번호를 재설정하려면 아래 버튼을 클릭하세요.</p>";
        resetMessage += "<div style='text-align: center;'><a href='" + resetLink + "' " +
                "style='display: inline-block; padding: 10px 20px; margin: 20px auto; color: white; background-color: #007BFF; " +
                "text-decoration: none; font-size: 18px;'>비밀번호 재설정</a></div>";

        return resetMessage;
    }
}
