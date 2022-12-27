package eu.eurotech.sandboxSolidity.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.web3j.protocol.Web3j;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.spring.autoconfigure.Web3jProperties;

import eu.eurotech.sandboxSolidity.model.BlockchainTransaction;

@Service
public class SsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsService.class);
    
    //private final Web3j web3j;
    
    @Autowired
    Web3j web3j;

	public SsService(Web3j web3j) {
		super();
		this.web3j = web3j;
	}
    
    public BlockchainTransaction process(BlockchainTransaction trx) throws IOException {
    	System.out.println("--------------------------------------------------------------");
    	System.out.println(trx.toString());
		try {
	    	// get basic info 
	    	EthMining mining;
	    	mining = web3j.ethMining().sendAsync().get();
	    	EthCoinbase coinbase = web3j.ethCoinbase().sendAsync().get();
			EthAccounts counts = web3j.ethAccounts().sendAsync().get();
	    	System.out.println("Client is mining: " + mining.getResult());
	    	System.out.println("Coinbase address: " + coinbase.getAddress());
	    	System.out.println("Account : " + counts.getAccounts().get(0));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			System.out.println("Localiced: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

    	
    	EthAccounts accounts = web3j.ethAccounts().send();
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	
    	
    	Iterator<String> it = accounts.getAccounts().iterator();
    	while(it.hasNext()) {
    		System.out.println("Cuenta: " + it.next());
    	}
    	
    	EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(accounts.getAccounts().get(trx.getFromId()), DefaultBlockParameterName.LATEST).send();
    	
    	Transaction transaction = Transaction.createEtherTransaction(
    			accounts.getAccounts().get(trx.getFromId()), 
    			transactionCount.getTransactionCount(), 
    			BigInteger.valueOf(trx.getValue()), 
    			BigInteger.valueOf(21_000), 
    			accounts.getAccounts().get(trx.getToId()),
    			BigInteger.valueOf(trx.getValue()));
    	
    	EthSendTransaction response = web3j.ethSendTransaction(transaction).send();
    	
    	if(response.getError() != null) {
    		trx.setAccepted(false);
    		LOGGER.info("TX rejected: {}", response.getError().getMessage());
    		return trx;
    	}
    	
    	trx.setAccepted(true);
    	String txHash = response.getTransactionHash();
    	LOGGER.info("Tx hash: {}", txHash);
    	
    	trx.setId(txHash);
    	EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();
    	
    	receipt.getTransactionReceipt().ifPresent(transactionReceipt -> LOGGER.info("Tx receipt: {}", transactionReceipt.getCumulativeGasUsed().intValue()));
    	
    	return trx;
    }

}
