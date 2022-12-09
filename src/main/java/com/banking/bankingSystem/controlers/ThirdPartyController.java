package com.banking.bankingSystem.controlers;
import com.banking.bankingSystem.modules.Transfer;
import com.banking.bankingSystem.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
@RestController
public class ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/thirdParty-transfer")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal transfer(@RequestBody Transfer transfer, @RequestHeader String hashedKey){
        return thirdPartyService.transfer(transfer, hashedKey);
    }

}
