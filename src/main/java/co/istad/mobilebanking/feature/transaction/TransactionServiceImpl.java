package co.istad.mobilebanking.feature.transaction;


import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.domain.Transaction;
import co.istad.mobilebanking.feature.account.AccountRepository;
import co.istad.mobilebanking.feature.transaction.dto.TransactionRequest;
import co.istad.mobilebanking.feature.transaction.dto.TransactionResponse;
import co.istad.mobilebanking.mapper.TransferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService  {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransferMapper transferMapper;

    @Override
    public TransactionResponse transfer(TransactionRequest transactionRequest) {


        //validate if sender exist
        if(!accountRepository.existsByActNo(transactionRequest.senderActNo())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender Account does not exist.");
        }

        //validate if receiver exist
        if(!accountRepository.existsByActNo(transactionRequest.receiverActNo())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender Account does not exist.");
        }

        Account sender = accountRepository
                .findByActNo(transactionRequest.senderActNo())
                .orElseThrow();

        Account receiver = accountRepository
                .findByActNo(transactionRequest.receiverActNo())
                .orElseThrow();

        //validate sender has money and is sufficeint
        if(sender.getBalance().compareTo(transactionRequest.amount())<1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not has sufficient balance.");
        }else{
            sender.setBalance(sender.getBalance().subtract(transactionRequest.amount()));
            receiver.setBalance(receiver.getBalance().add(transactionRequest.amount()));
        }

        Transaction transfer=transferMapper.fromTransactionRequest(transactionRequest);
        transfer.setTransactionAt(LocalDateTime.now());
        transfer.setIsDeleted(false);
        transfer.setStatus(true);
        transfer.setTransactionType("transfer");

        transfer.setSender(sender);
        transfer.setReceiver(receiver);


        accountRepository.save(sender);
        accountRepository.save(receiver);
        transactionRepository.save(transfer);

        return transferMapper.toTransactionResponse(transfer);
    }
}
