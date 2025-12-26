package com.gkprojects.cmmsandroidapp.data.local.serialization

import kotlinx.serialization.json.Json

val SimpleJson= Json {
    prettyPrint = true
    encodeDefaults = true
    ignoreUnknownKeys = true
}
