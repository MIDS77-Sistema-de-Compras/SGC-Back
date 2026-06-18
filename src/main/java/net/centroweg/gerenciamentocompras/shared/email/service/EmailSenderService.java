package net.centroweg.gerenciamentocompras.shared.email.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    
    private final JavaMailSender mailSender;

    public void sendEmail(DefaultEmail email) throws MessagingException{ // this might have to change to be catched by GlobalHandler
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email.getSendTo());
        helper.setSubject(email.getSubject());
        
        String content = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                        .card { background-color: #ffffff; max-width: 500px; margin: 0 auto; border-radius: 8px; border-top: 5px solid #103D85; box-shadow: 0 4px 8px rgba(0,0,0,0.05); }
                        h1 { color: #333333; padding: 30px 30px 0 30px; font-size: 24px; margin-bottom: 20px; }
                        p { color: #666666; padding: 0 30px 0 30px; line-height: 1.6; font-size: 14px; }
                        a { color: #103D85; padding: 30px 30px 0 30px; font-size: 14px; }
                        a:hover { color: #0b2a5c; }
                        small { color: #d4d4d4; padding: 30px 30px 0 30px }
                        .btn { display: inline-block; padding: 12px 24px; margin: 30px 30px 0 30px; background-color: #103D85; color: #ffffff !important; text-decoration: none; border-radius: 4px; font-weight: bold; margin-top: 20px; }
                        .btn:hover { background-color: #0b2a5c; }
                        .footer { 
                            display: flex;
                            flex-direction: column;
                            margin-top: 30px;
                            padding-bottom: 24px;
                            font-size: 12px; 
                            background-color: #103D85; 
                            text-align: center; 
                            border-radius: 8px;
                        }
                        .footer .images {
                            margin-top:20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h1>%s</h1>
                        
                        %s
                        
                        <div class="footer">
                            <div class="images">
                                <img src="https://i.postimg.cc/sBPrtXpY/SENAI.png" alt="Logo SENAI">
                                <img src="https://i.postimg.cc/LqkprXzk/SGS.png" alt="Logo SGS">
                            </div>
                            <small>SENAI | SGS &copy 2026</small>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(email.getSubject(), email.getText());

        helper.setText(content, true);

        mailSender.send(message);
    }
    
}
