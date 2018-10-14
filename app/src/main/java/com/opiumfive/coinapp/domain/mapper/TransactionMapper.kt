package com.opiumfive.coinapp.domain.mapper

import com.opiumfive.coinapp.data.bdModel.transactions.TransactionBdModel
import com.opiumfive.coinapp.data.serverModel.transactions.TransactionsResponse
import com.opiumfive.coinapp.data.uiModel.transactions.TransactionsModel

object TransactionMapper {

    fun map(transactionsResponse: TransactionsResponse, timestamp: Long?): TransactionsModel {
        return TransactionsModel(
            id = transactionsResponse.id ?: 0,
            address = transactionsResponse.address ?: "",
            amount = transactionsResponse.amount ?: .0,
            confirmations = transactionsResponse.confirmations ?: 0,
            direction = transactionsResponse.direction ?: "",
            fees = transactionsResponse.fees ?: .0,
            timestamp = timestamp ?: 0L,
            transactionDate = transactionsResponse.transactionDate ?: "",
            walletId = transactionsResponse.walletId ?: 0,
            walletType = transactionsResponse.walletType ?: ""
        )
    }

    fun map(transactionBdModel: TransactionBdModel): TransactionsModel {
        return TransactionsModel(
            id = transactionBdModel.id,
            address = transactionBdModel.address,
            amount = transactionBdModel.amount,
            confirmations = transactionBdModel.confirmations,
            direction = transactionBdModel.direction,
            fees = transactionBdModel.fees,
            timestamp = transactionBdModel.timestamp,
            transactionDate = transactionBdModel.transactionDate,
            walletId = transactionBdModel.walletId,
            walletType = transactionBdModel.walletType
        )
    }

    fun mapBd(transactionsResponse: TransactionsResponse, timestamp: Long?): TransactionBdModel {
        return TransactionBdModel(
            id = transactionsResponse.id ?: 0,
            address = transactionsResponse.address ?: "",
            amount = transactionsResponse.amount ?: .0,
            confirmations = transactionsResponse.confirmations ?: 0,
            direction = transactionsResponse.direction ?: "",
            fees = transactionsResponse.fees ?: .0,
            timestamp = timestamp ?: 0L,
            transactionDate = transactionsResponse.transactionDate ?: "",
            walletId = transactionsResponse.walletId ?: 0,
            walletType = transactionsResponse.walletType ?: ""
        )
    }
}