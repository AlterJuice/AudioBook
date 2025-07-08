@file:OptIn(ExperimentalContracts::class)

package com.alterjuice.test.audiobook.errors

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


fun Throwable.isAppError(): Boolean {
    contract { returns(true) implies (this@isAppError is AppError) }
    return this is AppError
}
