package org.example.payments;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Component("upi")
//@Primary
public class UPIPayment implements PaymentService {

    @Override
    public void createPayment() {
        System.out.println("Payment made via UPI");
    }
}
