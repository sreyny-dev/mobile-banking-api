package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.base.TransactionType;
import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.domain.Favorite;
import co.istad.mobilebanking.domain.Transaction;
import co.istad.mobilebanking.feature.account.AccountRepository;
import co.istad.mobilebanking.feature.account.UserAccountRepository;
import co.istad.mobilebanking.feature.favorite.FavoriteRepository;
import co.istad.mobilebanking.feature.transaction.dto.*;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.mapper.PaymentMapper;
import co.istad.mobilebanking.mapper.TransferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService  {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransferMapper transferMapper;
    private final PaymentMapper paymentMapper;
    private final FavoriteRepository favoriteRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;

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

        if(transactionRequest.senderActNo().equals(transactionRequest.receiverActNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Both sender and receiver accounts are the same");
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
        transfer.setTransactionType(TransactionType.TRANSFER.toString());


        //add to favorite
        if(transactionRequest.favorite()){
            if(!favoriteRepository.existsByActNo(transactionRequest.receiverActNo())) {
                Favorite fav = new Favorite();
                fav.setName(receiver.getAlias());
                fav.setActNo(receiver.getActNo());
                fav.setIsDeleted(false);
                fav.setCreatedAt(LocalDateTime.now());
                fav.setAccounts(receiver);
                fav.setFavoriteType(TransactionType.TRANSFER.toString());
                fav.setUser(userRepository
                        .findByUesrActNo(transactionRequest.senderActNo())
                        .orElseThrow());

                favoriteRepository.save(fav);
            }
        }


        transfer.setSender(sender);
        transfer.setReceiver(receiver);

        accountRepository.save(sender);
        accountRepository.save(receiver);
        transactionRepository.save(transfer);

        return transferMapper.toTransactionResponse(transfer);
    }

    @Override
    public TopUpReponse mobileTopUp(TopUpRequest topUpRequest) {

        if(!accountRepository.existsByActNo(topUpRequest.senderActNo())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        }

        if(topUpRequest.amount().compareTo(BigDecimal.valueOf(1))<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be equal or greater than 1$.");
        }



        Account account=accountRepository
                .findByActNo(topUpRequest.senderActNo())
                .orElseThrow();



        if((account.getBalance().compareTo(topUpRequest.amount()) <0)){
            System.out.println(account.getBalance());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not has sufficient money.");

        }else{
            account.setBalance(account.getBalance().subtract(topUpRequest.amount()));
        }

        Transaction transaction=paymentMapper.fromTopUpRequest(topUpRequest);
        transaction.setTransactionAt(LocalDateTime.now());
        transaction.setIsDeleted(false);
        transaction.setStatus(true);
        transaction.setSender(account);
        transaction.setTransactionType(TransactionType.PAYMENT.name());


        if(topUpRequest.favorite()){
            if(!favoriteRepository.existsByPhoneNumber(topUpRequest.phoneNumber())) {

                Favorite fav = new Favorite();
                fav.setIsDeleted(false);
                fav.setCreatedAt(LocalDateTime.now());
                fav.setFavoriteType(TransactionType.PAYMENT.toString());
                fav.setPhoneNumber(topUpRequest.phoneNumber());

                favoriteRepository.save(fav);
            }
        }


        accountRepository.save(account);
        transactionRepository.save(transaction);



        return paymentMapper.toTopUpResponse(transaction);
    }

    @Override
    public TransactionResponse educationPayment(EducationPaymentRequest educationPaymentRequest) {

        if(!accountRepository.existsByActNo(educationPaymentRequest.senderActNo())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        }

        if(!accountRepository.existsByActNo(educationPaymentRequest.receiverActNo())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        }

        if(educationPaymentRequest.senderActNo().equals(educationPaymentRequest.receiverActNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Both sender and receiver accounts are the same");
        }

        Account sender=accountRepository.findByActNo(educationPaymentRequest.senderActNo()).orElseThrow();
        Account educationInstitute=accountRepository.findByActNo(educationPaymentRequest.receiverActNo()).orElseThrow();


        if(sender.getBalance().compareTo(educationPaymentRequest.amount())<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account does not has sufficient balance.");
        }else {
            sender.setBalance(sender.getBalance().subtract(educationPaymentRequest.amount()));
            educationInstitute.setBalance(educationInstitute.getBalance().add(educationPaymentRequest.amount()));
        }

        Transaction educationPayment=paymentMapper.fromEducationPaymentRequest(educationPaymentRequest);
        educationPayment.setTransactionAt(LocalDateTime.now());
        educationPayment.setIsDeleted(false);
        educationPayment.setStatus(true);
        educationPayment.setTransactionType(TransactionType.PAYMENT.toString());

        educationPayment.setSender(sender);
        educationPayment.setReceiver(educationInstitute);

        //add to favorite
            if (educationPaymentRequest.favorite()) {
                if(!favoriteRepository.existsByActNo(educationPaymentRequest.receiverActNo())) {
                    Favorite fav = new Favorite();
                    fav.setName(educationInstitute.getAlias());
                    fav.setActNo(educationInstitute.getActNo());
                    fav.setIsDeleted(false);
                    fav.setCreatedAt(LocalDateTime.now());
                    fav.setAccounts(educationInstitute);
                    fav.setFavoriteType(TransactionType.TRANSFER.toString());
                    fav.setUser(userRepository
                            .findByUesrActNo(educationPaymentRequest.senderActNo())
                            .orElseThrow());

                    favoriteRepository.save(fav);
                }
            }


//        accountRepository.save(sender);
//        accountRepository.save(educationInstitute);

        transactionRepository.save(educationPayment);

        return paymentMapper.toTransactionResponse(educationPayment);
    }

    @Override
    public List<TransactionResponse> getTransactions(String actNo) {

        if(!accountRepository.existsByActNo(actNo)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        }

        Account account=accountRepository.findByActNo(actNo).orElseThrow();

        List<Transaction> transactionList=transactionRepository.findAllByAccount(account);

        return transferMapper.toTransactionResponseList(transactionList);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        List<Transaction> transactionList=transactionRepository.findAll();

        return transferMapper.toTransactionResponseList(transactionList);
    }

    @Override
    public List<TransactionResponse> getTransactionsBetweenAccounts(String senderActNo, String receiverActNo) {

        if(!accountRepository.existsByActNo(receiverActNo)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver does not exist!");
        }
        if(!accountRepository.existsByActNo(senderActNo)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "sender does not exist!");
        }


        List<Transaction> transactionList=transactionRepository.findAllBetweenAccounts(senderActNo, receiverActNo);



        return transferMapper.toTransactionResponseList(transactionList);
    }

    @Override
    public List<TransactionResponse> getIncome(String actNo) {

        if(!accountRepository.existsByActNo(actNo)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "account does not exist!");
        }

        List<Transaction> transactionList=transactionRepository.findIncome(actNo);

        return transferMapper.toTransactionResponseList(transactionList);
    }

    @Override
    public List<TransactionResponse> getExpense(String actNo) {
        if(!accountRepository.existsByActNo(actNo)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "account does not exist!");
        }

        List<Transaction> transactionList=transactionRepository.findExpense(actNo);

        return transferMapper.toTransactionResponseList(transactionList);
    }

    @Override
    public void deleteTransactionById(Integer id) {
        Transaction transaction=transactionRepository
                .findById(id)
                .orElseThrow();
        transaction.setIsDeleted(true);
        transactionRepository.save(transaction);

    }
}
