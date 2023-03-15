package com.maktabsharif.homeservices.controller;


import com.maktabsharif.homeservices.dto.TransactionDto;
import com.maktabsharif.homeservices.validation.Captcha;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static String srvCaptcha = null;

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute TransactionDto transactionDto) throws Exception {
        transactionDto.setSrvCaptcha(srvCaptcha);
        System.out.println("Source card number: " + transactionDto.getSrcCardNumber());
        System.out.println("Destination card number: " + transactionDto.getDestCardNumber());
        System.out.println("Transaction amount: " + transactionDto.getTransactionAmount());
        System.out.println("CVV2: " + transactionDto.getCvv2());
        System.out.println("Expiration year: " + transactionDto.getExpYear());
        System.out.println("Expiration month: " + transactionDto.getExpMonth());
        System.out.println("Client captcha: " + transactionDto.getCltCaptcha());
        System.out.println("Server captcha from dto: " + transactionDto.getSrvCaptcha());
        System.out.println("Server captcha: " + srvCaptcha);
        System.out.println("Second PIN: " + transactionDto.getSecPin());
        System.out.println("CVV2 and ED are saved: " + transactionDto.getCvv2EdSaved());

        return "OK";
    }

    @GetMapping("/captcha")
    public String generateCaptcha() throws Exception {
        String cltCaptcha = Captcha.returnCaptchaText();
        srvCaptcha = cltCaptcha;
        return cltCaptcha;
    }

}
