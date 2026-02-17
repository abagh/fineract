package org.apache.fineract.portfolio.paymenttype.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.portfolio.paymenttype.exception.PaymentTypeNotFoundException;
import org.springframework.stereotype.Service;

public interface IPaymentTypeRepositoryWrapper {
    List<PaymentType> findAll();
    List<PaymentType> findAllWithCodeName();
    PaymentType findOneWithNotFoundDetection(final Long id);
}
