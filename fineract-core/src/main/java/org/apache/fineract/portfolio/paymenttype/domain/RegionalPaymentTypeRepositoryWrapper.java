package org.apache.fineract.portfolio.paymenttype.domain;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.portfolio.paymenttype.exception.PaymentTypeNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionalPaymentTypeRepositoryWrapper implements IPaymentTypeRepositoryWrapper {

    private final PaymentTypeRepository repository;
    @NotNull private final String region; 

    public List<PaymentType> findAll() {
        return this.repository.findAllInRegionByOrderByPositionAsc(region);
    }

    public List<PaymentType> findAllWithCodeName() {
        return this.repository.findAllInRegionByCodeNameIsNotNullOrderByPositionAsc(region);
    }

    public PaymentType findOneWithNotFoundDetection(final Long id) {
        return this.repository.findByIdAndRegion(id, region).orElseThrow(() -> new PaymentTypeNotFoundException(id));
    }

}
