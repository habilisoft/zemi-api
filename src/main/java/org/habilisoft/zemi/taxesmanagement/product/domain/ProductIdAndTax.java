package org.habilisoft.zemi.taxesmanagement.product.domain;

import org.habilisoft.zemi.catalog.product.domain.ProductId;
import org.habilisoft.zemi.taxesmanagement.tax.domain.Tax;

public interface ProductIdAndTax {
    ProductId getProductId();
    Tax getTax();
}
