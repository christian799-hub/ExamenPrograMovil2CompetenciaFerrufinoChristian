package edu.ucb.cashin.Crypto.data.mapper

import edu.ucb.cashin.Crypto.data.dto.CryptoDto
import edu.ucb.cashin.Crypto.domain.model.CryptoModel


fun CryptoDto.toDomain() : CryptoModel = CryptoModel(
    id = id,
    name = name,
    currentPrice = currentPrice
)

