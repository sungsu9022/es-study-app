package com.starter.es.app.test.models.index

import java.time.ZonedDateTime

data class MyIndexClass(
    val fieldOne: String,
    val fieldTwo: Long,
    val fieldThree: ZonedDateTime,
)

data class MyPartialIndexClass(
    val fieldOne: String,
)
