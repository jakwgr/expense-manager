package com.exm.expensemanager.common;

import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.handler.exceptions.UserNotCreated;
import com.exm.expensemanager.handler.exceptions.WrongCurrencyException;
import com.exm.expensemanager.user.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Util {

    private final UserRepository userRepository;

    public int getCurrentUserId()
    {
        var activeUser = this.userRepository.findTopByOrderByIdDesc();

        return activeUser.get().getId();
    }

    public boolean doesCurrencyContains(String value)
    {
        for (Currency currency : Currency.values())
        {
            if(currency.name().equals(value))
            {
                return true;
            }
        }
        return false;
    }



//    public fullCurrency getCurrencyInformation(String value)
//    {
//        if(!doesCurrencyContains(value))
//        {
//            throw new WrongCurrencyException();
//        }
//
//
//    }
}
