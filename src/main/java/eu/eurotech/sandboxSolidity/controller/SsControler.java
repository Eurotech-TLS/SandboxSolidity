package eu.eurotech.sandboxSolidity.controller;
import eu.eurotech.sandboxSolidity.model.BlockchainTransaction;
import eu.eurotech.sandboxSolidity.service.SsService;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController; 


@RestController
public class SsControler {

    private final SsService service;

    public SsControler(SsService service) {
        this.service = service;
    }

    @PostMapping("/transaction")
    public BlockchainTransaction execute(@RequestBody BlockchainTransaction transaction) throws IOException {
        return service.process(transaction);
    }

}
