package com.ignite.bank;

import java.math.BigDecimal;

public record Account(String accountNo, String name, BigDecimal openingBalance) {
}
