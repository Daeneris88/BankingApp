package com.banking.bankingSystem.controlers;
import com.banking.bankingSystem.modules.Transfer;
import com.banking.bankingSystem.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.math.BigDecimal;

public class ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/thirdParty-transfer/{hashedKey}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal transfer(@RequestBody Transfer transfer, @RequestHeader String hashedKey){
        return thirdPartyService.transfer(transfer, hashedKey);
    }

}
